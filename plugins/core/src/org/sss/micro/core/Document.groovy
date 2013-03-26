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

class Document {
    //SWT Controls
    protected org.eclipse.jface.text.Document nativeDocument = null

    boolean modified = false
    Map properties = [:]

    Document(org.eclipse.jface.text.Document swtDocument) {
        nativeDocument = swtDocument
    }

    Document(String body = null) {
        nativeDocument = new org.eclipse.jface.text.Document()
        if (body) nativeDocument.set(body)
    }

    void appendText(String text) {
        int pos = nativeDocument.getLength()
        nativeDocument.replace(pos, 0, text, new Date().getDate())
    }


    String getContent() {
        nativeDocument.get()
    }

    void setContent(String body) {
        nativeDocument.set(body)
    }

    org.eclipse.jface.text.Document getNativeObject() {
        nativeDocument
    }

    def propertyMissing(name) {
        properties[name]
    }

    def propertyMissing(name, value) {
        properties[name] = value
    }
}