
import java.util.ArrayList;
import java.util.List;

public class IndexNode extends Node {
	/**
	 * This class represents the index node of Bplus tree it has two properties. m
	 * is the order of the Bplus tree one, m-1 keys of data type double. in second,
	 * ArrayList of m children pointers
	 * 
	 */
	public ArrayList<Node> children;

	public IndexNode(double key, Node leftChild, Node rightChild) {
		isDataNode = false;
		keys = new ArrayList<Double>();
		keys.add(key);
		children = new ArrayList<Node>();
		children.add(leftChild);
		children.add(rightChild);
	}

	public IndexNode(List<Double> newKeys, List<Node> newChildren) {
		isDataNode = false;
		keys = new ArrayList<Double>(newKeys);
		children = new ArrayList<Node>(newChildren);
	}

	/*
	 * if the new key to be inserted is less than first element then insert at
	 * beginning if the new key to be inserted is greater than last element then
	 * insert at the end else traverse through the list to find the position to be
	 * inserted
	 *
	 */
	public void insertIntoIndexNode(PairPOJO e, int index) {
		double key = e.getKey();
		Node child = e.getNode();
		if (index >= keys.size()) {
			keys.add(key);
			children.add(child);
		} else {
			keys.add(index, key);
			children.add(index + 1, child);
		}
	}
}
