import java.io.*;
import java.util.*;

class Template {
	String address;
	String password;
	String security;
	String pastPass;
	String addInfo;
	int tempIndex;
	
	static Scanner input  = new Scanner(System.in);
	//call super constructor with the above 5, handle extras in subclass constructor
	
	Template() {
		address = "";
		password = "";
		security = "";
		pastPass = "";
		addInfo = "";
	}
	
	Template(String address, String password, String security, String pastPass, String addInfo) {
		this.address = address;
		this.password = password;
		this.security = security;
		this.pastPass = pastPass;
		this.addInfo = addInfo;
	}
	
	String getAddress() {return address;}
	String getPassword() {return password;}
	String getSecurity() {return security;}
	String getPastPass() {return pastPass;}
	String getAddInfo() {return addInfo;}
	
	void updateAddress() {
		String newAddress;
		System.out.printf("Enter new email address (r to exit): ");
		newAddress = input.nextLine();
		if(newAddress.equals("r")) {return;}
		address = newAddress;
		System.out.printf("Email address updated to %s\n", address);
	}
	void updateAddress(String newAddress) {
		address = newAddress;
		System.out.printf("Email address updated to %s\n", address);
	}
	void updatePassword() {
		String p1;
		String p2;
		String newPass;
		System.out.printf("Enter new password (r to exit): ");
		p1 = input.nextLine();
		if(p1.equals("r")) {return;}
		System.out.printf("Enter new password again to complete change: ");
		p2 = input.nextLine();
		if(p1.equals(p2)) {
			newPass = p1;
			appendInfo("<past_pass>", password);
			password = newPass;
			System.out.printf("Password changed to %s\n", password);
		} else {
			System.out.printf("Passwords don't match; password not changed\n");
		}
	}
	void updatePassword(String newPass) {
		appendInfo("<past_pass>", password);
		password = newPass;
		System.out.printf("Password changed to %s\n", password);
	}
	void updateSecurityAddInfo(String s_or_a) { // "security" or "addInfo"
		s_or_a = s_or_a.toLowerCase();
		String field_name = s_or_a;
		if(s_or_a.equals("addinfo")) {field_name = "additional info";}
		String a_or_o;
		System.out.printf("Append or overwrite %s (a/o) (r to exit): ", field_name);
		a_or_o = input.nextLine();
		while(!a_or_o.equals("a") && !a_or_o.equals("o") && !a_or_o.equals("r")) {
			a_or_o = input.nextLine();
		}
		if(a_or_o.equals("r")) {return;}
		if(a_or_o.equals("a")) {appendOverwrite("a", s_or_a);}
		else if(a_or_o.equals("o")) {appendOverwrite("o", s_or_a);}
	}
	
	void appendOverwrite(String action, String field) {
		if(action.equals("o") && field.equals("security")) {security = "";}
		else if (action.equals("o") && field.toLowerCase().equals("addinfo")) {addInfo = "";}
		String ao = "";
		if(action.equals("a")) {ao = "append";}
		else if(action.equals("o")) {ao = "overwrite";}
		
		String to_ao = "";
		System.out.printf("Line to %s (blank line to end input): ", ao);
		String buff = input.nextLine();
		while(!buff.equals("")) {
			to_ao += (buff + "\n");
			buff = input.nextLine();
		}
		if(field.equals("security")) {appendInfo("<security>", to_ao);}
		else if(field.equals("addinfo")) {appendInfo("<addinfo>", to_ao);}
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
			else {security += (/*"\n" + */info);}
		}
		if(field_name.equals("<past_pass>")) {
			if(pastPass.equals("")) {pastPass += info;}
			else {pastPass += (/*"\n" + */info);}
		}
		if(field_name.equals("<addinfo>")) {
			if(addInfo.equals("")) {addInfo += info;}
			else {addInfo += (/*"\n" + */info);}
		}
	}
	
	String parseMonth(int month) {
		String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		return " " + months[month-1] + " ";
	}
	
	// http://stackoverflow.com/questions/10013998/fastest-way-to-parse-a-yyyymmdd-date-in-java
	String parseDate(int date) {
		if(date == 0) {return "No date entered.";}
		String year = Integer.toString(date / 10000);
		int month_num = (date % 10000) / 100;
		String month = parseMonth(month_num);
		String day = Integer.toString(date % 100);
		return day + month + year;
	}
	
	String stringRep() {
		String represent = "EMAIL ADDRESS: " + address + "\n";
		represent += "PASSWORD: " + password + "\n";
		represent += "SECURITY INFO: " + (security.equals("")? "\n" : ("\n" + security + "\n"));
		represent += "PAST PASSWORDS: " + (pastPass.equals("")? "\n" : ("\n" + pastPass + "\n"));
		represent += "ADDITIONAL INFO: " + (addInfo.equals("")? "\n" : ("\n" + addInfo + "\n"));
		return represent;
		}
	
	String writtenFormat() {
		String toFile = "";
		toFile += "<EM>\n<email>\n";
		toFile += address + "\n";
		toFile += "</email>\n<pass>\n";
		toFile += password + "\n";
		toFile += "</pass>\n<security>\n";
		toFile += (security.equals("")? security : (security + "\n"));
		toFile += "</security>\n<past_pass>\n";
		toFile += (pastPass.equals("")? pastPass : (pastPass + "\n"));
		toFile += "</past_pass>\n<addinfo>\n";
		toFile += (addInfo.equals("")? addInfo : (addInfo + "\n"));
		toFile += "</addinfo>\n</EM>\n";
		
		return toFile;
	}
	
}
