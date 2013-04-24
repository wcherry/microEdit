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
package org.sss.micro.core

import java.util.regex.Pattern
import java.util.regex.Matcher
import org.eclipse.swt.graphics.Color
import org.sss.micro.swt.DialogHelper

class SearchMgr {
    private TextHighlighterStyler textHighlighter = null
    String lastSearchExpression = null
    String highlightGUID = null
    PluginContext context
	SourceWindow win = null

    //        redraw()
    //if (ranges) {
    //    textArea.setCaretOffset(ranges[0].start)
    //    println "Moved caret to ${ranges[0].start}"
    //}

    SearchMgr(PluginContext context, SourceWindow win){
        this.context = context
		this.win = win

        textHighlighter = new  TextHighlighterStyler(win.textArea)
        win.addLineStyler(textHighlighter)

    }

    int highlightMatches(String p, Document doc) {
        if(highlightGUID){
            textHighlighter.removeHighlights(highlightGUID)
            highlightGUID = null
        }
        List ranges = []
        String text = doc.content
        Pattern pattern = Pattern.compile(p)
        Matcher m = pattern.matcher(text)
        while (m) {
            ranges << new TextRange(start: m.start(), end: m.end())
            //println "Found at ${m.start()} to ${m.end()}"
        }
        Color bg = context.appWindow.colors['red']

        highlightGUID = textHighlighter.highlightTextRanges(ranges, bg)
        ranges.size()
    }

    void search() {
        lastSearchExpression = DialogHelper.showInputDialog(context, context.getResourceString('searchDialog.title'), context.getResourceString('searchDialog.text'))
        if (lastSearchExpression) {
            int cnt = highlightMatches(lastSearchExpression, win.document)
			win.redraw()
            DialogHelper.showMessageDialog(context, context.getResourceString('searchDialog.results.title'), context.getResourceString('searchDialog.results.text', cnt))
        }
    }

    void searchAgain() {
        int line = getCaretLine()
        int col = getCaretColumn()
        for (int i = line; i < textArea.getLineCount(); i++) {
            String s = textArea.getLine(i)
            s = s.substring(col)
            int pos = s.indexOf(lastSearchExpression)
            if (pos > -1) {
                setCaretPosition(i, pos)
                return
            }
        }

    }
}
