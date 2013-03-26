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