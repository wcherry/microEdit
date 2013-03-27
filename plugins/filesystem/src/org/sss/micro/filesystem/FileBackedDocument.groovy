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

import org.sss.micro.core.Document

class FileBackedDocument  extends Document{
  File file = null

  FileBackedDocument(Map map){
    map?.each{k,v->
      if(this.hasProperty(k)) this[k] = v
    }
  }

  FileBackedDocument(Document doc){
    super(doc.nativeDocument)
    modified = doc.modified 
  }

  void load(){
    this.content = file.text
  }
  void save(){
    file.text = this.content
    this.modified = false
  }
}