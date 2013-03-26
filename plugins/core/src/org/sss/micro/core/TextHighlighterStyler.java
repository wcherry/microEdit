package org.sss.micro.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

class TextHighlighterStyler implements LineStyleListener {
    Map<String, List<StyleRange>> rangesMap = new HashMap<String, List<StyleRange>>();
    StyledText textArea = null;

    public TextHighlighterStyler(StyledText ta) {
        textArea = ta;
    }

    protected String generateGUID(){
        return Long.toString(System.currentTimeMillis());
    }


    public String highlightTextRanges(List<TextRange> ranges, Color bgColor){
        String guid = generateGUID();
        List<StyleRange> r = new ArrayList<StyleRange>();

        for(TextRange t : ranges){
            int start = textArea.getLineIndex(t.line) + t.start;
            r.add(new StyleRange(start, t.length, null, bgColor));
        }
        rangesMap.put(guid, r);
        return guid;
    }

    public void removeHighlights(String guid){
        rangesMap.remove(guid);
    }

    protected StyleRange[] getStyles(int start, int end){
        Vector styles = new Vector();

        for(List<StyleRange> list : rangesMap.values()){
            for(StyleRange s : list){
                if(s.start >= start && s.start <= end){
                    styles.add(s);
                }
            }
        }

        StyleRange[] stylesAr = new StyleRange[styles.size()];
        styles.copyInto(stylesAr);
        return stylesAr;
    }

    /**
     * Event.detail			line start offset (input)
     * Event.text 			line text (input)
     * LineStyleEvent.styles 	Enumeration of StyleRanges, need to be in order. (output)
     * LineStyleEvent.background 	line background color (output)
     */
    public void lineGetStyle(LineStyleEvent event) {
        StyleRange[] styles = getStyles(event.lineOffset, event.lineOffset+event.lineText.length());
        event.styles = styles;
    }
}
