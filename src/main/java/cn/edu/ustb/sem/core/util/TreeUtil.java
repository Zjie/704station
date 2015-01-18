package cn.edu.ustb.sem.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import cn.edu.ustb.sem.order.entity.Order;
import cn.edu.ustb.sem.schedule.entity.GroupProcessTree;
import cn.edu.ustb.sem.schedule.entity.GroupProcessTree.GroupProcessNode;
import cn.edu.ustb.sem.schedule.entity.GroupUnitProcess;

public class TreeUtil {
	/**
	 * 生成工序组的遍历树
	 * @param gup
	 * @return
	 */
	public static GroupProcessTree generateProcessGroupTree(List<GroupUnitProcess> gup, Order order) {
		GroupProcessTree tree = new GroupProcessTree();
		tree.order = order;
		Map<Integer, GroupProcessNode> map = new HashMap<Integer, GroupProcessNode>();
		for (GroupUnitProcess g : gup) {
			GroupProcessNode node = new GroupProcessNode();
			node.processGroup = g.getProcessGroup();
			node.gup = g;
			node.before = new HashSet<GroupProcessNode>();
			node.tree = tree;
			map.put(g.getProcessGroup(), node);
		}
		for (GroupUnitProcess g : gup) {
			GroupProcessNode afterNode = map.get(g.getAfterProcessGroup());
			if (afterNode != null) {
				GroupProcessNode cur = map.get(g.getProcessGroup());
				cur.after = afterNode;
				afterNode.before.add(cur);
			}
		}
		//设置叶子节点和根节点
		tree.leaves = new ArrayList<GroupProcessNode>();
		for (GroupUnitProcess g : gup) {
			GroupProcessNode node = map.get(g.getProcessGroup());
			if (node.after == null) {
				tree.root = node;
				continue;
			}
			if (node.before == null || node.before.size() == 0)
				tree.leaves.add(node);
		}
		return tree;
	}
	
}
