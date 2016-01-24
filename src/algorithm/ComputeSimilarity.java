/* Cong Wang
 * School Of Software
 * Tsinghua University, Beijing, China
 * wangcong15@mails.tsinghua.edu.cn
 * 2016.01.21
 */
package algorithm;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ComputeSimilarity extends ASTVisitor{
	
	public Definition DEFI = new Definition();
	private int number = 1;
	private RefactorClone rfc = new RefactorClone();
	private final static String BASIC_ADDR = "result\\jdk_clone_code\\";
	
	// Method visit : calculate the similarity and combine the similar methods
	@Override
	public boolean visit(TypeDeclaration node){
		MethodDeclaration[] mdList = node.getMethods();
		for(int i = 0; i < mdList.length - 1; i++){
			for(int j = i + 1; j < mdList.length; j++){
				if(DEFI.Similarity(mdList[i], mdList[j])){
					writeToFile(BASIC_ADDR + "threshold_" + String.format("%.1f", DEFI.threshold)+ "\\" + number + ".txt", mdList[i].toString() + mdList[j].toString());
					rfc.addMM(new MethodMatches(mdList[i], mdList[j]));
					number++;
				}
			}
		}
		rfc.refactor();
		return false;
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
}