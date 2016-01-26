/* Cong Wang
 * School Of Software
 * Tsinghua University, Beijing, China
 * wangcong15@mails.tsinghua.edu.cn
 * 2016.01.21
 */
package algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;

import main.FileOperation;

public class RefactorClone{
	
	public int number = 1;
	public int number2 = 1;
	public double threshold = 0.7;
	private int lenMin = 3;
	private List<MethodMatches> list = new ArrayList<MethodMatches>();
	private final static String BASIC_ADDR = "result\\jdk_refactor\\";
	
	
	// Method setLenMin : set the value of lenMin
	public void setLenMin(int lm){
		lenMin = lm;
	}
	
	// Method addMM : add a new MethodMatches to list
	public void addMM(MethodMatches mms){
		list.add(mms);
	}

	// Method refactor : handle the list of MethodMatches 
	public void refactor() {
		for(int i = 0; i < list.size(); i++)
			RCC(list.get(i));
	}
	
	// Method RCC : calculate the refactored code snippets
	@SuppressWarnings({ "unchecked" })
	public void RCC(MethodMatches mm){
		List<SingleVariableDeclaration> svd1 = mm.mdLeft.parameters();
		List<SingleVariableDeclaration> svd2 = mm.mdRight.parameters();
		if(compare(svd1, svd2) && compare2(mm))
			Combination(mm);
		else
			Extraction(mm);
	}
	
	// Method compare : compare the lists of parameters
	public boolean compare(List<SingleVariableDeclaration> a, List<SingleVariableDeclaration> b) {
	    if(a.size() != b.size())
	        return false;
	    Boolean mFlag;
	    for(int i = 0; i < a.size(); i++){
	    	mFlag = false;
	    	for(int j = 0; j < b.size(); j++){
	    		if(a.get(i).toString().equals(b.get(i).toString()))
	    			mFlag = true;
	    	}
	    	if(!mFlag)
	    		return false;
	    }
	    return true;
	}
	
	public boolean compare2(MethodMatches mm){
		if(mm.mdLeft.getReturnType2() == null && mm.mdRight.getReturnType2() == null)
			return true;
		if((mm.mdLeft.getReturnType2() != null && mm.mdRight.getReturnType2() == null)
			|| (mm.mdLeft.getReturnType2() == null && mm.mdRight.getReturnType2() != null)
			|| (!mm.mdLeft.getReturnType2().toString().equals(mm.mdRight.getReturnType2().toString())))
			return false;
		return true;
	}
	
	// Method Combination
	@SuppressWarnings({ "unchecked" })
	public void Combination(MethodMatches mm){
		AST ast = mm.mdLeft.getParent().getAST();
		MethodDeclaration md = ast.newMethodDeclaration();
		String name_1 = mm.mdLeft.getName().toString();
		String name_2 = mm.mdRight.getName().toString();
		SimpleName nameNewMethod = ast.newSimpleName(name_1 + "_" + name_2);
		md.setName(nameNewMethod);
		List<SingleVariableDeclaration> svd1 = mm.mdLeft.parameters();
		List<SingleVariableDeclaration> svd2 = mm.mdRight.parameters();
		for(int k = 0; k < svd1.size(); k++){
			SingleVariableDeclaration variableDeclaration = svd1.get(k);
			md.parameters().add(ASTNode.copySubtree(ast, variableDeclaration));
		}
		for(int k = 0; k < svd2.size(); k++){
			SingleVariableDeclaration variableDeclaration = svd2.get(k);
			md.parameters().add(ASTNode.copySubtree(ast, variableDeclaration));
		}
		SingleVariableDeclaration svd3 = ast.newSingleVariableDeclaration();
		String newParamName = "combFlag";
		svd3.setName(ast.newSimpleName(newParamName));
		svd3.setType(ast.newPrimitiveType(PrimitiveType.INT));
		md.parameters().add(svd3);
		Block newBlock = combineMethod(ast, mm, newParamName);
		md.setBody(newBlock);
		String content = mm.mdLeft.toString() +  "\n" + mm.mdRight.toString() + "\n-->\n" + md.toString();
//		FileOperation.writeToFile(BASIC_ADDR + "threshold_" + String.format("%.1f", threshold)+ "\\Combination\\" + number + ".txt" , content);
		number++;
	}
	
