package Data;

import java.util.ArrayList;

public class Profile {
	
	public static ArrayList<Profile> allProfiles = new ArrayList<>();
	
	private String UserID;
	private String Name;
	private String Birthday;
	private String Email;
	
	public void setName(String Name) {
		this.Name = Name;
	}
	
	public void setEmail(String Email) {
		this.Email = Email;
	}
	
	public void setBirthday(String Birthday) {
		this.Birthday = Birthday;
	}
	
	public void setUserID(String UserID) {
		this.UserID = UserID;
	}
	
	public String returnUserID() {
		return UserID;
	}
	
	public String returnName() {
		return Name;
	}
	
	public String returnBirthday() {
		return Birthday;
	}
	
	public String returnEmail() {
		return Email;
	}
	
	public static Profile profileExists(String UserID) {
		for(Profile p: allProfiles) {
			if(p.returnUserID().equals(UserID)) {
				return p;
			}
		}
		return null;
	}
	
	public static boolean emailExists(String email) {
		for(Profile p: allProfiles) {
			if(p.returnEmail().equals(email)) {
				return true;
			}
		}
		return false;
	}
	
	public Profile(String UserID, String Name, String Email) {
		this.UserID = UserID;
		this.Name = Name;
		this.Email = Email;
		allProfiles.add(this);
		MongoDB.saveProfile(this);
	}
}
