package application;

public class Message {
	private String topic;
	private String message;

	public Message(String topic, String message) {
		this.topic = topic;
		this.message = message;

	}

	String getTopic() {
		return topic;
	}
	String getMessage() {
		return message;
	}

}
