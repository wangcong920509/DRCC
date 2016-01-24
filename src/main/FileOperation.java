/* Cong Wang
 * School Of Software
 * Tsinghua University, Beijing, China
 * wangcong15@mails.tsinghua.edu.cn
 * 2016.01.24
 */
package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class FileOperation {
	
	/* Method readToString : read the content of a file to String
	 * fileName : name of the file
	 * return : the content of the file
	 */
	public static String readToString(String fileName) {  
        String encoding = "ISO-8859-1";
        File file = new File(fileName);  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            return new String(filecontent, encoding);  
        } catch (UnsupportedEncodingException e) {  
            System.err.println("The OS does not support " + encoding);  
            e.printStackTrace();  
            return null;  
        }  
    }
	
	/* Method writeToFile : write the content to the file
	 * fileName : name of the file
	 * content : a String written to file
	 */
	public static void writeToFile(String fileName, String content) {
		Writer writer;
		try {
			writer = new FileWriter(fileName);
			writer.write(content);  
	        writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
}
