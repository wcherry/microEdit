package org.sss.micro.swt

import org.sss.micro.core.*
import org.eclipse.swt.browser.Browser
import org.eclipse.swt.SWT
import org.eclipse.swt.custom.LineStyleListener
import org.eclipse.swt.events.KeyAdapter
import org.eclipse.swt.events.KeyEvent

class HtmlWindow implements Window {
    private Browser browser = null
    String title = "Google"
    private URL url
    private data = [:]
    private KeyStroke  refreshKey = new KeyStroke(0, 16777230)

    void addLineStyler(LineStyleListener aLineStyler){}

    void setUrl(URL aUrl) {url = aUrl; if (browser) browser.url = url}

    String getBody() {throw new UnsupportedOperationException('getBody is not supported for org.sss.micro.swt.HtmlWindow')}

    void setBody(String s) {throw new UnsupportedOperationException('setBody is not supported for org.sss.micro.swt.HtmlWindow')}

    def getNativeControl() {browser}

    Document getDocument() {return null}

    void show(WindowContainer windowContainer, PluginContext context) {
        browser = new Browser(windowContainer.getNativeControl(), SWT.NONE);
        browser.url = url
        browser.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                println e
                KeyStroke ke = Helper.convertKeyEventToStroke(e)
                if(ke == refreshKey){
                    browser.refresh()
                }
        }
        })
    }

    void setKeyMap(o) {}

    void setTitleChangeEventListener(e) {}

    def propertyMissing(key) {
        data[key]
    }

    def propertyMissing(key, value) {
        data[key] = value
    }


}