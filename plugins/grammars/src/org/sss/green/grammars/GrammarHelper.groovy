package org.sss.micro.grammars

import org.sss.micro.textmate.Helper

/**
 * Created with IntelliJ IDEA.
 * User: wcherry
 * Date: 6/8/12
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */
class GrammarHelper {
    static Grammar createGrammarFromBundleFile(File file){
        Grammar g = new Grammar()
        Map map = Helper.processBundleFile(file)
        map.each {k,v-> if(g.hasProperty(k)) g."$k" = v}
        return g
    }

    static Grammar updateGrammarFromMap(Map map, Grammar g){
        map.each {k,v-> k = k.replaceAll("\\-", "_"); if(g.hasProperty(k)) g."$k" = v else println "Property $k not found in grammar"}
        return g
    }

    static Snippet createSnippetFromBundleFile(File file){
        Snippet g = new Snippet()
        Map map = Helper.processBundleFile(file)
        map.each {k,v-> if(g.hasProperty(k)) g."$k" = v}
        return g
    }

    static Command createCommandFromBundleFile(File file){
        Command g = new Command()
        Map map = Helper.processBundleFile(file)
        map.each {k,v-> if(g.hasProperty(k)) g."$k" = v}
        return g
    }

    static int findLast(String s, int end, tokens){
        int pos = -1
        tokens.each{
            pos = Math.max(s.lastIndexOf(it, end), pos)
        }
        return pos
    }


}
