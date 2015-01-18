package cn.edu.ustb.sem.core.web.model;

import java.util.List;

public class TreeStore {
	private String model;
	private Node root;
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public static class Node {
		//菜单名称
		private String text;
		//是否展开
		private boolean expanded;
		//子节点
		private List<Node> children;
		//是否是叶子节点
		private boolean leaf;
		//对应的url
		private String hrefTarget;
		//层级
		private int level;
		//勾选框
		private Boolean checked;
		
		public Boolean getChecked() {
			return checked;
		}
		public void setChecked(Boolean checked) {
			this.checked = checked;
		}
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public boolean isExpanded() {
			return expanded;
		}
		public void setExpanded(boolean expanded) {
			this.expanded = expanded;
		}
		public List<Node> getChildren() {
			return children;
		}
		public void setChildren(List<Node> children) {
			this.children = children;
		}
		public boolean isLeaf() {
			return leaf;
		}
		public void setLeaf(boolean leaf) {
			this.leaf = leaf;
		}
		public String getHrefTarget() {
			return hrefTarget;
		}
		public void setHrefTarget(String hrefTarget) {
			this.hrefTarget = hrefTarget;
		}
		
	}
}
