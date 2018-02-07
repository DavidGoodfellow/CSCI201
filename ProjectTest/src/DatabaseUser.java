
import java.awt.Image;

public class DatabaseUser {
	private String id;
	private String email;
	private String password;
	private String fname;
	private String lname;
	private String bio;
	private int userType;
	private String image;
	
	public DatabaseUser(JSONObject input){
		id = input.get("ID");
		email = input.get("Email");
		password = input.get("password");
		fname = input.get("FirstName");
		lname = input.get("LastName");
		//bio is not included upon first DatabaseUser construction
		userType = Integer.parseInt(input.get("UserType"));
		if(!input.isNull("Picture")){
			image = input.get("Picture");
		}
		else{
			image = null;
		}
	}
	
	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getFname() {
		return fname;
	}

	public String getLname() {
		return lname;
	}

	public String getBio() {
		return bio;
	}
	//need this set method
	public void setBio(String bio) {
		this.bio = bio;
	}
	public int getUserType() {
		return userType;
	}

	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}	
	
}
