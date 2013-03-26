package org.sss.micro.grammars

import org.sss.micro.core.SourceWindow
import org.sss.micro.textmate.Helper
import org.sss.micro.core.PluginContext
import org.sss.micro.core.Window
import org.sss.micro.core.Document

class GrammarsManager /*extends PluginCore*/ {
    private static final String BUNDLES_BASE = "\\assets\\grammars\\bundles"
    private static final String COLORS_FILE ="\\assets\\grammars\\colors.properties"

    File bundlesDir = null
    PluginContext context = null;
    Map scopes = [:]
    Map tokenColorsMap =[:]

    Grammar defaultGrammar = new Grammar()
    GrammarLineStyleListener listener = null

    def showGrammars() {
        def win = new SourceWindow()
        String text = context.registeredFileExtensions.join("\n")
        win.setBody(text)
        context.appWindow.addWindow(win)
    }

    def switchGrammar() {
        Window win = context.appWindow.focusedWindow
        if (!win) return
        Document doc = win.document
        if (!doc) return

        def dlg = new org.sss.micro.swt.ComboBoxDialog(app.shell, 'Switch Grammar', 'Select')
        scopes.each {name, value -> dlg.data << name}
        if (doc.grammar) dlg.selected = doc.grammar.name
        def s = dlg.show()
        if (s == org.eclipse.swt.SWT.OK) {
            //doc.grammar = grammars[dlg.value]

            // Lookup the styler in the map by name of the grammar, if not found then lookup and use
            // the default. The styler are loaded in as classes and need to be instaited.
            String name = dlg.value
//      Styler styler = stylers[name]
//      if(!styler) {name = defaultStyler; styler = stylers[name]}
//      if(styler instanceof Class){ 
//        styler = styler.newInstance()
//        stylers[name] = styler
//      }
//      win.setStyler(styler)
        }

    }

    List getCurrentWord(Window win){
        int caretOffset = win.caretColumn
        if (caretOffset == 0) return null  // short-circuit if at start of line
        int lineNumber = win.caretLine
        String line = win.getLine(lineNumber)
        int pos = GrammarHelper.findLast(line, caretOffset, ' .()[];') + 1

        String key = line.substring(pos, caretOffset).trim()
        [key, lineNumber, pos, caretOffset]
    }

    def completeSnippet() {
        Window win = context.appWindow.focusedWindow
        def word = getCurrentWord(win);
        if (word) {
            def (key, line, start, end) = word
            Snippet snippet = win.document?.grammar?.snippets?.get(key)
            if(snippet != null){
                context.appWindow.alert("Completing snippet $key with $snippet")
                win.replaceText(line,start, end, snippet.getSimpleString())
            }
        }
    }

    def load(PluginContext context) {
        println "Loading Grammars plugin"
        this.context = context

        bundlesDir = new File(context.pluginDir, BUNDLES_BASE)
        processBundles()

        loadColors()

        listener = new GrammarLineStyleListener(tokenColorsMap)

        context.registerEvent('grammarChanged')

        context.addEventListener('grammarChanged'){data->
            data.window.addLineStyler(listener)
            data.window.redraw()
        }
    }

    def loadColors(){
        Properties colors = new Properties()
        colors.load(new FileReader(new File(context.pluginDir, COLORS_FILE)))
        //context.appWindow.colors.clear()
        colors.each{token,colorName->
            def color = context.appWindow.colors[colorName]
            if(color) tokenColorsMap[token] = color
        }


    }

    def start(PluginContext context) {
        println "Starting Grammars plugin"
        def registeredFileExtensions = []
        scopes.each {name, grammar -> registeredFileExtensions << grammar.fileTypes}
        registeredFileExtensions = registeredFileExtensions.flatten()
        context.registeredFileExtensions = registeredFileExtensions
        context.addEventListener('windowAdd') {data ->
            data.window.addLineStyler(listener)
        }

        SourceWindow.metaClass.setGrammar ={Grammar g->
            data.grammar = g
            context.fireEvent('grammarChanged', [window: delegate, grammar: g])
        }


        context.addEventListener('documentLoaded') {data ->
            def g = findGrammarByFilename(data.filename)
            data.window.grammar = g
            //println "${data.filename} = $g"
            data.window.addLineStyler(listener)
            data.window.redraw()
        }
        println "Completed starting Grammars plugin"

    }

    def stop(PluginContext context) {}

    def unload(PluginContext context) {}

    Grammar findGrammarByFilename(String filename) {
        String extension = filename.split('\\.')[-1]
        def g = scopes.find {name, grammar -> grammar.fileTypes?.contains(extension)}
        if (g) g = g.value
        else g = defaultGrammar
        g

    }

    Grammar findGrammarByName(String grammarName) {
        def g = scopes.find {name, grammar -> grammar.name==grammarName||name==grammarName}
        if (g) g = g.value
    }


    void processBundles(){
        bundlesDir.eachFileRecurse(groovy.io.FileType.FILES) {file ->
            try{
                if (file.name.endsWith('.tmLanguage') || (file.parentFile.name == 'Syntaxes' && file.name.endsWith('plist'))) {
                    processGrammarFile(file)
                }
                if (file.name.endsWith('.tmSnippet') || (file.parentFile.name == 'Snippets' && file.name.endsWith('plist'))) {
                    processSnippetFile(file)
                }
                if (file.name.endsWith('.tmCommand') || (file.parentFile.name == 'Commands' && file.name.endsWith('plist'))) {
                    processCommandFile(file)
                }

                if (file.name.endsWith('.tmPreferences') || (file.parentFile.name == 'Preferences' && file.name.endsWith('plist'))) {
                    processPreferencesFile(file)
                }
            } catch (Exception e) {
                println "The langauge file ${file.absolutePath} has the following error: ${e.message}"
                e.printStackTrace()
            }
        }
    }

    Grammar getOrCreateGrammar(String scopeName){
        Grammar g = scopes[scopeName]
        if(!g){
            g = new Grammar()
            scopes[scopeName] = g
        }
        return g
    }

    void processGrammarFile(File file) {
        def map = Helper.processLanguageFile(file)
        String scope = map?.scopeName
        if (!scope) {println "The langauge file $file is missing the required attribute 'scopeName'";return}
        println "Processing grammar $scope from $file"

        Grammar g = getOrCreateGrammar(scope)
        GrammarHelper.updateGrammarFromMap(map, g)
    }

    void processSnippetFile(File file) {
        Snippet snippet = GrammarHelper.createSnippetFromBundleFile(file)
        if (snippet && snippet.scope && snippet.tabTrigger){    //TODO: Currently only tab trigger are supported
            Grammar g = getOrCreateGrammar(snippet.scope)
            g.addSnippet(snippet.tabTrigger, snippet)
        }
        else println "The snippet file ${file.absolutePath} is missing the scopeName attribute"
    }

    void processCommandFile(File file){}

    void processPreferencesFile(File file){}

    String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[")
        scopes.each {k,v-> sb.append(v.name);sb.append(",")}
        sb.append("]")
        return sb.toString()
    }
}