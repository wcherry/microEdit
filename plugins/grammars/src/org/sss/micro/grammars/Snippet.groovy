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

class Snippet {
  String content
  String name
  String scope
  String tabTrigger
  String keyEquivalent
  String uuid

    public String toString(){return name}

    def getLabels(){
        Map labels = [:]

        int n = 0
        content.eachLine{t->
            n++
            def m = t =~ /\$\{(\d):(.*?)\}/
            if(m){
                for(int i = 0; i < m.count; i++){
                    String org = m[i][0]
                    String number = m[i][1]
                    String defValue = m[i][2]
                    labels[number] = [defValue, org, n, m.start(0), m.end(0)]
                }
            }
        }

        return labels
    }

    def getSimpleString(){
        def labels = getLabels()
        StringBuilder b = new StringBuilder()

        content.eachLine{t->
            labels.each {k, g-> t = t.replace(g[1], g[0]) }
            b.append(t).append('\n')
        }
        b.toString()
    }
}