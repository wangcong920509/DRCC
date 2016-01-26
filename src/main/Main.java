/* Cong Wang
 * School Of Software
 * Tsinghua University, Beijing, China
 * wangcong15@mails.tsinghua.edu.cn
 * 2016.01.21
 */
package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import algorithm.ComputeSimilarity;

public class Main {
	
	private final static String BASIC_ADDR = "jdk_src";
	@SuppressWarnings("unused")
	private final static String SAMPLE_FILE = "jdk_src\\com\\sun\\org\\apache\\xerces\\internal\\xni\\parser\\XMLParseException.java";
	private static ComputeSimilarity mVisit = new ComputeSimilarity();
	
	/* Method main : entrance of the tool
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args){
		List<String> fileList = getSampleFile(BASIC_ADDR);
		System.out.println("the number of Java files: " + fileList.size());
		for(String tempFile : fileList){
			String sampleContent = FileOperation.readToString(tempFile);
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setSource(sampleContent.toCharArray());
			parser.setResolveBindings(true);
			CompilationUnit result = (CompilationUnit) parser.createAST(null);
			mVisit.DEFI.setSrcName(tempFile);
			result.accept(mVisit);			
		}
		System.out.println(mVisit.DEFI.mCount);
		System.out.println(mVisit.DEFI.number);
		System.out.println(mVisit.DEFI.mCount / mVisit.DEFI.number / 2);
		
		
//		WARNING: THE FOLLOWING PART IS ONLY FOR DEBUGGING!
		
//		String sampleContent = FileOperation.readToString(SAMPLE_FILE);
//		ASTParser parser = ASTParser.newParser(AST.JLS3);
//		parser.setKind(ASTParser.K_COMPILATION_UNIT);
//		parser.setSource(sampleContent.toCharArray());
//		parser.setResolveBindings(true);
//		CompilationUnit result = (CompilationUnit) parser.createAST(null);
//		mVisit.DEFI.setSrcName(SAMPLE_FILE);
//		result.accept(mVisit);	
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