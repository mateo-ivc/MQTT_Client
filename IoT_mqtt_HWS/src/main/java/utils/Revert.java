package utils;

import java.util.ArrayList;

public class Revert {

	public ArrayList<Message> revert(ArrayList<Message> list) {
		ArrayList<Message> reverted = new ArrayList<>();
		for (int i = list.size() - 1; i > 0; i--) {
			reverted.add(new Message(list.get(i).getTopic(), list.get(i).getMessage()));
			if (reverted.size() == 11)
				reverted.remove(0);
		}
		return reverted;
	}

}
