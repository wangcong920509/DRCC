/* Cong Wang
 * School Of Software
 * Tsinghua University, Beijing, China
 * wangcong15@mails.tsinghua.edu.cn
 * 2016.01.21
 */
package algorithm;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

public class CountNodes  extends ASTVisitor{

	public int mCount = 0;

	@Override
	public void preVisit(ASTNode astnode){
		mCount++;
	}
}