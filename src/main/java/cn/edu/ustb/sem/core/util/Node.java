package cn.edu.ustb.sem.core.util;

import java.util.List;

public class Node<T> {
	public String nodeName;
	public List<Node<T>> children;
	public Node<T> parent;
	public T data;
}
