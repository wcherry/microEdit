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

import org.eclipse.swt.SWT
import org.eclipse.swt.custom.LineStyleListener
import org.eclipse.swt.custom.StyledText
import org.eclipse.swt.events.KeyAdapter
import org.eclipse.swt.events.KeyEvent
import org.eclipse.swt.graphics.GC
import org.eclipse.swt.graphics.Rectangle
import org.eclipse.swt.widgets.Canvas
import org.sss.micro.swt.Helper
import org.eclipse.swt.graphics.Color

import java.util.regex.*
import org.sss.micro.swt.DialogHelper
import org.eclipse.jface.text.TextViewerUndoManager
import org.eclipse.swt.custom.LineBackgroundListener
import org.eclipse.jface.text.source.SourceViewer
import org.eclipse.jface.text.source.CompositeRuler
import org.eclipse.jface.text.source.AnnotationRulerColumn
import org.eclipse.jface.text.source.LineNumberRulerColumn
import org.eclipse.jface.text.source.AnnotationModel
import org.eclipse.jface.text.source.IAnnotationAccess
import org.eclipse.jface.text.source.IAnnotationAccessExtension

class SourceWindow implements Window {
    //SWT Controls
    private StyledText textArea = null
    public SourceViewer viewer
    private CompositeRuler compositeRuler
    public AnnotationRulerColumn annotationRuler
    private LineNumberRulerColumn lineNumbers
    private AnnotationModel fAnnotationModel = new AnnotationModel()
    private LineStyleListener foregroundStyler = null
    public LineBackgroundListener backgroundStyler = null
    private List lineStylers = []
    SearchMgr searchMgr = null

    protected undoMgr = null

    private Document doc = null
    String title = "Untitled"
    PluginContext context = null
    Map keyMap = [:]
    Map data = [:]

    void setBody(String s) {document.content = s}

    String getBody() {document.content}

    void setTitle(String t) {
        title = t
        if (title && data.tabItem) data.tabItem.setText(title)
    }

    int getCaretColumn() {
        int offset = textArea.getCaretOffset()
        int line = textArea.getLineAtOffset(offset)
        int start = textArea.getOffsetAtLine(line)
        offset - start
    }

    void redraw(){
        textArea.redraw()
    }


    int getCaretLine() {
        int offset = textArea.getCaretOffset()
        textArea.getLineAtOffset(offset)
    }

    String getLine(int line) {
        textArea.getLine(line)
    }

    void replaceText(int line, int start, int end, String replacement) {
        int posStart = textArea.getOffsetAtLine(line) + start
        int length = end - start
        textArea.replaceTextRange(posStart, length, replacement)
    }

    void cut() {
        textArea.cut()
    }

    void copy() {
        textArea.copy()
    }

    void paste() {
        textArea.paste()
    }

    void selectAll() {
        textArea.selectAll()
    }

    void undo() {
        if (undoMgr.undoable())
            undoMgr.undo()
        else DialogHelper.showMessageDialog(context, "Undo Action", "Nothing to Undo!!!")
    }

    void redo() {
        undoMgr.redo()
    }


    Document getDocument() {
        if (!doc) doc = new Document()
        return doc
    }

    void setDocument(Document document) {
        doc = document
        doc.nativeObject.addDocumentListener([
                documentAboutToBeChanged: {event ->},
                documentChanged: {event ->
                    if (setModified(true))
                    //if(data.tabItem)
                        data?.tabItem?.setText(title + "*")
                }
        ] as org.eclipse.jface.text.IDocumentListener)

    }

    void addLineStyler(LineStyleListener aLineStyler) {
        if(textArea){
            textArea.removeLineStyleListener(aLineStyler)
            textArea.addLineStyleListener(aLineStyler)
        } else {
            lineStylers.add(aLineStyler)
        }
    }

    void show(WindowContainer windowContainer, PluginContext context) {

        compositeRuler = new CompositeRuler();
        lineNumbers = new LineNumberRulerColumn();
        lineNumbers.setBackground(context.display.getSystemColor(SWT.COLOR_GRAY));  //FORNOW: native call across components
        compositeRuler.addDecorator(0, lineNumbers);

        viewer = new SourceViewer(windowContainer.getNativeControl(), compositeRuler, SWT.FULL_SELECTION | SWT.HORIZONTAL | SWT.VERTICAL);
        viewer.setDocument(document.nativeObject);

        textArea = viewer.textWidget

        searchMgr = new SearchMgr(context, this)
        undoMgr = new TextViewerUndoManager(99)

        viewer.setUndoManager(undoMgr)
        undoMgr.connect(viewer)

        if(context.appWindow.defaultFont) textArea.setFont(context.appWindow.defaultFont)
        if(context.appWindow.defaultColor) textArea.setForeground(context.appWindow.defaultColor)

        textArea.setData('plaintextwindow', this)
        if(foregroundStyler) addLineStyler(foregroundStyler)

        lineStylers.each{addLineStyler(it)}


        //FORNOW: Disable the background highlighter for now
        //backgroundStyler = new LineHighlighterStyler(context.appWindow.colors['light_yellow'])
        //textArea.addLineBackgroundListener(backgroundStyler)

        textArea.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                KeyStroke ke = Helper.convertKeyEventToStroke(e)

                String actionName = keyMap[ke]
                if (actionName) {
                    println "KeyStroke $ke produced action $actionName"
                    context.callAction(actionName)
                }
            }
        })
		// Remove key bindings for keys that we are mapping.
		keyMap.each{k,v-> textArea.setKeyBinding(k.bindingCode, SWT.NULL)}
    }

    public Object getNativeControl() {
        return viewer.control
    }


    private boolean setModified(boolean modified) {
        if (doc.modified == modified) return false
        doc.modified = modified
        return true
    }

    def propertyMissing(key) {
        data[key]
    }

    def propertyMissing(key, value) {
        data[key] = value
    }
}