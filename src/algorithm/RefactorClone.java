/* Cong Wang
 * School Of Software
 * Tsinghua University, Beijing, China
 * wangcong15@mails.tsinghua.edu.cn
 * 2016.01.21
 */
package algorithm;

import java.util.ArrayList;
import java.util.List;

public class RefactorClone{
	
	@SuppressWarnings("unused")
	private int lenMin = 3;
	private List<MethodMatches> list = new ArrayList<MethodMatches>();
	
	// Method setLenMin : set the value of lenMin
	public void setLenMin(int lm){
		lenMin = lm;
	}
	
	// Method addMM : add a new MethodMatches to list
	public void addMM(MethodMatches mms){
		list.add(mms);
	}

	// Method refactor : calculate the refactored code snippets
	public void refactor() {
		
	}
}