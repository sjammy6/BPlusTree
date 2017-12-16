/**
 * 
 * @author srikanthjammy
 * This is the user defined class to hold the key value pair
 * Since advanced data structures like java.util.map are not allowed
 * Creating the equivalent class and setter getter methods to access data 
 *
 */
public class PairPOJO {
	
	public double key;
	public Node node;
	
	public PairPOJO(double key, Node node) {
		this.key = key;
		this.node = node;
	}
	
	public double getKey() {
		return key;
	}

	public void setKey(double key) {
		this.key = key;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}
}
