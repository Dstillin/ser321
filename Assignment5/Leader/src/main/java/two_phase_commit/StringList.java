package two_phase_commit;

import java.util.ArrayList;
import java.util.List;

/**
 * File: StringList.java
 * Author: Terry Grant Simpson
 * Description: Responsible for adding and displaying contents of the list.
 * Date: 1.27.2022
 */
public class StringList {

	List<String> strings = new ArrayList<>();

	public StringList(List<String> list) {
		strings.addAll(list);
	}

	synchronized public void add(String str) {
		int pos = strings.indexOf(str);
		if (pos < 0) {
			strings.add(str);
		}
	}

	/**
	 * Creates a one string with arraylist content;
	 * @return string of the arraylist
	 */
	synchronized public String displayList() {

		if (strings.isEmpty()) {
			return "Empty List";
		}

		StringBuilder rtnString = new StringBuilder();

		for (String string : strings) {
			if (strings.indexOf(string) == strings.size() - 1) {
				rtnString.append(string);
			} else {
				rtnString.append(string).append("\n");
			}
		}

		return rtnString.toString();
	}

	synchronized public int size() {
		return strings.size();
	}

	synchronized public String toString() {
		return strings.toString();
	}
}