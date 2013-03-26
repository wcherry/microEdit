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
package org.sss.micro.swt

import org.eclipse.jface.dialogs.InputDialog
import org.eclipse.jface.dialogs.MessageDialog
import org.sss.micro.core.PluginContext
import org.eclipse.swt.widgets.MessageBox
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Combo
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.Control
import org.eclipse.swt.events.SelectionListener
import org.eclipse.swt.events.SelectionEvent
/**
 * Created with IntelliJ IDEA.
 * User: wcherry
 * Date: 6/13/12
 * Time: 11:05 AM
 * To change this template use File | Settings | File Templates.
 */
class DialogHelper {
    public static String showInputDialog(PluginContext context, String title, String message){
        InputDialog dlg = new  InputDialog(context.appWindow.shell, title, message, null, null)
        dlg.open()
        return dlg.getValue()
    }
    public static void showMessageDialog(PluginContext context, String title, String message){
        String[] btns = ['Ok']
        MessageDialog dlg = new  MessageDialog(context.appWindow.shell, title, null, message, MessageDialog.INFORMATION, btns, 0)
        dlg.open()
    }

    public void alert(PluginContext context, String message, String title = "Alert") {
        def msgBox = new MessageBox(context.appWindow.shell, SWT.ICON_INFORMATION | SWT.OK)
        msgBox.message = message
        msgBox.text = title
        msgBox.open()
    }

    public static String showPickListDialog(PluginContext context, String title, String message, def options){
        def dlg =  new ComboDialog(context.appWindow.shell, title, message, options)
        dlg.open()
        return dlg.getSelection()
    }

    static class ComboDialog extends MessageDialog {
        def options = null
        String result = null;


        public ComboDialog(Shell parentShell, String dialogTitle, String dialogMessage, def options){
            super(parentShell, dialogTitle, null, dialogMessage, MessageDialog.QUESTION, ['Ok'], 0)
            this.options = options
        }

        protected  Control	createCustomArea(Composite parent){
            Combo c = new Combo(parent, 0)
            options.each{c.add(it) }

            c.addSelectionListener(new SelectionListener(){

                void widgetSelected(SelectionEvent selectionEvent) {
                    result = options[((Combo)selectionEvent.widget).getSelectionIndex()]
                }

                void widgetDefaultSelected(SelectionEvent selectionEvent) {}
            })
            return c
        }

        public String getSelection(){
            return result
        }

    }
}
