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

import org.eclipse.swt.graphics.Color
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.widgets.Menu
import org.eclipse.swt.custom.CBanner
import org.eclipse.swt.widgets.ToolBar
import org.eclipse.swt.graphics.RGB
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.MenuItem
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets.FontDialog
import org.eclipse.swt.graphics.FontData
import org.eclipse.swt.graphics.Font

/**
 * Main application window. Contains the window container, menu bar, etc...
 * @author  wcherry
 * @since   1.0
 * @version 1.0
 */
class AppWindow {
    // SWT Classes
    Display display             = null
    Shell shell                 = null
    Menu mainMenu               = null

    PluginContext context       = null
    Map colors                  = [:]

    Font defaultFont = null
    Color defaultColor = null

    WindowContainer windowContainer = null
    DragAndDropSupport dnd      = null

    protected void initColors() {
        // Some basic colors
        colors['black'] = new Color(display, new RGB(0, 0, 0))
        colors['red'] = new Color(display, new RGB(255, 0, 0))
        colors['green'] = new Color(display, new RGB(0, 255, 0))
        colors['light_green'] = new Color(display, new RGB(196, 255, 196))
        colors['blue'] = new Color(display, new RGB(0, 0, 255))
        colors['white'] = new Color(display, new RGB(255, 255, 255))
        colors['light_gray'] = new Color(display, new RGB(196, 196, 196))
        colors['dark_gray'] = new Color(display, new RGB(64, 64, 64))
        colors['yellow'] = new Color(display, new RGB(255, 255, 0))
        colors['light_yellow'] = new Color(display, new RGB(255, 255, 228))
        colors['light_goldenrod_yellow'] = new Color(display, new RGB(250, 250, 210))

        // Colors used by various effects
        //colors['selected_background'] = colors['light_goldenrod_yellow']
        colors['highlight_background'] = colors['light_goldenrod_yellow']
        colors['highlight_foreground'] = colors['red']
    }

    private Image getImageFor(String cmd) {
        return new Image(display, org.sss.micro.bootstrap.Application.properties.getProperty("home.dir") + "res\\icons\\" + cmd + ".png")
    }

    def removeMenu(String menuName, List actions){
        MenuItem topMenuItem = mainMenu.items.find {it.text.equals(menuName)}
        actions.each{action->  topMenuItem.items.remove(action.name)}
    }

    def createMenu(String menuName, List<Action> actions) {
        MenuItem topMenuItem = mainMenu.items.find {it.text.equals(menuName)}
        Menu subMenuItem
        if (!topMenuItem) {
            topMenuItem = new MenuItem(mainMenu, SWT.CASCADE)
            topMenuItem.setText(menuName)
            subMenuItem = new Menu(shell, SWT.DROP_DOWN)
            topMenuItem.setMenu(subMenuItem)
        } else {
            subMenuItem = topMenuItem.getMenu()
        }
        def x = actions.sort {a, b -> a.order <=> b.order}
        x.each {action ->
            def actionName = action.name
            if (action) {
                MenuItem menuItem = new MenuItem(subMenuItem, SWT.NULL)
                String name = context.resources.get("menu.${actionName}.label".toString())
                if(!name) name = action.name
                println "MENU: $actionName = $name"
                menuItem.setText(name)
                menuItem.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent event) {
                        try{
                            action.action.call()
                        } catch(Exception ex){
                            Helper.prettyPrintStackTrace(ex)
                        }
                    }
                })
            } else println "Unable to find registered action $actionName"
        }
    }

    //public AppWindow() {}

    public AppWindow(String aTitle, context) {
        this.context = context
        // Create the native window
        display = new Display()
        context.display = display
        shell = new Shell(display)

        context.shell = shell
        shell.setText(aTitle)
        shell.setSize(800, 500)
        shell.setLayout(new FillLayout())  //TODO: Change layout type to support multiple widgets
        mainMenu = new Menu(shell, SWT.BAR)
        shell.setMenuBar(mainMenu)

        windowContainer = new TabbedWindowContainer(this)

        //TODO: Add DND Support here!
        dnd = new DragAndDropSupport()
        dnd.addDragAndDropSupport(context, windowContainer.nativeControl);

        context.windowContainer = windowContainer
        initColors()
    }


    public Window addWindow(Window w) {
        windowContainer.addWindow(w)
        w.context = context //FORNOW: Make sure that plain text windows have a context
        return w
    }

	boolean closeWindow(Window w){
		windowContainer.closeWindow(w)
	}

    public Window getFocusedWindow() {
        windowContainer.getFocusedWindow()
    }

    public void setFocusedWindow(int index) {
        windowContainer.setFocusedWindow(index)
    }

    public void changeFont(){
        FontDialog fd = new FontDialog(shell, SWT.NONE)
        fd.setText("Select Font")
        fd.setRGB(defaultColor?.getRGB()?:new RGB(0,0,0))
        FontData fontData = null
        if(defaultFont)
            fontData = defaultFont.getFontData()[0]
        else fontData =  new FontData("Courier",10,SWT.BOLD)
        fd.setFontData(fontData)
        FontData newFont = fd.open()
        if(newFont==null)
            return
        defaultFont = new Font(display, newFont)
        defaultColor = new Color(display, fd.getRGB())

        windowContainer.windows.each{
            it.textArea.setFont(defaultFont)
            it.textArea.setForeground(defaultColor)
        }
    }


    public void start() {
        shell.open()
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep()
        }
        display.dispose()
    }

    public Shell getNativeShell() {return shell}
}