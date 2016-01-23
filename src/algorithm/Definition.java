/* Cong Wang
 * School Of Software
 * Tsinghua University, Beijing, China
 * wangcong15@mails.tsinghua.edu.cn
 * 2016.01.21
 */
package algorithm;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;

public class Definition {
	
	private String srcName = "";
	private int number = 1;
	private double threshold = 0.7;
	private static CountNodes mVisit = new CountNodes();
	
	// Method setSrcName : set the name of the source file
	public void setSrcName(String str){
		srcName = str;
	}
	
	// Method setThreshold : set the threshold of clone code
	public void setThreshold(double newthres){
		threshold = newthres;
	}
	
	/* Method isSame : judge the sameness of two objects
	 * return the bool result - whether stmt1 and stmt2 are the same
	 */
	public Boolean isSame(Object stmt1, Object stmt2){
		if(stmt1 == null || stmt2 == null)
			return false;
		if(stmt1.toString().compareTo(stmt2.toString()) == 0)
			return true;
		else if(((Statement)stmt1).getNodeType() != ((Statement)stmt2).getNodeType())
			return false;
		else if(((Statement)stmt1).getNodeType() == ASTNode.IF_STATEMENT){
			if(((IfStatement)stmt1).getExpression().toString().compareTo(((IfStatement)stmt2).getExpression().toString()) == 0){
				return isSame(((IfStatement)stmt1).getThenStatement(), ((IfStatement)stmt2).getThenStatement()) && 
				isSame(((IfStatement)stmt1).getElseStatement(), ((IfStatement)stmt2).getElseStatement());
			}
			else
				return false;
		}
		else if(((Statement)stmt1).getNodeType() == ASTNode.BLOCK){
			return Similarity((Block)stmt1, (Block)stmt2);
		}
		else if(((Statement)stmt1).getNodeType() == ASTNode.FOR_STATEMENT){
			if(((ForStatement)stmt1).getExpression() == null || ((ForStatement)stmt2).getExpression() == null)
				return false;
			if(((ForStatement)stmt1).getExpression().toString().compareTo(((ForStatement)stmt2).getExpression().toString()) == 0
			&&((ForStatement)stmt1).initializers().toString().compareTo(((ForStatement)stmt2).initializers().toString()) == 0
			&&((ForStatement)stmt1).updaters().toString().compareTo(((ForStatement)stmt2).updaters().toString()) == 0){
				return  isSame(((ForStatement)stmt1).getBody(), ((ForStatement)stmt2).getBody());
			}
		}
		else if(((Statement)stmt1).getNodeType() == ASTNode.WHILE_STATEMENT){
			if(((WhileStatement)stmt1).getExpression().toString().compareTo(((WhileStatement)stmt2).getExpression().toString()) == 0)
				return  isSame(((WhileStatement)stmt1).getBody(), ((WhileStatement)stmt2).getBody());
		}
		else if(((Statement)stmt1).getNodeType() == ASTNode.DO_STATEMENT){
			if(((DoStatement)stmt1).getExpression().toString().compareTo(((DoStatement)stmt2).getExpression().toString()) == 0){
				return  isSame(((DoStatement)stmt1).getBody(), ((DoStatement)stmt2).getBody());
			}
		}
		return false;
	}
	
	/* Method isSame : judge the sameness of two MethodDeclarations
	 * return the bool result - whether md1 and md2 are the similar
	 */
	@SuppressWarnings("unchecked")
	public Boolean Similarity(MethodDeclaration md1, MethodDeclaration md2){
		mVisit = new CountNodes();
		if(md1.getBody() == null || md2.getBody() == null)
			return false;
		List<Statement> list1 = md1.getBody().statements();
		List<Statement> list2 = md2.getBody().statements();
		if(list1.size() == 1 || list2.size() == 1)
			return false;
		int total = 0;
		double same = 0.0;
		int len1 = list1.size();
		int len2 = list2.size();
		//calculate the number of node
		int countNodes1[] = new int[len1];
		for(int i = 0; i < len1; i++){
			list1.get(i).accept(mVisit);
			countNodes1[i] = mVisit.mCount;
			total += mVisit.mCount;
		}
		int countNodes2[] = new int[len2];
		for(int i = 0; i < len2; i++){
			list2.get(i).accept(mVisit);
			countNodes2[i] = mVisit.mCount;
			total += mVisit.mCount;
		}
		BestChoice bc = new BestChoice();
		bc.addCount(countNodes1, countNodes2);
		int temp = -1;
		Boolean mFlag = true;
		for(int i = 0; i < len1; i++)
			for(int j = 0; j < len2; j++)
				if(isSame(list1.get(i), list2.get(j))){
					if(j < temp)
						mFlag = false;
					temp = j;
					bc.addMatch(new Matches(i, j));
				}
		if(bc.list.size() != 0)
			same = bc.bestChoice(0);
		if(!mFlag){
			System.out.println("[" + number + ":" + srcName + "]" + md1.getName().toString() + "-" + md2.getName().toString() + ":" + same / total);
			number += 1;
		}
		return same / total > threshold;
	}
	
	/* Method Similarity £º judge the sameness of two Blocks
	 * return the bool result - whether bk1 and bk2 are the similar
	 */
	@SuppressWarnings("unchecked")
	public Boolean Similarity(Block bk1, Block bk2){
		mVisit = new CountNodes();
		if(bk1 == null || bk2 == null)
			return false;
		List<Statement> list1 = bk1.statements();
		List<Statement> list2 = bk2.statements();
		if(list1.size() == 1 || list2.size() == 1)
			return false;
		int total = 0;
		double same = 0.0;
		int len1 = list1.size();
		int len2 = list2.size();
		//calculate the number of node
		int countNodes1[] = new int[len1];
		for(int i = 0; i < len1; i++){
			list1.get(i).accept(mVisit);
			countNodes1[i] = mVisit.mCount;
			total += mVisit.mCount;
		}
		int countNodes2[] = new int[len2];
		for(int i = 0; i < len2; i++){
			list2.get(i).accept(mVisit);
			countNodes2[i] = mVisit.mCount;
			total += mVisit.mCount;
		}
		BestChoice bc = new BestChoice();
		bc.addCount(countNodes1, countNodes2);
		for(int i = 0; i < len1; i++)
			for(int j = 0; j < len2; j++)
				if(isSame(list1.get(i), list2.get(j)))
					bc.addMatch(new Matches(i, j));
		if(bc.list.size() != 0)
			same = bc.bestChoice(0);
		return same / total > threshold;
	}
}