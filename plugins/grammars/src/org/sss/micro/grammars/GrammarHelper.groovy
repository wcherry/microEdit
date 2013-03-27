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
package org.sss.micro.grammars

import org.sss.micro.textmate.Helper

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
