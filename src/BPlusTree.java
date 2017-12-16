import java.util.ArrayList;
import java.util.ListIterator;

public class BPlusTree {

	public Node root;
	public static int order;

	/**
	 * Intializing the Bplus tree of order m
	 * 
	 * @param order
	 */

	public BPlusTree(int order) {
		BPlusTree.order = order;
	}

	/**
	 * Accepts key as parameter and returns all the occurrences of the key from the
	 * tree
	 * 
	 * @param key
	 * @return
	 */
	public ArrayList<String> search(double key) {
		// Search for data node that contain key
		ArrayList<String> result = new ArrayList<String>();
		DataNode dataNode = searchForDataNode(root, key);

		if (dataNode == null)
			return result;

		// Since duplicate keys are allowed.Iteratively look for all values of the key
		for (int i = 0; i < dataNode.keys.size(); i++) {
			if (dataNode.keys.get(i) == key) {
				if (i == 0) {
					// In this case we have to look for key in the previous data node
					ArrayList<String> previosNodeValues = searchPreviosNode(dataNode.previousNode, key);
					result.addAll(previosNodeValues);
				}
				result.add(dataNode.values.get(i));
				if (i == dataNode.keys.size() - 1) {
					// In this case we have to look for key in the next data node
					ArrayList<String> nextNodeValues = searchNextNode(dataNode.nextNode, key);
					result.addAll(nextNodeValues);
				}
			}
		}

		return result;
	}

	/**
	 * Recursively search in the previous nodes of leaf and compute a consolidated
	 * list
	 * 
	 * @param leaf
	 * @param key
	 * @return
	 */
	public ArrayList<String> searchPreviosNode(DataNode leaf, double key) {
		ArrayList<String> result = new ArrayList<String>();
		if (leaf == null) {
			return result;
		}
		for (int i = 0; i < leaf.keys.size(); i++) {
			if (leaf.keys.get(i) == key) {
				if (i == 0) {
					ArrayList<String> temp = searchPreviosNode(leaf.previousNode, key);
					result.addAll(temp);
				}
				result.add(leaf.values.get(i));
			}
		}
		return result;
	}

	/**
	 * Recursively look for key in the next nodes of next nodes
	 *
	 * @param leaf
	 * @param key
	 * @return
	 */

	public ArrayList<String> searchNextNode(DataNode leaf, double key) {
		ArrayList<String> result = new ArrayList<String>();
		if (leaf == null) {
			return result;
		}
		for (int i = 0; i < leaf.keys.size(); i++) {
			if (leaf.keys.get(i) == key) {
				result.add(leaf.values.get(i));
				if (i == leaf.keys.size() - 1) {
					ArrayList<String> temp1 = searchNextNode(leaf.nextNode, key);
					result.addAll(temp1);
				}
			}
		}
		return result;
	}

	/**
	 * This method first finds the node that contain startKey and recursively
	 * traverse forward from that node to node containting endKey
	 * 
	 * @param startKey
	 * @param endKey
	 * @return
	 */
	public ArrayList<String> search(double startKey, double endKey) {
		ArrayList<String> result = new ArrayList<String>();

		// look for data node that should contain key
		DataNode leaf = searchForDataNode(root, startKey);

		if (leaf == null)
			return result;

		while (leaf.keys.get(0) >= startKey) {
			if (leaf.previousNode != null)
				leaf = leaf.previousNode;
		}
		if (leaf.keys.get(leaf.keys.size() - 1) < startKey) {
			if (leaf.nextNode != null)
				leaf = leaf.nextNode;
		}

		boolean shouldProceed = true;

		while (shouldProceed) {
			shouldProceed = addValues(leaf, result, startKey, endKey);
			leaf = leaf.nextNode;
		}

		return result;
	}

	/**
	 * This method will compare all the keys between startKey and endKey and will
	 * write into a list
	 * 
	 * 
	 * @param leaf
	 * @param result
	 * @param startKey
	 * @param endKey
	 * @return
	 */

	public boolean addValues(DataNode leaf, ArrayList<String> result, double startKey, double endKey) {
		boolean shouldProceed = false;
		if (leaf == null)
			return false;

		for (int i = 0; i < leaf.keys.size(); i++) {
			if (startKey <= leaf.keys.get(i) && leaf.keys.get(i) <= endKey) {
				result.add("(" + leaf.keys.get(i) + "," + leaf.values.get(i) + ")");
				if (i == leaf.keys.size() - 1) {
					shouldProceed = true;
				}
			}
		}

		return shouldProceed;
	}

