/* Cong Wang
 * School Of Software
 * Tsinghua University, Beijing, China
 * wangcong15@mails.tsinghua.edu.cn
 * 2016.01.21
 */
package algorithm;

import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

public class Definition {
	private String srcName = "";
	private int number = 1;
	private double threshold = 0.5;
	public void setSrcName(String str){
		srcName = str;
	}
	public void setThreshold(double newthres){
		threshold = newthres;
	}
	public Boolean isSame(Statement stmt1, Statement stmt2){
		if(stmt1.toString().compareTo(stmt2.toString()) == 0)
			return true;
		else if(stmt1.getNodeType() != stmt2.getNodeType())
			return false;
//		else if(stmt1.getNodeType() == ASTNode.IF_STATEMENT){
//		}
		return false;
	}
	@SuppressWarnings("unchecked")
	public Boolean Similarity(MethodDeclaration md1, MethodDeclaration md2){
		if(md1.getBody() == null || md2.getBody() == null)
			return false;
		List<Statement> list1 = md1.getBody().statements();
		List<Statement> list2 = md2.getBody().statements();
		int total = list1.size() + list2.size();
		double same = 0;
		int len1 = list1.size();
		int len2 = list2.size();
		int temp = -1;
		Boolean mFlag = true;
		for(int i = 0; i < len1; i++)
			for(int j = 0; j < len2; j++)
				if(isSame(list1.get(i), list2.get(j))){
					if(j < temp)
						mFlag = false;
					temp = j;
					same += 2.0;
					break;
				}
		if(!mFlag){
			System.out.println("[" + number + ":" + srcName + "]" + md1.getName().toString() + "-" + md2.getName().toString() + ":" + same / total);
			number += 1;
		}
		if(same / total > threshold)
			return true;
		else
			return false;
	}
	@SuppressWarnings("unchecked")
	public double Similarity(Block bk1, Block bk2){
		if(bk1 == null || bk2 == null)
			return 0;
		List<Statement> list1 = bk1.statements();
		List<Statement> list2 = bk2.statements();
		int total = list1.size() + list2.size();
		double same = 0;
		int len1 = list1.size();
		int len2 = list2.size();
		for(int i = 0; i < len1; i++)
			for(int j = 0; j < len2; j++)
				if(isSame(list1.get(i), list2.get(j))){
					same += 2.0;
					break;
				}
		return same / total;
	}
}