	// Method combineMethod
	@SuppressWarnings("unchecked")
	private Block combineMethod(AST ast, MethodMatches mm, String outlineFlag){
		Block result = ast.newBlock();
		Block bk1 = mm.mdLeft.getBody();
		Block bk2 = mm.mdRight.getBody();
		List<Statement> list1 = bk1.statements();
		List<Statement> list2 = bk2.statements();
		int len1 = list1.size();
		int len2 = list2.size();
		int cursor1 = 0;
		int cursor2 = 0;
		Queue<Integer> queue1 = new LinkedBlockingQueue<Integer>();
		Queue<Integer> queue2 = new LinkedBlockingQueue<Integer>();
		int currIn2 = 0;
		for(int i = 0; i < len1; i++)
			for(int j = currIn2; j < len2; j++)
				if(Definition.isSame(list1.get(i), list2.get(j)) && j > currIn2){
					queue1.add(i);
					queue2.add(j);
					currIn2 = j;
					break;
				}
		while(cursor1 < len1 || cursor2 < len2){
			if(!queue1.isEmpty() && cursor1 != queue1.element() || !queue2.isEmpty() && cursor2 != queue2.element()){
				IfStatement ifstmt = ast.newIfStatement();
				InfixExpression infixexpr = ast.newInfixExpression();
				infixexpr.setLeftOperand(ast.newSimpleName(outlineFlag));
				infixexpr.setOperator(org.eclipse.jdt.core.dom.InfixExpression.Operator.EQUALS);
				infixexpr.setRightOperand(ast.newNumberLiteral("1")); 
				ifstmt.setExpression(infixexpr);
				Block thenBlock = ast.newBlock();
				Block elseBlock = ast.newBlock();
				ifstmt.setThenStatement(thenBlock);
				ifstmt.setElseStatement(elseBlock);
				while(cursor1 < queue1.element()){
					thenBlock.statements().add(ASTNode.copySubtree(ast, list1.get(cursor1)));
					cursor1 += 1;
				}
				while(cursor2 < queue2.element()){
					elseBlock.statements().add(ASTNode.copySubtree(ast, list2.get(cursor2)));
					cursor2 += 1;
				}
				result.statements().add(ifstmt);
			}
			result.statements().add(ASTNode.copySubtree(ast, list1.get(cursor1)));
			cursor1 += 1;
			cursor2 += 1;
			queue1.poll();
			queue2.poll();
		}
		return result;
	}
	
	
	// Method Extraction
	@SuppressWarnings("unchecked")
	public void Extraction(MethodMatches mm){
		BlockCollector bc = new BlockCollector();
		mm.mdLeft.accept(bc);
		mm.mdRight.accept(bc);
		int startPos = 0;
		int endPos = 0;
		for(int m = 0; m < bc.list.size() - 1; m++){
			for(int n = m + 1; n < bc.list.size(); n++){
				Block bk1 = bc.list.get(m);
				Block bk2 = bc.list.get(n);
				List<Statement> list1 = bk1.statements();
				List<Statement> list2 = bk2.statements();
				int len1 = list1.size();
				int len2 = list2.size();
				Queue<Integer> queue1 = new LinkedBlockingQueue<Integer>();
				Queue<Integer> queue2 = new LinkedBlockingQueue<Integer>();
				int currIn2 = 0;
				for(int i = 0; i < len1; i++)
					for(int j = currIn2; j < len2; j++)
						if(list1.get(i).toString().compareTo(list2.get(j).toString()) == 0 && j >= currIn2){
							queue1.add(i);
							queue2.add(j);
							currIn2 = j;
							break;
						}
				if(queue1.size() < lenMin)
					continue;
				else{
					int len = queue1.size();
					int[] array1 = new int[len];
					int[] array2 = new int[len];
					for(int i = 0; i < len; i++)
						array1[i] = queue1.poll();
					for(int i = 0; i < len; i++)
						array2[i] = queue2.poll();
					int mCount1[] = new int[len];
					int mCount2[] = new int[len];
					for(int i = 0; i < len - 1; i++){
						for(mCount1[i] = 1; i + mCount1[i] != len; mCount1[i]++)
							if(array1[i + mCount1[i]] != array1[i] + mCount1[i])
								break;
					}
					for(int i = 0; i < len - 1; i++){
						for(mCount2[i] = 1; i + mCount2[i] != len; mCount2[i]++)
							if(array2[i + mCount2[i]] != array2[i] + mCount2[i])
								break;
					}
					int finalCount[] = new int[len];
					int maxPos = 0;
					int maxCount = 1;
					for(int i = 0; i < len - 1; i++){
						finalCount[i] = Math.min(mCount1[i], mCount2[i]);
						if(finalCount[i] > maxCount){
							maxCount = finalCount[i];
							maxPos = i;
						}
					}
					startPos = array1[maxPos];
					endPos = array1[maxPos + finalCount[maxPos] - 1];
					if(endPos - startPos < lenMin - 1)
						continue;
					AST ast = mm.mdLeft.getParent().getAST();
					Block mBlock = ast.newBlock();
					for(int i = startPos; i <= endPos; i++)
						mBlock.statements().add(ASTNode.copySubtree(ast, list1.get(i)));
//					FileOperation.writeToFile(BASIC_ADDR + "threshold_" + String.format("%.1f", threshold)+ "\\Extraction_lenMin" + lenMin +"\\" + number2 + ".txt" , mBlock.toString());
					number2++;
				}
			}
		}
	}
}