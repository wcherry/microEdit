package org.sss.micro.textmate;

import org.xml.sax.SAXException;

/**
 * Created with IntelliJ IDEA.
 * User: wcherry
 * Date: 6/6/12
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class IgnoreDTDHandler implements org.xml.sax.DTDHandler{
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
