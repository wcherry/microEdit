package org.sss.micro.swt

import org.sss.micro.core.PluginContext
import org.sss.micro.core.ManagedThread

import org.eclipse.swt.SWT
import org.eclipse.swt.events.MouseAdapter
import org.eclipse.swt.events.MouseEvent
import org.eclipse.swt.events.TreeEvent
import org.eclipse.swt.events.TreeAdapter
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.widgets.Tree
import org.eclipse.swt.widgets.TreeItem

class FileViewer {
    static final String FULL_FILENAME_KEY = '_filename'

	DockingWindow win	= null
	Tree tree 						= null
	String directory 			= null
	String topTree				= null

	FileViewer(PluginContext context, String dir){
		directory = dir
		win = new DockingWindow(context: context, title: 'File System Browser', setup: {
			tree = new Tree(it.shell, SWT.BORDER)
            tree.addMouseListener(new MouseAdapter() {
                void mouseDoubleClick(MouseEvent mouseEvent) {
                    TreeItem item = tree.getItem(new Point(mouseEvent.x, mouseEvent.y))
					if(item == null || item.getItems().length > 0) return
                    String fullName = item.getData(FULL_FILENAME_KEY)
                    if (fullName) {
                        println "Loading $fullName"
                        context.Filesystem.loadFile(fullName)
                    }

                }
            })

			tree.addTreeListener(new TreeAdapter(){
				void treeExpanded(TreeEvent e){
					directory = e.item.getData(FULL_FILENAME_KEY)
					def item = e.item.getItems()[0]
					if(item.getData()!=null) return
					item.dispose()
					
					scanDirectory(e.item.getData(FULL_FILENAME_KEY), e.item)
				}
			})


			topTree = System.properties.getProperty("user.home")
			scanDirectory(new File(topTree), tree) //TODO: this should be saved across opening
		})
	}

	

	protected scanDirectory(File dir,def tree){
//		def directories = []
		dir.eachFile{file->
			if(file.name!='.' && file.name!='..'){
				TreeItem item = new TreeItem(tree, 0)
				item.text = file.name
				// if(file.isFile()) 
				item.setData(FULL_FILENAME_KEY, file)
				if(file.isDirectory()) new TreeItem(item, 0)
			}
		}
	//	new ManagedThread('File Browser', {scanDirectory(directories)}).start()
	}
		
	protected scanDirectory(directories){
        def moreDir = []
		directories.each{entry->
			def dir = entry[0]
			def tree = entry[1]
		
			dir.eachFile{file->
				if(file.name!='.' && file.name!='..'){
					TreeItem item = new TreeItem(tree, 0)
					item.text = file.name
					if(file.isFile()) item.setData(FULL_FILENAME_KEY, file)
					if(file.isDirectory()) moreDir << [ file, item]
				}
			}
		}
		if(!moreDir.isEmpty()) new ManagedThread('File Browser', {scanDirectory(moreDir)}).start()
	}

	void expandTree(){
		String dir = directory - topTree
		println "Starting in directory $dir"
		String[] sub = dir.split("\\"+java.io.File.separatorChar)
		def subTree = tree
		sub[1..-1].each{name->
			def item = findTreeItem(subTree, name)
			if(item) {
				tree.showItem(item)
				tree.setSelection(item) 
				subTree = item 

				scanDirectory( item.getData(FULL_FILENAME_KEY),subTree)
			}
		}
	}

	def findTreeItem(def tree, String name){
		if(!tree) return null
		TreeItem item = tree.getItems().find{item->item.text == name }
		return item
	}

	void toggle(){
		if (win.visible) win.hide()
        else win.show()
	}

    FileViewer show(){
        if (!win.visible) win.show()
		expandTree()
        return this
    }

}