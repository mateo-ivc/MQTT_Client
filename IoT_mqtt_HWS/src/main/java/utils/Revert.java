package utils;

import java.util.ArrayList;

public class Revert {

	public ArrayList<Message> revert(ArrayList<Message> list) {
		ArrayList<Message> reverted = new ArrayList<>();
		for (int i = list.size() - 1; i >= 0; i--) {
			// revert arraylist
			reverted.add(list.get(i));
			if (reverted.size() == 11)
				// if max is exceeded remove first item
				reverted.remove(0);
		}
		return reverted;
	}

}
