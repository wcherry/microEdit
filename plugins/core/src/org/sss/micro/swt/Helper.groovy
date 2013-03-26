package org.sss.micro.swt


import org.eclipse.swt.SWT
import org.eclipse.swt.events.KeyEvent

import org.sss.micro.core.KeyStroke

class Helper {
  static KeyStroke convertKeyStringToEvent(String keyString){
    int stateMask, keyCode = 0
    String[] parts = keyString.split('\\+') //TODO: This should be handled by a regex
    for(int i = 0; i < parts.size()-1; i++){
      String p =parts[i]
      if(p=='SHIFT'){stateMask |= SWT.SHIFT }
      else if(p=='CTRL') {stateMask |= SWT.CONTROL} 
      else if(p=='ALT'){stateMask |= SWT.ALT}
      else throw new IllegalArgumentException("Unknown key modifer ${parts[i]} for $keyString")
    }
    String p = parts[parts.size()-1]
    if(p=='{SPACE}') {keyCode = ' '}    //TODO: Make this table based, expandable ('{0xXXXX}') hex value of keystroke
    else if(p=='{{}') {keyCode = '{'}
    else if(p=='{TAB}') {keyCode = SWT.TAB}
    else if(p=='{ESC}') {keyCode = SWT.ESC}
    else if(p=='{F1}') {keyCode = SWT.F1}
    else if(p=='{F2}') {keyCode = SWT.F2}
    else if(p=='{F3}') {keyCode = SWT.F3}
    else if(p=='{F4}') {keyCode = SWT.F4}
    else if(p=='{F5}') {keyCode = SWT.F5}
    else if(p=='{F6}') {keyCode = SWT.F6}
    else if(p=='{F7}') {keyCode = SWT.F7}
    else if(p=='{F8}') {keyCode = SWT.F8}
    else if(p=='{F9}') {keyCode = SWT.F9}
    else if(p=='{F10}') {keyCode = SWT.F10}
    else if(p=='{F11}') {keyCode = SWT.F11}
    else if(p=='{F12}') {keyCode = SWT.F12}
    else if(p=='{PGUP}') {keyCode = SWT.PAGE_UP}
    else if(p=='{PGDN}') {keyCode = SWT.PAGE_DOWN}
    else if(p=='{HOME}') {keyCode = SWT.HOME}
    else if(p=='{END}') {keyCode = SWT.END}

    else keyCode = p.toCharacter().toLowerCase()

    return new KeyStroke(stateMask, keyCode)
  }

  static KeyStroke convertKeyEventToStroke(KeyEvent e){
    new KeyStroke(stateMask: e.stateMask, keyCode: e.keyCode)
  }

  static String convertKeyStrokeToString(KeyStroke ks){
      String out = ""
      switch(ks.keyCode){
          case SWT.TAB: out= "TAB"; break;
          case SWT.ESC: out= "ESC"; break;
          case SWT.F1:  out= "F1"; break;
          case SWT.F2:  out= "F2"; break;
          case SWT.F3:  out= "F3"; break;
          case SWT.F4:  out= "F4"; break;
          case SWT.F5:  out= "F5"; break;
          case SWT.F6:  out= "F6"; break;
          case SWT.F7:  out= "F7"; break;
          case SWT.F8:  out= "F8"; break;
          case SWT.F9:  out= "F9"; break;
          case SWT.F10: out= "F10"; break;
          case SWT.F11: out= "F11"; break;
          case SWT.F12: out= "F12"; break;
          case SWT.PAGE_UP:   out= "PGUP"; break;
          case SWT.PAGE_DOWN: out= "PGDN"; break;
          case SWT.HOME:      out= "HOME"; break;
          case SWT.END:       out= "END"; break;
          default:       out = new String(((char)ks.keyCode))
  }


      def s = ks.stateMask
      if (s & SWT.SHIFT){
          s -= SWT.SHIFT
          out +=" SHIFT"
      }
      if (s & SWT.ALT){
          s -= SWT.ALT
          out +=" ALT"
      }
      if (s & SWT.CTRL){
          s -= SWT.CTRL
          out +=" CTRL"
      }
      if (s>0){
          out += " $s"
      }
      return out
  }

}