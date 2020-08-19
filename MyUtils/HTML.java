package MyUtils;

import java.util.Scanner;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class HTML {
    // Code Shamelessly taken from webpage: https://www.tutorialspoint.com/how-to-read-the-contents-of-a-webpage-into-a-string-in-java
    
    public static String html_to_string(String my_url) {
        //Instantiating the URL class.
        try {
            // Fixes 403 error
            System.setProperty("http.agent", "Chrome");
            URL url = new URL(my_url);
            //Retrieving the contents of the specified page
            Scanner sc = new Scanner(url.openStream());
            //Instantiating the StringBuffer class to hold the result
            StringBuffer sb = new StringBuffer();
            while(sc.hasNext()) {
                sb.append(sc.next());
            }
            sc.close();
            //Retrieving the String from the String Buffer object
            String result = sb.toString();
            //Removing the HTML tags
            result = result.replaceAll("<[^>]*>", "");
            // Return raw data
            return result;
        } catch(IOException e) {
            System.out.println("Error! " + my_url + " is invalid.");
            return "";
        }
    }

    // This code also taken from: https://javadiscover.blogspot.com/2013/08/how-to-read-webpage-source-code-through.html
    public static String html_to_file(String my_url) {
        //Instantiating the URL class.
        try {
            // Fixes 403 error
            System.setProperty("http.agent", "Chrome");
            URL url = new URL(my_url);
            //Retrieving the contents of the specified page
            InputStream is = url.openConnection().getInputStream();
            
            // Name of temp file
            String tmp_file = "tmp_bracket_results.html";
            File myFile = new File(tmp_file);
            // Create file if it does not exist (TODO: Make sure tmp files cleaned up)
            if(!(myFile.exists())){ 
                myFile.createNewFile();
            }
            // Write to file
            FileWriter fWrite = new FileWriter(myFile);  
                    BufferedWriter bWrite = new BufferedWriter(fWrite); 
            int i=0;
            while((i=is.read()) != -1){
                bWrite.write((char)i); 
            }
            // Close buffer
            bWrite.close();
            return tmp_file;
        } catch(IOException e) {
            System.out.println("Error! " + my_url + " is invalid.");
            return "";
        }
    }
}