import java.util.ArrayList;
/**
 * 
 * @author srikanthjammy
 * This is basis structure of Node DataStructure
 * Index node and DataNode inherit properties of Node DataStructure
 *
 */
public class Node {
	public boolean isDataNode;
	public ArrayList<Double> keys;
	public boolean isNodeFull() {
		return keys.size() >= BPlusTree.order;
	}
}
