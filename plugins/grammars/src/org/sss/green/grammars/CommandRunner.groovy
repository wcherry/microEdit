package org.sss.micro.grammars

import org.sss.micro.core.PluginContext
import org.sss.micro.core.Window

/**
 * Created with IntelliJ IDEA.
 * User: wcherry
 * Date: 6/7/12
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
class CommandRunner {

    public static final long TIMEOUT_IN_MILLIS = 60000  // 60 Seconds
    public static final String ENV_VAR_NAMES = [
            'TM_BUNDLE_SUPPORT',    // shell commands which are (indirectly) triggered from a bundle item (which could be a Command, Drag Command, Macro, or Snippet) will have this variable pointing to the Support folder of the bundle that ran the item, if such a folder exists. In addition, $TM_BUNDLE_SUPPORT/bin will be added to the path.
            'TM_CURRENT_LINE',      // textual content of the current line.
            'TM_CURRENT_WORD',      // the word in which the caret is located.
            'TM_DIRECTORY',         // the folder of the current document (may not be set).
            'TM_FILEPATH',          // path (including file name) for the current document (may not be set).
            'TM_LINE_INDEX',        // the index in the current line which marks the caret’s location. This index is zero-based and takes the utf-8 encoding of the line (e.g. read as TM_CURRENT_LINE) into account. So to split a line into what is to the left and right of the caret you could do:
            'TM_LINE_NUMBER',       // the carets line position (counting from 1). For example if you need to work with the part of the document above the caret you can set the commands input to “Entire Document” and use the following to cut off the part below and including the current line:'
            'TM_PROJECT_DIRECTORY', // the top-level folder in the project drawer (may not be set).
            'TM_SCOPE',             // the scope that the caret is inside. See scope selectors for information about scopes.
            'TM_SELECTED_FILES',    // space separated list of the files and folders selected in the project drawer (may not be set). The paths are shell-escaped, so to use these, you need to prefix the line with eval (to make the shell re-evaluate the line, after expanding the variable). For example to run the file command on all selected files in the project drawer, the shell command would be:
            'TM_SELECTED_FILE',     // full path of the first selected file or folder in the project drawer (may not be set).
            'TM_SELECTED_TEXT',     // full content of the selection (may not be set). Note that environment variables have a size limitation of roughly 64 KB, so if the user selects more than that, this variable will not reflect the actual selection (commands that need to work with the selection should generally set this to be the standard input).
            'TM_SOFT_TABS',         // this will have the value YES if the user has enabled soft tabs, otherwise it has the value NO. This is useful when a shell command generates an indented result and wants to match the users preferences with respect to tabs versus spaces for the indent.
            'TM_SUPPORT_PATH',      // the TextMate application bundle contains a support folder with several items which are used by some of the default commands (for example CocoaDialog, Markdown, the SCM commit window, Textile, tidy, etc.). This variable points to that support folder. Generally you would not need to use the variable directly since $TM_SUPPORT_PATH/bin is added to the path, so using some of the bundled commands can be done without having to specify their full path.
            'TM_TAB_SIZE'          // the tab size as shown in the status bar. This is useful when creating commands which need to present the current document in another form (Tidy, convert to HTML or similar) or generate a result which needs to match the tab size of the document. See also TM_SOFT_TABS.
            ]
    PluginContext context

    CommandRunner(PluginContext context) {
        this.context = context
    }

    public String[] getEnv(){
        Window win = context.appWindow.focusedWindow

        Map env = System.getenv();
        //env.put()   //TM_BUNDLE_SUPPORT
        env.put('TM_CURRENT_LINE', win.getLine(win.caretLine))
        env.put('TM_CURRENT_WORD', context.Grammars.getCurrentWord(win))
        env.put('TM_DIRECTORY', win.document.file?.parent?.toString())
        env.put('TM_FILEPATH', win.document.file?.toString())
        env.put('TM_LINE_INDEX', win.caretColumn)
        env.put('TM_LINE_NUMBER', win.caretLine)
        //TODO: Add additional support for TM environment variables.
        //TM_PROJECT_DIRECTORY
        //TM_SCOPE
        //TM_SELECTED_FILES
        //TM_SELECTED_FILE
        //TM_SELECTED_TEXT
        //TM_SOFT_TABS
        //TM_SUPPORT_PATH
        //TM_TAB_SIZE

        env.collect{k,v->"$k=$v"}.toArray()
    }

    Map<String, String> runCommand(String cmd){
        StringWriter out = new StringWriter()
        StringWriter err = new StringWriter()
        Process process = Runtime.getRuntime().exec(cmd,getEnv())
        process.consumeProcessOutput(out, err)
        process.waitForOrKill(TIMEOUT_IN_MILLIS)
        int rc = process.exitValue()
        [rc.toString(), out.toString(), err.toString()]
    }
}
