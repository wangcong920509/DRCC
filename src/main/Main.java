/* Cong Wang
 * School Of Software
 * Tsinghua University, Beijing, China
 * wangcong15@mails.tsinghua.edu.cn
 * 2016.01.21
 */
package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import algorithm.ComputeSimilarity;

public class Main {
	
	private final static String BASIC_ADDR = "jdk_src";
	@SuppressWarnings("unused")
	private final static String SAMPLE_FILE = "jdk_src\\java\\math\\BigInteger.java";
	private static ComputeSimilarity mVisit = new ComputeSimilarity();
	
	/* Method main : entrance of the tool
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args){
		List<String> fileList = getSampleFile(BASIC_ADDR);
		System.out.println("the number of Java files: " + fileList.size());
		for(String tempFile : fileList){
			String sampleContent = readToString(tempFile);
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setSource(sampleContent.toCharArray());
			parser.setResolveBindings(true);
			CompilationUnit result = (CompilationUnit) parser.createAST(null);
			mVisit.DEFI.setSrcName(tempFile);
			result.accept(mVisit);
		}
	}
	
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
	public void writeToFile(String fileName, String content) {
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
	
	/* Method getSampleFile
	 * return : the list of file names
	 */
	public static List<String> getSampleFile(String dirName){
		List<String> result = new ArrayList<String>();
		File file = new File(dirName);
		File[] subFileList = file.listFiles();
		if(subFileList != null){
			for(int i = 0; i < subFileList.length; i++){
				if(subFileList[i].isFile() && subFileList[i].getName().endsWith(".java"))
					result.add(subFileList[i].getPath());
				else if(subFileList[i].isDirectory())
					result.addAll(getSampleFile(subFileList[i].getPath()));
			}
		}
		return result;
	}
}