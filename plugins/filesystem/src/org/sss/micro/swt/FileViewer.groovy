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
			new TreeItem(tree, 0)

			topTree = System.properties.getProperty("user.home")
			scanDirectory(new File(topTree), tree) //TODO: this should be saved across opening
// 		expandTree()


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
					scanDirectory(e.item.getData(FULL_FILENAME_KEY), e.item)
				}
			})
		})
	}

	

	protected scanDirectory(File dir,def tree){
		TreeItem item = null
		if(tree.itemCount>0 && tree.getItem(0).getData(FULL_FILENAME_KEY)==null){
			tree.getItem(0).dispose() // dispose the dummy node

			println "Scanning directory ${dir.name} for changes"
			dir.eachFile{file->
				if(file.name!='.' && file.name!='..'){
					item = new TreeItem(tree, 0)
					item.text = file.name
					item.setData(FULL_FILENAME_KEY, file)
					if(file.isDirectory()) new TreeItem(item, 0)	// add dummy node so that this sub-tree is expandable
				}
			}
		}
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
				subTree = item 
				scanDirectory( item.getData(FULL_FILENAME_KEY),subTree)
			} else { println "Not able to find sub tree in file browser for '$name'"}
		}
	}

	def findTreeItem(def tree, String name){
		if(!tree || tree.isDisposed()) {println "Tree already disposed or null"; return null}
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