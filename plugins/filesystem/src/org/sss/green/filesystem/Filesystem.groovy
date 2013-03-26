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
package org.sss.micro.filesystem

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.FileDialog
import org.eclipse.swt.dnd.FileTransfer
import org.eclipse.swt.dnd.DND
import org.sss.micro.core.Window
import org.sss.micro.core.Document
import org.sss.micro.core.SourceWindow
import org.sss.micro.core.PluginContext
import org.sss.micro.core.DataTransfer
import org.sss.micro.swt.FileViewer


class Filesystem {
    def context = null;
//    List registeredFileExtensions = []
    FileViewer fileBrowser = null;

    String openFile() {
        println("Open File")
        def shell = context.appWindow.getNativeShell()
        FileDialog fd = new FileDialog(shell, SWT.OPEN)
        fd.setText("Open File")
        String path = context.filesystemLastPath
        if (path) fd.setFilterPath(path)
        String[] filterExt = context.registeredFileExeteions
        fd.setFilterExtensions(filterExt)
        String filename = fd.open()
        if (filename) {
            Window window = loadFile(filename)
        }
        return filename
    }

    Window loadFile(String filename) {
        context.fireEvent('documentAboutToLoad', [filename: filename])
        File file = new File(filename)
        context.filesystemLastPath = file.parent
        Document document = new FileBackedDocument(file: file)
        Window window = new SourceWindow(document: document)
        document.load()
        window.title = shortName(filename)
        context.appWindow.addWindow(window)
        context.fireEvent('documentLoaded', [window: window, filename: filename])
        return window
    }

    boolean saveFile() {
        println("Save File")
        Window window = context.appWindow.focusedWindow
        Document doc = window.document
        println "Saving file ${doc.file}"
        if (doc instanceof FileBackedDocument && doc.file) {
            context.fireEvent('documentAboutToSave', [window: window, document: doc, filename: doc.file.name])
            doc.save()
            context.fireEvent('documentSaved', [window: window, document: doc, filename: doc.file.name])
        } else saveFileAs()
    }

    boolean saveFileAs() {
        println("Save File As")
        def shell = context.appWindow.getNativeShell()
        FileDialog fd = new FileDialog(shell, SWT.SAVE)
        fd.setText("Save File As")
        String path = context.filesystemLastPath
        if (path) fd.setFilterPath(path)
        String[] filterExt = ["*.groovy", "*.gvy", ".txt", "*.*"]
        fd.setFilterExtensions(filterExt)
        String filename = fd.open()
        if (filename) {
            def file = new File(filename)
            context.filesystemLastPath = file.parent

            Window window = context.appWindow.focusedWindow
            Document doc = window.document
            if (!(doc instanceof FileBackedDocument)) doc = new FileBackedDocument(doc)
            window.document = doc
            context.fireEvent('documentAboutToSave', [window: window, document: doc, filename: file.name])
            doc.file = file
            doc.save()
            window.title = shortName(filename)
            context.fireEvent('documentSaved', [window: window, document: doc, filename: doc.file.name])
        }
    }

    boolean toggleFileViewer(){
        if (!fileBrowser)
            fileBrowser = new org.sss.micro.swt.FileViewer(context).show()
        else {
            fileBrowser.toggle()
        }
    }

    def load(PluginContext context) {
        println "Loading Filesystem plugin"
        this.context = context

        context.registerEvent('documentAboutToLoad')
        context.registerEvent('documentLoaded')

        context.registerEvent('documentAboutToSave')
        context.registerEvent('documentSaved')

        context.registerEvent('filesystemDirectoryChanged')
        context.registerEvent('filesystemDirectoryUpdated')


        context.addTransferType(new DataTransfer(transferType: FileTransfer.getInstance(), supportedOperations: DND.DROP_COPY, onDrop: {event ->
            String[] files = (String[]) event.data;
            files.each {filename ->
                loadFile(filename)
            }
        }))

    }

    def start(PluginContext context) {
        println "Starting Filesystem plugin"
        //context.grammars.each{name, grammar->registeredFileExtensions << grammar.registeredFileExtensions}
        //registeredFileExtensions = registeredFileExtensions.flatten()

        println "Completed starting Filesystem plugin"

    }

    def startMain(PluginContext context) {}

    def stop(PluginContext context) {}

    def unload(PluginContext context) {}

    String shortName(String longname) {
        int pos = longname.lastIndexOf(File.separator)
        if (pos >= 0) {
            return longname.substring(pos + 1)
        }
        return longname
    }
}