/*
* The MIT License
*
* Copyright (c) 2013 Eugene Wang (euhome.github.io)
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*
* */
package gen;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

public class IPpack {
	
	Map<IPref, TreeSet<CIDR>> ipMap;	
	//TODO sortout the Pattern Usage
	//private static Pattern ipPattern = Pattern.compile("(\d+\.\d+\.\d+\.\d+)");
	
	public IPpack (){
		ipMap = new HashMap<IPref, TreeSet<CIDR>> ();	
	}
		
	
	public void process(String buffered) {
		CIDR cidr = new CIDR();
		IPref ref = null; 
		//Matcher m = ipPattern.matcher(buffered);
		
		//split into ip field and reference field
		String twoparts[] = buffered.split("\\(");
		//System.out.println(twoparts.length);
		
		if (twoparts.length != 2) {
			return;
		}
		
		//Handling IPs: split init and end ip
		String[]ips = twoparts[0].split("-");
		for (int i = 0; i < ips.length; i++ ){
			ips[i] = ips[i].trim();
		}
		
		//Handling IPs		
		try {
			cidr = new CIDR(ips[0], 24);
		} catch (Exception e){}
		
		
		twoparts[1] = twoparts[1].replace (")", "" ).trim();
		//System.out.println(twoparts[1]);
		String[]strRef = twoparts[1].split(",");
		for (int i = 0; i < strRef.length; i++ ){
			strRef[i] = strRef[i].trim();
			//System.out.print(strRef[i]+"|");
		}
		
		if (strRef.length == 3) {
			ref = new IPref (strRef[0],strRef[1],strRef[2]);
		} else if (strRef.length == 2){
			ref = new IPref (strRef[0],strRef[1]);
		}
		
		
		
		if (cidr.exist && ref != null){
			this.add(ref, cidr);
		}
		
		
		
	}
	
	public boolean write (PrintWriter w) throws Exception {
		Iterator<Entry<IPref, TreeSet<CIDR>>> it = ipMap.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<IPref, TreeSet<CIDR>> pair = (Map.Entry<IPref, TreeSet<CIDR>>)it.next();
			
			//getting the ref Object
			IPref ref = ((IPref)pair.getKey());
			
			
			
			//printing each items
			TreeSet<CIDR> set = (TreeSet<CIDR>)pair.getValue();
			for (CIDR ci : set){
				String YoloBuff = "\t"+"'" + ci + "', ";				
				w.println(YoloBuff);
				w.print(ref.itemReturn());
			}
			w.flush();
			//System.out.println();
		}
		return true;		
	}
	
	private void add (IPref ref, CIDR ip) {
		TreeSet<CIDR> set; 
		set = (ipMap.get(ref) == null)? new TreeSet<CIDR> (): ipMap.get(ref);
		set.add(ip);
		ipMap.put(ref, set);
	}

	
}
