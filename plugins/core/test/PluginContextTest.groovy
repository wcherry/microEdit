import groovy.util.GroovyTestCase
import org.sss.micro.core.PluginContext
import org.sss.micro.core.KeyStroke
import org.eclipse.swt.SWT

class PluginContextTest extends GroovyTestCase {
	PluginContext context
	void setUp(){
		context = new PluginContext()

	}
	
	void testKeyMapContainsAddedAction(){
		def map =    ['CTRL+N': 'newAction']
		context.addToKeyMap(map)
		assert context.keyMap[new KeyStroke(SWT.CTRL, (int)'n')]
		assert !context.keyMap[new KeyStroke(SWT.CTRL+SWT.SHIFT, (int)'n')]
	}
	
	void testTryingToAddInvalidKeyMapEntry(){
		def map =    ['SHAFT+N': 'newAction']
		try{
			context.addToKeyMap(map)  // Doesn't fail until we try to get the map back
			context.getKeyMap()
			fail('Adding an invalid keymap entry should fail')
		}catch(Exception expected){}
	}
	
	void testContextContainsAddedBean(){
		def bean = new Object()
		context.addBean('foo', bean)
		def other = context.foo
		assert other != null
		assert other == bean
	}

	void testContextThrowsExceptionOnMissingBean(){
		def bean = new Object()
		try{
			def other = context.foo
			fail('Context should throw an exception when bean is not found')
		} catch(Exception expected){}
	}
	
	void testAddListenerToUnRegisiteredEvent(){
		try{
			context.addEventListener('foo'){println 'Argh...'}
			fail('Cannot add event listener to unregisited event')
		} catch(Exception expected){}
	}
}
