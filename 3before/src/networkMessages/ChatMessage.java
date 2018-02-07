package networkMessages;

import java.io.Serializable;

public abstract class ChatMessage implements Serializable {
	public static final long serialVersionUID = 1;
	private String message;
	private String name;
	
	public ChatMessage(String name, String message){
		this.message = message;
		this.name = name;
	}

	public String getMessage() {
		return message;
	}
	public String getName(){
		return name;
	}
	
}
