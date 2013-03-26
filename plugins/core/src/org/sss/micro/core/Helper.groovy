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
package org.sss.micro.core

class Helper {

    public static void prettyPrintStackTrace(Throwable ex, PrintStream out = System.out){
        out.println "================ ERROR ========================"
        out.println "${ex.getClass().getName()}: ${ex.message}"
        ex.stackTrace.each{
            if(it.className.startsWith('org.codehaus.groovy')
                    || it.className.startsWith('groovy.lang')
                    || it.className.startsWith('java.lang')
                    || it.className.startsWith('sun')){} else out.println "** $it"
        }

    }
}
