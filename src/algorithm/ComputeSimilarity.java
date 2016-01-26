/* Cong Wang
 * School Of Software
 * Tsinghua University, Beijing, China
 * wangcong15@mails.tsinghua.edu.cn
 * 2016.01.21
 */
package algorithm;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import main.FileOperation;

public class ComputeSimilarity extends ASTVisitor{
	
	public Definition DEFI = new Definition();
	private int number = 1;
	private RefactorClone rfc = new RefactorClone();
	private final static String BASIC_ADDR = "result\\jdk_clone_code\\";
	
	// Method visit : calculate the similarity and combine the similar methods
	@SuppressWarnings("static-access")
	@Override
	public boolean visit(TypeDeclaration node){
		MethodDeclaration[] mdList = node.getMethods();
		for(int i = 0; i < mdList.length - 1; i++){
			for(int j = i + 1; j < mdList.length; j++){
				if(DEFI.Similarity(mdList[i], mdList[j])){
//					FileOperation.writeToFile(BASIC_ADDR + "threshold_" + String.format("%.1f", DEFI.threshold)+ "\\" + number + ".txt", mdList[i].toString() + mdList[j].toString());
					rfc.addMM(new MethodMatches(mdList[i], mdList[j]));
					number++;
				}
			}
		}
		rfc.refactor();
		int currNumber = rfc.number;
		rfc = new RefactorClone();
		rfc.number = currNumber;
		return false;
	}
}