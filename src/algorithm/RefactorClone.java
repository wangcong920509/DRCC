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

public class RefactorClone  extends ASTVisitor{
	
	// Method visit : extract clone statements when visiting the TypeDeclaration node
	@Override
	public boolean visit(TypeDeclaration node){
		MethodDeclaration[] mdList = node.getMethods();
		return false;
	}
}