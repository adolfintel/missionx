/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import org.easyway.utils.Utility;

/**
 *
 * @author Administrator
 */
public class GlobalVars {

    static HashMap<String, Double> variables = new HashMap<String, Double>();
    static String thisFile = "defGlobalVariables.vars";

    public static void setVariable(String name, double value) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (variables.get(name) != null) {
            variables.remove(name);
        }
        variables.put(name, value);
    }

    public static double parseText(String text) {
        double outValue = 0;

        try {
            outValue = Double.parseDouble(text);
        } catch (NumberFormatException ex) {
            if (text.compareToIgnoreCase("true") == 0) {
                outValue = 1.0d;
            } else if (text.compareToIgnoreCase("false") == 0) {
                outValue = 0.0d;
            } else {
                outValue = GlobalVars.getVariable(text);
            }
        }
        return outValue;
    }

    public static double getVariable(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (variables.get(name) != null) {
            return variables.get(name);
        }
        variables.put(name, 0.0d);
        return 0.0d;
    //throw new RuntimeException("Variable: '" + name + "' not found");
    }

    public static synchronized void saveOnFile(String filename) throws IOException {
        //System.out.println("saving on file..");
        FileOutputStream outputFile = null;
        {
            if (filename.startsWith("/")) {
                filename = filename.substring(1);
            }
            int index;
            // path = path.replaceAll("\\", "/");
            while ((index = filename.indexOf("\\")) != -1) {
                filename = filename.substring(0, index) + '/' + filename.substring(index + 1);
            }
            try {
                outputFile = new FileOutputStream(Thread.currentThread().getContextClassLoader().getResource(filename).getFile());
            } catch (Exception e) {
                Utility.error("the file " + filename + " was not found!", e);
            }
        }

        // save data
        ObjectOutputStream outStream = new ObjectOutputStream(outputFile);
        outStream.writeObject(variables);

        thisFile = filename;
    }

    public static synchronized void loadFromFile(String filename) throws IOException {
        //System.out.println("loading fromfile..");
        InputStream inputFile = null;
        {
            if (filename.startsWith("/")) {
                filename = filename.substring(1);
            }
            int index;
            // path = path.replaceAll("\\", "/");
            while ((index = filename.indexOf("\\")) != -1) {
                filename = filename.substring(0, index) + '/' + filename.substring(index + 1);
            }
            try {
                inputFile = Thread.currentThread().getContextClassLoader().getResource(filename).openStream();
            } catch (Exception e) {
                Utility.error("the file " + filename + " was not found!", e);
            }
        }

        ObjectInputStream inputStream = new ObjectInputStream(inputFile);
        try {
            variables = (HashMap<String, Double>) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            Utility.error("GlobalVars.loadFromFile(String)", ex);
        }

        thisFile = filename;
    }

    @Override
    protected void finalize() throws Throwable {
        if (thisFile != null) {
            saveOnFile(thisFile);
        }
        super.finalize();
    }
}
