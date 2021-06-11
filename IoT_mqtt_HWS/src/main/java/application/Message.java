package application;

import java.time.LocalDateTime;

public class Message {
	private String topic;
	private String message;
//	private LocalDateTime time;

	public Message(String topic, String message) {
		this.topic = topic;
		this.message = message;
//		this.time = time;

	}

	String getTopic() {
		return topic;
	}

	String getMessage() {
		return message;
	}

//	LocalDateTime getTime() {
//		return time;
//	}

}
