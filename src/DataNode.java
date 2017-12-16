
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class DataNode extends Node {

	public ArrayList<String> values;
	public DataNode previousNode;
	public DataNode nextNode;

	public DataNode(double key, String value) {
		isDataNode = true;
		keys = new ArrayList<Double>();
		values = new ArrayList<String>();
		keys.add(key);
		values.add(value);
	}

	public DataNode(List<Double> newKeys, List<String> newValues) {
		isDataNode = true;
		keys = new ArrayList<Double>(newKeys);
		values = new ArrayList<String>(newValues);
	}

	/*
	 * if the new key to be inserted is less than first element then insert at
	 * beginning if the new key to be inserted is greater than last element then
	 * insert at the end else traverse through the list to find the position to be
	 * inserted
	 *
	 */
	public void insertIntoDataNode(double key, String value) {
		if (key < keys.get(0)) {
			keys.add(0, key);
			values.add(0, value);
		} else if (key > keys.get(keys.size() - 1)) {
			keys.add(key);
			values.add(value);
		} else {
			ListIterator<Double> iterator = keys.listIterator();
			while (iterator.hasNext()) {
				if (iterator.next() >= key) {
					int position = iterator.previousIndex();
					keys.add(position, key);
					values.add(position, value);
					break;
				}
			}

		}
	}

}
