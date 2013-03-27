package org.sss.micro.textmate

import org.sss.micro.grammars.Snippet

//TODO: Cross package dependencies

class Helper {
    public static Map processBundleFile(File bundleFile) {
        try {
            def parser = new XmlSlurper()
            parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            parser.setFeature("http://xml.org/sax/features/namespaces", false)
            parser.setDTDHandler(new IgnoreDTDHandler())
            def xml = parser.parse(bundleFile)
            Map map = processDict(xml.dict)
            return map
        } catch (org.xml.sax.SAXParseException ex) {println ex}//TODO: Don't throw an exception just yet!
        return null;
    }

    public static Map processLanguageFile(File tmLangFile) {
        return processBundleFile(tmLangFile)
    }

    public static Snippet processSnippetFile(File tmSnippetFile) {
        try {
            Snippet snippet = new Snippet()
            def parser = new XmlSlurper(false, false)
            parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            parser.setFeature("http://xml.org/sax/features/namespaces", false)
            parser.setDTDHandler(new IgnoreDTDHandler())
            def xml = parser.parse(tmSnippetFile)
            String currentKey = null
            xml.dict.children().each {topLevel ->
                if ('key' == topLevel.name()) {
                    currentKey = topLevel.text()
                } else {
                    if ('string' == topLevel.name() && snippet.hasProperty(currentKey)) {
                        snippet."$currentKey" = topLevel.text()
                    }
                }
            }
            return snippet
        } catch (org.xml.sax.SAXParseException ex) {println ex}
        return null
    }









    public static Map processDict(dictEl) {
        Map map = [:]
        String key = null
        dictEl.children().each {topLevel ->
            if ('key' == topLevel.name()) {
                key = topLevel.text()
            } else if ('array' == topLevel.name()) {
                def array = processArray(topLevel)
                map[key] = array
            } else if ('dict' == topLevel.name()) {
                def dict = processDict(topLevel)
                map[key] = dict
            } else if ('string' == topLevel.name()) {
                map[key] = topLevel.text()
            } else println "${topLevel.name()} not found"
        }
        return map
    }

    public static List processArray(arrayEl) {
        //println arrayEl.children()
        List list = []
        arrayEl.children().each {topLevel ->
            if ('array' == topLevel.name()) {
                def array = processArray(topLevel)
                list << array
            } else if ('dict' == topLevel.name()) {
                def dict = processDict(topLevel)
                list << dict
            } else if ('string' == topLevel.name()) {
                list << topLevel.text()
            } else println "${topLevel.name()} not found"
        }
        return list
    }

}