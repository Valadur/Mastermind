package com.andreasgrassl.common;

import java.io.*;
import java.util.ArrayList;

class FileReplace
{
    ArrayList<String> lines = new ArrayList<String>();
    String line = null;
    public void  doIt(ArrayList<String> results)
    {
        try
        {
            File f1 = new File("results.txt");
            if (f1.isFile()){
                FileReader fr = new FileReader(f1);
                BufferedReader br = new BufferedReader(fr);
                while ((line = br.readLine()) != null)
                {
                    if (line.contains("java"))
                       line = line.replace("java", " ");
                 lines.add(line);
                }
                fr.close();
                br.close();
            }
            FileWriter fw = new FileWriter(f1);
            BufferedWriter out = new BufferedWriter(fw);
            for(String s : lines)
                out.write(s + "\n");
            if(!results.isEmpty()){
                for(String s : results)
                    out.write(s + "\n");
            }
            out.write("--------------------------------\n");
            out.flush();
            out.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}