import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class treesearch {

	public static void main(String[] args) throws IOException {

		String inputFilePath = "";
		if (args.length > 0)
			inputFilePath = args[0];

		//File file = new File("//Users//srikanthjammy//Documents//input.txt");
		File file = new File(inputFilePath);
		FileReader fileReader = new FileReader(file);
		BufferedReader br = new BufferedReader(fileReader);

		File outputFile = new File("output_file.txt");
		FileWriter fw = new FileWriter(outputFile);
		BufferedWriter bw = new BufferedWriter(fw);

		String readLine = br.readLine();
		
		if (Integer.parseInt(readLine.trim()) < 3) {
			System.out.println("B plus tree doesnt exit for order less than 3. Please try higher order");
		}
		BPlusTree myTree = new BPlusTree(Integer.parseInt(readLine.trim()));
		while ((readLine = br.readLine()) != null) {

			if (readLine.contains("Insert") || readLine.contains("insert")) {
				double key = getKey(readLine);
				String value = getValue(readLine);
				myTree.insert(key, value);
			}

			if ((readLine.contains("Search") || readLine.contains("search")) && !readLine.contains(",")) {

				double searchKey = getSearchKey(readLine);

				ArrayList<String> searchResult = myTree.search(searchKey);
				if (searchResult.isEmpty()) {
					bw.write("Null");
				} else {
					bw.write(searchResult.toString().substring(1, searchResult.toString().length() - 1));
				}
				bw.newLine();
			}

			if ((readLine.contains("Search") || readLine.contains("search")) && readLine.contains(",")) {
				double startKey = getKey(readLine);
				double endKey = getEndKey(readLine);
				ArrayList<String> rangeSearchResult = myTree.search(startKey, endKey);
				if (rangeSearchResult.isEmpty()) {
					bw.write("Null");
				} else {
					bw.write(rangeSearchResult.toString().substring(1, rangeSearchResult.toString().length() - 1));
				}
				bw.newLine();
			}

		}

		br.close();
		bw.close();

	}

	public static double getKey(String command) {
		int a = command.indexOf("(");
		int b = command.indexOf(",");
		String temp = command.substring(a + 1, b).trim();
		return Double.parseDouble(temp);
	}

	public static double getEndKey(String command) {
		int a = command.indexOf(",");
		int b = command.indexOf(")");
		String temp = command.substring(a + 1, b).trim();
		return Double.parseDouble(temp);
	}

	public static String getValue(String command) {
		int a = command.indexOf(",");
		int b = command.indexOf(")");
		String temp = command.substring(a + 1, b).trim();
		return temp;
	}

	public static double getSearchKey(String command) {
		int a = command.indexOf("(");
		int b = command.indexOf(")");
		String temp = command.substring(a + 1, b).trim();
		return Double.parseDouble(temp);
	}
}
