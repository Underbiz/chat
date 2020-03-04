package chat.client;

import chat.network.User;

public class Messages {
	
	
	private String value;
	private String date;

	public Messages(String value, String date) {
		
		this.value = value;
		this.date = date;
	}

	


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDate() {
		return date;
	}
	
	
}
