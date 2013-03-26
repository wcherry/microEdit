/*import spock.lang.*
import org.eclipse.swt.SWT

import org.sss.micro.core.KeyStroke
import org.sss.micro.swt.Helper

class SwtHelperSpec extends Specification {
  def "key code and key strings"(){
	  when:
	    def swt_ks = new KeyStroke(stateMask:  SWT.CTRL, keyCode:  's')
	  and:
	    def ks = Helper.convertKeyStringToEvent("CTRL+S")
	  then:
	    ks == swt_ks
	    ks.hashCode() == swt_ks.hashCode()

  }
	
	def "do simple key strings work?"() {
    expect:
      def ks = Helper.convertKeyStringToEvent(string)
      ks.keyCode == code
      ks.stateMask == state

    where:
    string      | state | code
      'CTRL+S'    | SWT.CTRL | 's'
      'CTRL+X'    | SWT.CTRL | 'x'
      'ALT+S'     | SWT.ALT | 's'
      'ALT+X'     | SWT.ALT | 'x'
      'SHIFT+S'   | SWT.SHIFT | 's'
      'SHIFT+X'   | SWT.SHIFT | 'x'
  }

    def "do single key strings work?"() {
      expect:
        def ks = Helper.convertKeyStringToEvent(string)
        ks.keyCode == code
        ks.stateMask == state

      where:
      string      | state | code
        '{TAB}'     | 000000 | SWT.TAB
        '{ESC}'     | 000000 | SWT.ESC
        '{HOME}'     | 000000 | SWT.HOME
        '{PGUP}'     | 000000 | SWT.PAGE_UP
        '{F1}'     | 000000 | SWT.F1
        '{SPACE}'     | 000000 | ' '
    }

    def "do complex key strings work?"() {
        expect:
        def ks = Helper.convertKeyStringToEvent(string)
        ks.keyCode == code
        ks.stateMask == state

        where:
        string      | state | code
        'CTRL+SHIFT+S'    | SWT.CTRL+SWT.SHIFT | 's'
        'CTRL+ALT+X'    | SWT.CTRL+SWT.ALT | 'x'
        'ALT+CTRL+S'     | SWT.ALT+SWT.CTRL | 's'
        'ALT+SHIFT+X'     | SWT.ALT+SWT.SHIFT | 'x'
        'SHIFT+ALT+S'   | SWT.SHIFT+SWT.ALT | 's'
        'SHIFT+CTRL+X'   | SWT.SHIFT+SWT.CTRL | 'x'
        'ALT+CTRL+SHIFT+X'   | SWT.ALT+SWT.CTRL+SWT.SHIFT | 'x'
    }
}*/