/* Cong Wang
 * School Of Software
 * Tsinghua University, Beijing, China
 * wangcong15@mails.tsinghua.edu.cn
 * 2016.01.21
 */
package algorithm;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;

public class BlockCollector extends ASTVisitor{
	
	public List<Block> list = new ArrayList<Block>();
	
	@Override
	public boolean visit(Block bk){
		list.add(bk);
		return true;
	}
}
