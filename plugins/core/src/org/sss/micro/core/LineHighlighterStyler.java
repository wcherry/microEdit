package org.sss.micro.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Vector;

class LineHighlighterStyler implements LineBackgroundListener {
    Color bgColor;

    public LineHighlighterStyler(Color c) {
        bgColor = c;
    }

    /**
     * Event.detail			line start offset (input)
     * Event.text 			line text (input)
     * LineStyleEvent.styles 	Enumeration of StyleRanges, need to be in order. (output)
     * LineStyleEvent.background 	line background color (output)
     */
    public void lineGetBackground(LineBackgroundEvent event) {
        StyledText textArea = (StyledText) event.widget;
        int offset = textArea.getCaretOffset();
        boolean current = textArea.getLineAtOffset(offset) == textArea.getLineAtOffset(event.lineOffset);

        // If the line is part of a block comment, create one style for the entire line.
        if (current) {
            event.lineBackground = bgColor;
        } else {
            event.lineBackground = ((Control)event.widget).getBackground();

        }
    }

}
