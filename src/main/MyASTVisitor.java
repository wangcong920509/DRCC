/* Cong Wang
 * School Of Software
 * Tsinghua University, Beijing, China
 * wangcong15@mails.tsinghua.edu.cn
 * 2016.01.21
 */
package main;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import algorithm.Definition;

public class MyASTVisitor extends ASTVisitor{
	public Definition DEFI = new Definition();
	@Override
	public boolean visit(TypeDeclaration node){
		MethodDeclaration[] mdList = node.getMethods();
		for(int i = 0; i < mdList.length - 1; i++){
			for(int j = i + 1; j < mdList.length; j++){
				DEFI.Similarity(mdList[i], mdList[j]);
			}
		}
		return false;
	}
}