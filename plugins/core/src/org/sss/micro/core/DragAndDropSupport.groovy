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

import org.eclipse.swt.dnd.DropTarget
import org.eclipse.swt.dnd.DND
import org.eclipse.swt.dnd.TextTransfer
import org.eclipse.swt.dnd.Transfer
import org.eclipse.swt.widgets.Control
import org.eclipse.swt.dnd.DropTargetListener
import org.eclipse.swt.dnd.DropTargetEvent

class DragAndDropSupport {
    DropTarget target = null
    List dataTransfers = []

    public void addDragAndDropSupport(PluginContext context, Control control) {
        makeDropable(control)
        addTransferType(new DataTransfer(transferType: TextTransfer.getInstance(), supportedOperations: DND.DROP_COPY, onDrop: {event ->
            String text = (String) event.data;
            context.appWindow.focusedWindow.document.appendText(text);
        }))
    }

    void addTransferType(DataTransfer type) {
        dataTransfers.add(type)
//		def types = new Transfer[dataTransfers.size()]
        def types = dataTransfers.collect {it.transferType}
        target.setTransfer(types.toArray(new Transfer[0]))
    }

    private void makeDropable(Control dropControl) {
        def operations = DND.DROP_COPY | DND.DROP_DEFAULT;
        target = new DropTarget(dropControl, operations);
        target.addDropListener(new DropTargetListener() {
            public void dragEnter(DropTargetEvent event) {
                if (event.detail == DND.DROP_DEFAULT) {
                    if ((event.operations & DND.DROP_COPY) != 0) {
                        event.detail = DND.DROP_COPY;
                    } else {
                        event.detail = DND.DROP_NONE;
                    }
                }

            }

            public void dragOver(DropTargetEvent event) {
                event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
            }

            public void dragOperationChanged(DropTargetEvent event) {
                if (event.detail == DND.DROP_DEFAULT) {
                    if ((event.operations & DND.DROP_COPY) != 0) {
                        event.detail = DND.DROP_COPY;
                    } else {
                        event.detail = DND.DROP_NONE;
                    }
                }
            }

            public void drop(DropTargetEvent event) {
                dataTransfers.each {transfer ->
                    if (transfer.isSupportedType(event.currentDataType)) {
                        transfer.onDrop.call(event)
                    }
                }
            }

            public void dragLeave(DropTargetEvent event) {}

            public void dropAccept(DropTargetEvent event) {}

        })
    }
}
