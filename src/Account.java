import java.io.*;
import java.util.*;

class Account extends Template {
	String user = "";
	int dateCreated = 0; //yyyymmdd
	
	Account() {}
	
	String getUser() {return user;}
	int getDate() {return dateCreated;} 
	
	Account(String address, String password, String security, String pastPass, String addInfo, String user, int dateCreated) {
		super(address, password, security, pastPass, addInfo);
		this.user = user;
		this.dateCreated = dateCreated;
	}
	
	void updateUser() {
		String newUser;
		System.out.printf("Enter new username (r to exit): ");
		newUser = input.nextLine();
		if(newUser.equals("r")) {return;}
		user = newUser;
		System.out.printf("Username updated to %s\n", newUser);
	}
	void updateUser(String newUser) {
		user = newUser;
		System.out.printf("Username updated to %s\n", newUser);
	}
	void updateDateCreated() {
		int newDate;
		System.out.printf("Enter new date (YYYYMMDD) (0 to exit): ");
		newDate = input.nextInt();
		if(newDate == 0) {return;}
		dateCreated = newDate;
		System.out.printf("Date of creation changed to %s\n", parseDate(newDate));
	}
	void updateDateCreated(int newDate) {
		dateCreated = newDate;
		System.out.printf("Date of creation changed to %s\n", parseDate(newDate));
	}
	
	void appendInfo(String field_name, String info) {
		if(field_name.equals("<email>")) {
			if(address.equals("")) {address += info;}
			else {address += ("\n" + info);}
		}
		if(field_name.equals("<pass>")) {
			if(password.equals("")) {password += info;}
			else {password += ("\n" + info);}
		}
		if(field_name.equals("<security>")) {
			if(security.equals("")) {security += info;}
			else {security += ("\n" + info);}
		}
		if(field_name.equals("<past_pass>")) {
			if(pastPass.equals("")) {pastPass += info;}
			else {pastPass += ("\n" + info);}
		}
		if(field_name.equals("<addinfo>")) {
			if(addInfo.equals("")) {addInfo += info;}
			else {addInfo += ("\n" + info);}
		}
		if(field_name.equals("<user>")) {
			if(user.equals("")) {user += info;}
			else {user += ("\n" + info);}
		}
		if(field_name.equals("<date>")) {
			dateCreated = Integer.parseInt(info);
		}
	}
	
	String stringRep() {
		String represent = "USERNAME: " + user + "\n"; 
		represent += "PASSWORD: " + password + "\n";
		represent += "EMAIL ADDRESS: " + address + "\n";
		represent += "DATE CREATED: " + parseDate(dateCreated) + "\n";
		represent += "SECURITY INFO: " + (security.equals("")? "\n" : ("\n" + security + "\n"));
		represent += "PAST PASSWORDS: " + (pastPass.equals("")? "\n" : ("\n" + pastPass + "\n"));
		represent += "ADDITIONAL INFO: " + (addInfo.equals("")? "\n" : ("\n" + addInfo + "\n"));
		return represent;
		
	}
	
	String writtenFormat() {
		String toFile = "";
		toFile += "<ACC>\n<email>\n";
		toFile += address + "\n";
		toFile += "</email>\n<pass>\n";
		toFile += password + "\n";
		toFile += "</pass>\n<security>\n";
		toFile += (security.equals("")? security : (security + "\n"));
		toFile += "</security>\n<past_pass>\n";
		toFile += (pastPass.equals("")? pastPass : (pastPass + "\n"));
		toFile += "</past_pass>\n<addinfo>\n";
		toFile += (addInfo.equals("")? addInfo : (addInfo + "\n"));
		toFile += "</addinfo>\n<user>\n";
		toFile += user + "\n";
		toFile += "</user>\n<date>\n";
		toFile += Integer.toString(dateCreated) + "\n";
		toFile += "</date>\n</ACC>\n";
		
		return toFile;
	}

}
