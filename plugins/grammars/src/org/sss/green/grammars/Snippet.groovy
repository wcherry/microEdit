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