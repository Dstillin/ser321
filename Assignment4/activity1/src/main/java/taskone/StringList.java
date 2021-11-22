package taskone;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class StringList {

  List<String> strings = new ArrayList<String>();

  public void add(String str) {
	int pos = strings.indexOf(str);
	if (pos < 0) {
	  strings.add(str);
	}
  }

  /**
   * Remove a string within arraylist
   *
   * @param index integer representing location of string to be remove
   */
  public String remove(int index) {

	if (strings.isEmpty()) {return "empty";}

	String rtnValue = "empty";

	if (isWithinBounds(index)) {
	  rtnValue = strings.get(index);
	  strings.remove(index);
	}

	return rtnValue;
  }

  /**
   * Returns the string of the provided index location
   *
   * @param index integer representing location of string to be returned
   * @return String value
   */
  public String getValue(int index) {

	if (strings.isEmpty()) {return "empty";}

	if (isWithinBounds(index)) {
	  return strings.get(index);
	}

	return "Not Found";
  }

  /**
   * Validates index is within arraylist bounds
   *
   * @param index integer | location of string
   * @return true: within bounds | false: outside of bounds
   */
  public boolean isWithinBounds(int index) {
	if (strings.isEmpty()) {return false;}
	return index >= 0 && index < strings.size();
  }

  /**
   * Creates a one string with arraylist content;
   *
   * @return string of the arraylist
   */
  public String displayList() {

	if (strings.isEmpty()) {return "Empty List";}

	StringBuilder rtnString = new StringBuilder();

	for (String string : strings) {
	  if (strings.indexOf(string) == strings.size() - 1) {
		rtnString.append(string);
	  }
	  else {
		rtnString.append(string).append("\n");
	  }
	}

	return rtnString.toString();
  }

  /**
   * Reverses the order of a string value within the arraylist
   *
   * @param index integer | location of string
   * @return String value
   */
  public String reverse(int index) {

	if (strings.isEmpty()) {return "Empty List";}

	if (isWithinBounds(index)) {

	  //  Original string
	  String originalString = getValue(index);

	  //  Array for reversed string
	  char[] charArray = new char[originalString.length()];

	  //  Reverse string
	  for (int i = 0; i < originalString.length(); i++) {
		char originalChar = originalString.charAt(i);
		charArray[(originalString.length() - 1) - i] = originalChar;
	  }

	  //  Add reversed string
	  String newString = new String(charArray);
	  strings.set(index, newString);
	  return displayList();
	}

	return "Not Found";
  }


  public boolean contains(String str) {
	return strings.indexOf(str) >= 0;
  }

  public int size() {
	return strings.size();
  }

  public String toString() {
	return strings.toString();
  }
}