/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author Do$$e
 */
public class CSVToolkit {
    Vector<Vector<String>> o=new Vector<Vector<String>>();
    public CSVToolkit(String filename) throws IOException{
        //System.out.println("Converting CSV: " + Thread.currentThread().getContextClassLoader().getResource(filename).getFile());
        FileReader fr = null;
        if (filename.startsWith("/")) {
                filename = filename.substring(1);
            }
            int index;
            // path = path.replaceAll("\\", "/");
            while ((index = filename.indexOf("\\")) != -1) {
                filename = filename.substring(0, index) + '/' + filename.substring(index + 1);
            }
        InputStreamReader isr=new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(filename));
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, ",");
            Vector<String> v = new Vector();
            while (st.hasMoreTokens()) {
                v.add(st.nextToken());
            }
            if (v.size() == 0) {
                continue;
            }
            o.addElement(v);
        }
    }
    public Vector<Vector<String>> getOutput(){
        return o;
    }
}
