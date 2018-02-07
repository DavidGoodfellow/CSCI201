package game_logic;

import java.io.Serializable;
//implements Serializable
public class User implements Serializable{
	public static final long serialVersionUID = 1;
	
	private String password;
	private String username;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	 
	 public String getUsername(){
		 return username;
	 }
	 
	 public String getPassword(){
		 return password;
	 }
}