	/**
	 * This method uses BinarySearchTreeAlgorithm to search for a key
	 * @param theNode
	 * @param key
	 * @return
	 */
	public DataNode searchForDataNode(Node theNode, double key) {
		if (theNode == null)
			return null;

		if (theNode.isDataNode) {
			// Found the LeafNod
			return (DataNode) theNode;
		} else {
			// The node is an index node
			IndexNode indexNode = (IndexNode) theNode;

			if (key < theNode.keys.get(0)) {
				return searchForDataNode(indexNode.children.get(0), key);
			} else if (key >= theNode.keys.get(theNode.keys.size() - 1)) {
				return searchForDataNode(indexNode.children.get(indexNode.children.size() - 1), key);
			} else {
				ListIterator<Double> iterator = indexNode.keys.listIterator();
				while (iterator.hasNext()) {
					if (iterator.next() > key) {
						return searchForDataNode(indexNode.children.get(iterator.previousIndex()), key);
					}
				}
			}
		}

		return null;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 *            Entry point to insert Key value into the Tree
	 * 
	 */

	public void insert(double key, String value) {
		// Initial insert to the tree
		if (root == null) {
			root = new DataNode(key, value);
		}

		PairPOJO overflowed = handleInsert(root, key, value);
		if (overflowed != null) {
			// Create a new root node as it is full
			root = new IndexNode(overflowed.getKey(), root, overflowed.getNode());
		}

	}

	/**
	 * 
	 * @param theNode
	 * @param key
	 * @param value
	 * @return
	 * 
	 * 		This Method is find the location of datanode where key, value to be
	 *         inserted After insertion if there is a overflow in either datanode or
	 *         index node Then recursively splits the node and reinserts into the
	 *         tree
	 */
	private PairPOJO handleInsert(Node theNode, double key, String value) {
		PairPOJO overflowedNode = null;
		if (theNode.isDataNode) {
			DataNode leaf = (DataNode) theNode;
			leaf.insertIntoDataNode(key, value);
			if (leaf.isNodeFull()) {
				PairPOJO rightSplit = handleDataNodeOverflow(leaf);
				return rightSplit;
			}
			return null;
		} else {
			IndexNode idxNode = (IndexNode) theNode;
			if (key < (double) theNode.keys.get(0))
				overflowedNode = handleInsert(idxNode.children.get(0), key, value);
			else if (key >= (double) theNode.keys.get(idxNode.keys.size() - 1))
				overflowedNode = handleInsert(idxNode.children.get(idxNode.children.size() - 1), key, value);
			else {
				// insert at one of the middle child
				for (int i = 0; i < idxNode.children.size(); i++) {
					if ((double) idxNode.keys.get(i) > key) {
						overflowedNode = handleInsert(idxNode.children.get(i), key, value);
						break;
					}
				}
			}
		}
		if (overflowedNode != null) {
			// This module is to find the location of overflow node
			IndexNode indexNode = (IndexNode) theNode;
			double splittingKey = overflowedNode.getKey();
			int indexAtParent = indexNode.keys.size();
			if (splittingKey < indexNode.keys.get(0)) {
				indexAtParent = 0;
			} else if (splittingKey > indexNode.keys.get(indexNode.keys.size() - 1)) {
				indexAtParent = indexNode.children.size();
			} else {
				for (int i = 0; i < indexNode.keys.size(); i++) {
					if (splittingKey < indexNode.keys.get(i)) {
						indexAtParent = i;
						break;
					}
				}
			}

			indexNode.insertIntoIndexNode(overflowedNode, indexAtParent);

			if (indexNode.isNodeFull()) {
				PairPOJO rightSplit = handleIndexNodeOverflow(indexNode);
				return rightSplit;
			}
			return null;
		}
		return overflowedNode;

	}

	/*
	 * This method is used when there is a DataNode overflow Index node will be
	 * split into two DataNodes left and right of size order/2 and returns the newly
	 * created right DataNodes to the caller to insert back into Bplus tree
	 * Recursively
	 */
	public PairPOJO handleDataNodeOverflow(DataNode leaf) {
		int newNodeSize = (order / 2) + 1;

		ArrayList<Double> rightKeys = new ArrayList<Double>(newNodeSize);
		ArrayList<String> rightValues = new ArrayList<String>(newNodeSize);

		rightKeys.addAll(leaf.keys.subList(order / 2, leaf.keys.size()));
		rightValues.addAll(leaf.values.subList(order / 2, leaf.values.size()));

		// delete the right side from the left
		leaf.keys.subList(order / 2, leaf.keys.size()).clear();
		leaf.values.subList(order / 2, leaf.values.size()).clear();

		DataNode rightLeaf = new DataNode(rightKeys, rightValues);

		// manage the new siblinghood
		reArrangeDataNodePointers(leaf, rightLeaf);
		return new PairPOJO(rightLeaf.keys.get(0), rightLeaf);

	}

	/**
	 * 
	 * @param leftLeaf
	 * @param rightLeaf
	 *            This method will re arrange the doubly linked list pointers after
	 *            inserting a new data node into a B Plus tree
	 * 
	 * 
	 */
	private void reArrangeDataNodePointers(DataNode leftLeaf, DataNode rightLeaf) {
		if (leftLeaf.nextNode != null) {
			rightLeaf.nextNode = leftLeaf.nextNode;
			rightLeaf.nextNode.previousNode = rightLeaf;
		}
		leftLeaf.nextNode = rightLeaf;
		rightLeaf.previousNode = leftLeaf;
	}

	/**
	 * This method is used when there is a indexnode overflow Index node will be
	 * split into two indexnodes left and right of size order/2 and returns the
	 * newly created right indexnode to the caller to insert back into Bplus tree
	 * Recursively
	 */

	public PairPOJO handleIndexNodeOverflow(IndexNode index) {
		int rightSplitSize = order / 2;
		ArrayList<Double> rightKeys = new ArrayList<Double>(rightSplitSize);
		ArrayList<Node> rightChildren = new ArrayList<Node>(rightSplitSize + 1);

		rightKeys.addAll(index.keys.subList((order / 2) + 1, index.keys.size()));
		rightChildren.addAll(index.children.subList((order / 2) + 1, index.children.size()));

		// push up the new index
		IndexNode rightNode = new IndexNode(rightKeys, rightChildren);
		PairPOJO splitted = new PairPOJO(index.keys.get(order / 2), rightNode);

		index.keys.subList((order / 2), index.keys.size()).clear();
		index.children.subList((order / 2) + 1, index.children.size()).clear();

		return splitted;
	}
}
