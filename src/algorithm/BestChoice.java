/* Cong Wang
 * School Of Software
 * Tsinghua University, Beijing, China
 * wangcong15@mails.tsinghua.edu.cn
 * 2016.01.21
 */
package algorithm;

import java.util.ArrayList;
import java.util.List;

public class BestChoice {
	public List<Matches> list = new ArrayList<Matches>();
	private int result = 0;
	private int max = 0;
	private int mFlag[];
	private int count1[], count2[];
	public void addCount(int[] countNode1, int[] countNode2){
		count1 = countNode1;
		count2 = countNode2;
	}
	public void addMatch(Matches mt){
		list.add(mt);
	}
	public int bestChoice(int pos){
		if(pos == 0)
			mFlag = new int[list.size()];
		if(possible(pos)){
			mFlag[pos] = 1;
			result = result + count1[list.get(pos).posInLeft] + count2[list.get(pos).posInRight];
			if(pos == list.size() - 1){
				if(result > max)
					max = result;
			}
			else
				bestChoice(pos + 1);
			result = result - count1[list.get(pos).posInLeft] - count2[list.get(pos).posInRight];
		}
		mFlag[pos] = 0;
		if(pos == list.size() - 1){
			if(result > max)
				max = result;
		}
		else
			bestChoice(pos + 1);
		return max;
	}
	public Boolean possible(int pos){
		if(pos == 0)
			return true;
		int originLeft, originRight;
		int newLeft = list.get(pos).posInLeft;
		int newRight = list.get(pos).posInRight;
		for(int i = 0; i < pos; i++)
			if(mFlag[i] == 1){
				originLeft = list.get(i).posInLeft;
				originRight = list.get(i).posInRight;
				if(!(originLeft > newLeft && originRight > newRight) || (originLeft < newLeft && originRight < newRight))
					return false;
			}
		return true;
	}
}