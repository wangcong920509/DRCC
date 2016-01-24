/* Cong Wang
 * School Of Software
 * Tsinghua University, Beijing, China
 * wangcong15@mails.tsinghua.edu.cn
 * 2016.01.21
 */
package algorithm;

import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodMatches {
	
	public MethodDeclaration mdLeft;
	public MethodDeclaration mdRight;
	
	public MethodMatches(MethodDeclaration md1, MethodDeclaration md2){
		mdLeft = md1;
		mdRight = md2;
	}
}
