package cn.edu.ustb.sem.core.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.ustb.sem.account.entity.Application;
import cn.edu.ustb.sem.account.entity.Role;
import cn.edu.ustb.sem.account.service.AccountService;
import cn.edu.ustb.sem.account.service.RoleService;
import cn.edu.ustb.sem.core.exception.ServiceException;
import cn.edu.ustb.sem.core.pagination.Page;
import cn.edu.ustb.sem.core.service.IndexService;
import cn.edu.ustb.sem.core.web.model.TreeStore;
import cn.edu.ustb.sem.core.web.model.TreeStore.Node;
@Service("indexService")
public class IndexServiceImpl implements IndexService {
	@Autowired
	private AccountService accountService;
	@Override
	public TreeStore getTreeStore(int roleId) throws ServiceException {
		RoleService rs = accountService.getRoleService();
		Role r = new Role();
		r.setId(roleId);
		Page<Role> result = rs.listAll(r, 0, 5);
		List<Role> roles = result.getItems();
		if (roles == null || roles.isEmpty()) {
			return new TreeStore();
		}
		Map<Integer, Node> allNodes = new HashMap<Integer, Node>();
		bindChildNodeWithParentNode(allNodes, roles);
		return generateTreeStoreFromNodes(allNodes);
	}
	/**
	 * 通过application之间的父子关系，绑定节点间的关系
	 * @param allNodes
	 * @param roles
	 */
	private void bindChildNodeWithParentNode(Map<Integer, Node> allNodes, List<Role> roles) {
		//对于多角色的支持
		for (Role role : roles) {
			Set<Application> apps = role.getApps();
			List<Application> sortedApp = new ArrayList<Application>();
			sortedApp.addAll(apps);
			//按照层级深度进行排序
			Collections.sort(sortedApp, new Comparator<Application>(){
				@Override
				public int compare(Application o1, Application o2) {
					if (o1.getLevel() == o2.getLevel()) {
						return o1.getId() - o2.getId();
					}
					return o1.getLevel() - o2.getLevel();
				}});
			for (Application app : sortedApp) {
				//如果是一级节点且不是菜单项
				if (app.getParent() == null && app.getIsMenu() != 1) {
					continue;
				}
				Node cur = new Node();
				cur.setText(app.getMenuName());
				cur.setExpanded(false);
				cur.setLevel(app.getLevel());
				Set<Application> childs = app.getChilds();
				if (childs == null || childs.isEmpty()) {
					cur.setLeaf(true);
					cur.setHrefTarget(app.getMenuUrl());
				} else {
					cur.setChildren(new ArrayList<Node>());
				}
				allNodes.put(app.getId(), cur);
				//没有父节点
				if (app.getParent() == null) {
					continue;
				}
				Integer pid = app.getParent().getId();
				if (pid == null || pid == 0) {
					continue;
				}
				//有父节点，查找父节点
				Node parent = allNodes.get(pid);
				if (parent == null) {
					continue;
				}
				//如果找到父节点，将该节点加入父节点的子叶中
				parent.getChildren().add(cur);
			}
		}
	}
	
	/**
	 * 从所有节点中，过滤出第一级节点，并加入根节点中
	 * @param allNodes
	 * @return
	 */
	private TreeStore generateTreeStoreFromNodes(Map<Integer, Node> allNodes) {
		TreeStore model = new TreeStore();
		model.setModel("Item");
		Node root = new Node();
		//遍历菜单树，并生成model
		Set<Entry<Integer, Node>> keyValue = allNodes.entrySet();
		List<Node> childs = new ArrayList<Node>();
		for (Entry<Integer, Node> kv : keyValue) {
			Node value = kv.getValue();
			if (value.getLevel() != 0) {
				continue;
			}
			//只把第一级节点加入根节点中
			childs.add(value);
		}
		root.setChildren(childs);
		model.setRoot(root);
		return model;
	}
	
}
