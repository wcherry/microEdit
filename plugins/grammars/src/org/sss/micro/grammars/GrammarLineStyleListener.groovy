/*
* Copyright © 2013 William R. Cherry
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package org.sss.micro.grammars

import java.util.regex.*
import org.eclipse.swt.custom.StyleRange
import org.eclipse.swt.custom.*
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.widgets.Display

class GrammarLineStyleListener implements LineStyleListener {
    Map colorMap = null
    Map patternsCache = [:]

    GrammarLineStyleListener(Map colorMap) {
        this.colorMap = colorMap
    }

    protected java.util.regex.Pattern compilePattern(String text){
        String t = text
        java.util.regex.Pattern p = patternsCache[text]
        if(p) return p  // Shortcut if in cache
        try{
            if(text =='{') t = '\\'+text
            p = java.util.regex.Pattern.compile(t)
            patternsCache[text] = p
        } catch(PatternSyntaxException e){
            println "Error with pattern '$text'"
            throw e
        }
        return p

    }

    protected List<StyleRange> processPattern(def pattern, String line, int lineOffset) {
        List styles = []
        try{
        if (pattern.match) {
            java.util.regex.Pattern p = compilePattern(pattern.match)
            Matcher m = line =~ p
            if (m) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    def token = pattern.captures?(pattern.captures[i.toString()]?.name):null
                    if (token) {
                        def color = colorMap[token]
                        // "1:Captured text '${m[0][i]}' for token ${token} with color $color"
                        if (color) {
                            StyleRange style = new StyleRange(m.start(i) + lineOffset, m.end(i) - m.start(i), color, null)
                            styles << style
                        }  else println "&&& No color map entry for ${token}"
                    } else {
                        // no capture groups means we match the entire text
                        def color = colorMap[pattern.name]
                        //println "Start: ${m.start()}, End: ${m.end()}, Text: ${line.substring(m.start(), m.end())}"
                        if (color) {
                            StyleRange style = new StyleRange(m.start() + lineOffset, m.end() - m.start(), color, null)
                            styles << style
                        }  else println "&&& No color map entry for ${pattern.name}"
                        //println "1b:Captured text '\${m[0][i]}' for token \${token} with color \$color\""
                    }
                }
            }
        } else if (pattern.begin) {
            //BEGIN
            java.util.regex.Pattern p = compilePattern(pattern.begin)
            //println pattern.begin
            Matcher m = line =~ p
            if (m) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    def token = pattern.beginCaptures ? (pattern.beginCaptures[i.toString()]?.name) : null
                    if (token) {
                        def color = colorMap[token]
                        //println "2:Captured text ${m[0][i]} for token ${token} with color $color"
                        if (color) {
                            StyleRange style = new StyleRange(m.start(i) + lineOffset, m.end(i) - m.start(i), color, null)
                            styles << style
                        }  else println "&&& No color map entry for ${token}"
                    }
                }

                // MIDDLE
                pattern.patterns.each {
                    if (it.match) {
                        p = compilePattern(it.match)
                        m = line =~ p
                        if (m) {
                            for (int i = 0; i <= m.groupCount(); i++) {
                                def token = it.captures ? (it?.captures[i.toString()]?.name) : null
                                if (token) {
                                    def color = colorMap[token]
                                    //println "3:Captured text '${m[0][i]}' for token ${token} with color $color"
                                    if (color) {
                                        StyleRange style = new StyleRange(m.start(i) + lineOffset, m.end(i) - m.start(i), color, null)
                                        styles << style
                                    }  else println "&&& No color map entry for ${token}"
                                }
                            }
                        }
                    } else if(it.begin){
                        //println "Begin found"
                    }
                }

                //END
                if (pattern.end) {
                    p = compilePattern(pattern.end)
                    m = line =~ p
                    if (m) {
                        for (int i = 0; i <= m.groupCount(); i++) {
                            def token = pattern.endCaptures?(pattern.endCaptures[i.toString()]?.name):null
                            if (token) {
                                def color = colorMap[token]
                                //println "4:Captured text '${m[0][i]}' for token ${token} with color $color"
                                if (color) {
                                    StyleRange style = new StyleRange(m.start(i) + lineOffset, m.end(i) - m.start(i), color, null)
                                    styles << style
                                }   else println "&&& No color map entry for ${token}"
                            }
                        }

                    }
                }
            }
        }
        }catch(PatternSyntaxException e){
            e.printStackTrace(System.out)
        }
        return styles
    }


    public void lineGetStyle(LineStyleEvent event) {
        try{
        Grammar g = event.widget.getData('plaintextwindow').grammar
        if (!g) return
        def styles = []
        g.patterns.each {pattern ->
            styles.addAll(processPattern(pattern, event.lineText, event.lineOffset))
        }

        g.repository.each {name, repo ->
            //println "Trying repo $name"
            repo.patterns.each {
                //println "trying repo pattern $it"
                styles.addAll(processPattern(it, event.lineText, event.lineOffset))
            }
        }
        if (g.repository)
            g.repository["storage-modifiers"].patterns.each {
                styles.addAll(processPattern(it, event.lineText, event.lineOffset))
            }


        if (!styles.isEmpty()) event.styles = styles.toArray(new StyleRange[styles.size()])

    } catch(Exception ex){
        System.err.println( "Failed to syntax highlight document due to error:")
        ex.printStackTrace()
    }
    }

}
