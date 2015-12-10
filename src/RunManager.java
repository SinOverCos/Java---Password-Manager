import java.io.*;
import java.util.*;

class RunManager {
	
	static Scanner input = new Scanner(System.in);
	static Manager M;
	
	static Template errorSentinel = new Template();
	static List<Template> error = new ArrayList<Template>();
	
	static final int EMAIL = 0;
	static final int ACCOUNT = 1;
	static final int UNDONE = 2;
	static final int NONE_STORED = 3;
	
	static Template lastDel; // tempIndex 0 for em, 1 for acc
	static String BAD_INPUT = "THIS IS THE BAD INPUT STRING. DO NOT DO ANYTHING IF THIS IS RECEIVED";
	static String OUT_OF_BOUNDS = "Out of bounds.\n";
	static String NO_NEW_VAL = "THERE IS NO NEW VALUE. LAUNCH THE INTERACTIVE FUNCTION";
	static String welcome = "Welcome to the password manager! Type \"help\" for instructions or Enter to continue. :)\n";
	static String instructions = "\n\nPassword manager manual:\n\n"
			+ "When the program starts, you'll be prompted for a file name - please include the .txt extension. If you do not yet have a file, feel free to name a new one.\n"
			+ "Next (and also before exiting the program), you'll be prompted for a key that is used to polyalphabetically encrypt the contents of your password text file. The key is case-sensitive and this program will not check for your key's validity. If you enter the wrong key to decrypt after encrypting - you'll have a bad time. Same goes for if you try to edit the text file outside this program. It might be safe to edit the text file if you didn't encrypt, but I still wouldn't recommend it.\n"
			+ "Your entries are stored in either the \"em\" (email) list or the \"acc\" (account) list. Each entry will have an index when printed.\n"
			+ "Email entries have the following fields:   \"address\" \"password\" \"security\" \"addinfo\" \"pastpass\"\n"
			+ "Account entries have the following fields: \"address\" \"password\" \"security\" \"addinfo\" \"pastpass\" \"user\" \"datecreated\"\n"
			+ "\nTo print 100 new lines to clear the screen: clear\n\n"
			+ "To print a list:\t\tprint <list>\n"
			+ "To retrieve an entry:\t\tget <list> <index/unique part of entry> <field name>.\n"
			+ "\t\t\t\tLeave out the field name if you want to print all of the entry's info.\n"
			+ "To edit certain fields:\t\tedit <list> <index/unique part of entry> <field name> <new value>.\n"
			+ "\t\t\t\tNew value is optional - you'll be prompted for a new value if it's not given here.\n"
			+ "To add a new entry:\t\tadd <list>\n"
			+ "To delete an entry:\t\tdel <list> <index>\n"
			+ "To undo the last deletion:\tundo del\n"
			+ "To quicksort:\t\t\tsort <list> <addinfo/email/user/date> user and date are for accounts only.\n"
			+ "To search:\t\t\tsearch <list> <term>\n"
			+ "To quit:\t\t\tquit\n"
			+ "\nIMPORTANT:\t\t\tYOU MUST SAVE WITH THE QUIT FUNCTION OR YOUR DATA WILL NOT SAVE PROPERLY.\n\n";
	
	
    public static void main(String[] args) {
    	//System.out.printf("Welcome! Type \"help\" for instructions or Enter to continue: ");
    	//String response = input.nextLine().toLowerCase();
    	//if(response.equals("help")) {help();}
    	
    	errorSentinel.address = BAD_INPUT;
    	error.add(errorSentinel);
    	
    	String FILE_NAME = getFileName();
        M = new Manager(FILE_NAME);
        interpret();
        M.quit(FILE_NAME);
    }
    
    static String getFileName() {
    	String FILE_NAME;
    	String check;
    	do {
    		System.out.printf("Enter the full file name of your password document (with the extension): ");
    		FILE_NAME = input.nextLine();
    		//System.out.printf("Press \"Enter\" if you are sure, enter any other key to try again: ");
    		//check = input.nextLine();
    		check = "";
    	} while (!check.equals(""));
    	if(FILE_NAME.equals("")) {return "MyFirstProject";}
    	return FILE_NAME;
    }
    
    static void interpret() {
    	
    	String[] cmdTokens = {"", "", "", "", ""};
    	int len;
    	String[] tempTokens;
    	String cmd = "";
    	
    	while(true) {
    		cmd = input.nextLine();
    		if(cmd.equals("")) {continue;}
    		tempTokens = cmd.split(" ");
    		int tempLen = tempTokens.length;
    		for(int i = 0; i < tempLen; i++) {
    			if(i >= 4) {continue;}
    			tempTokens[i] = tempTokens[i].toLowerCase();
    		}
    		
    		if(tempLen >= 5) {
    			String wholeEntry = "";
    			for(int i = 4; i < tempLen; i++) {
    				if(wholeEntry.equals("")) {wholeEntry += tempTokens[i];}
    				else {wholeEntry += " "; wholeEntry += tempTokens[i];}
    			}
    			for(int i = 0; i < 4; i++) {cmdTokens[i] = tempTokens[i];}
    			cmdTokens[4] = wholeEntry;
    			len = 5;
    		} else {
    			len = tempTokens.length;
    			for(int i = 0; i < tempTokens.length; i++) {
    				cmdTokens[i] = tempTokens[i];
    			}
    		}
    		
    		if(len == 0) {
    			continue;
    		} else if (cmdTokens[0].equals("clear")) {
    			clear();
    		} else if(cmdTokens[0].equals("help")) {
    			help();
    		} else if(cmdTokens[0].equals("print")) {
    			print(cmdTokens, len);
    		} else if(cmdTokens[0].equals("get")) {
    			get(cmdTokens, len);
    		} else if(cmdTokens[0].equals("edit")) {
    			edit(cmdTokens, len);
    		} else if(cmdTokens[0].equals("add")) {
    			add(cmdTokens, len);
    		} else if(cmdTokens[0].equals("del")) {
    			del(cmdTokens, len);
    		} else if(cmdTokens[0].equals("undo")) {
    			undo(cmdTokens, len);
    		} else if(cmdTokens[0].equals("sort")) {
    			sort(cmdTokens, len);
    		} else if(cmdTokens[0].equals("quit")) {
    			break;
    		} else {
    			invalid(cmdTokens[0]);
    		}
    	}
    	
    }
    
    static void tooShort() {
    	System.out.println("Not enough arguments for this method.");
    }
    
    static void invalid(String problem) {
    	System.out.printf("Invalid input: \"%s\" is not recognized.\n", problem);
    }
    
    static boolean badInput(String[] cmd, int len, int correctLen) {
    	if(len < correctLen) {tooShort(); return true;}
    	else if(len > correctLen) {invalid(cmd[correctLen]); return true;}
    	return false;
    }
    
    static void help() {
    	System.out.println(instructions);
    }
    
    static void clear() {
    	for(int i = 0; i < 100; i++) {System.out.println("");}
    }
    
    static void print(String[] cmd, int len) {
    	if(badInput(cmd, len, 2)) {return;}
    	else if(cmd[1].equals("acc")) {M.printList("acc");}
    	else if(cmd[1].equals("em")) {M.printList("em");}
    	else {invalid(cmd[1]);}
    }
    
    // source:
    // http://stackoverflow.com/questions/237159/whats-the-best-way-to-check-to-see-if-a-string-represents-an-integer-in-java
    static boolean isInteger(String str) {
    	if (str == null) {
    		return false;
    	}
    	int length = str.length();
    	if (length == 0) {
    		return false;
    	}
    	int i = 0;
    	if (str.charAt(0) == '-') {
    		if (length == 1) {
    			return false;
    		}
    		i = 1;
    	}
    	for (; i < length; i++) {
    		char c = str.charAt(i);
    		if (c <= '/' || c >= ':') {
    			return false;
    		}
    	}
    	return true;
    }

    static List<Template> getOrEdit(String[] cmd, int len) {
    	int minLen = (cmd[0].equals("get")? 3 : 4);
    	// Checking for invalid input
    	if(len < minLen) {tooShort(); return error;}
    	if(len > minLen+1) {invalid(cmd[minLen+1]); return error;}
    	if(!cmd[1].equals("acc") && !cmd[1].equals("em")) {invalid(cmd[1]); return error;}
    	if(isInteger(cmd[2])) {
    		int index = Integer.parseInt(cmd[2]);
    		if(cmd[1].equals("acc")) {
    			int lenAcc = M.accounts.size();
    			if(lenAcc == 0) {System.out.println(OUT_OF_BOUNDS); return error;}
    			if(index < 0 || index >= lenAcc) {System.out.println(OUT_OF_BOUNDS); return error;}
    		} else {
    			int lenEm = M.emails.size();
    			if(lenEm == 0) {System.out.println(OUT_OF_BOUNDS); return error;}
    			if(index < 0 || index >= lenEm) {System.out.println(OUT_OF_BOUNDS); return error;}
    		}
    		// Get list based on index here and return
    		List<Template> results = new ArrayList<Template>();
    		if(cmd[1].equals("acc")) {
    			Account target = (Account) M.accounts.get(index);
    			target.tempIndex = index;
    			results.add(target);
    			return results;
    		} else {
    			Email target = (Email) M.emails.get(index);
    			target.tempIndex = index;
    			results.add(target);
    			return results;
    		}
    	} else { // Searching instead of getting certain index
    		String target = cmd[2];
    		List<Template> results = new ArrayList<Template>();
    		if(cmd[1].equals("acc")) {
    			int lenAcc = M.accounts.size();
    			for(int i = 0; i < lenAcc; i++) {
    				Account trial = (Account) M.accounts.get(i);
    				if(trial.stringRep().toLowerCase().contains(target)) {
    					trial.tempIndex = i;
    					results.add(trial);
    				}
    			}
    			if(results.size() == 0) {System.out.println("Can't find such an entry."); return error;}
    		} else {
    			int lenEm = M.emails.size();
    			for(int i = 0; i < lenEm; i++) {
    				Email trial = (Email) M.emails.get(i);
    				if(trial.stringRep().toLowerCase().contains(target)) {
    					trial.tempIndex = i;
    					results.add(trial);
    				}
    			}
    			if(results.size() == 0) {System.out.println("Can't find such an entry."); return error;}
    		}
    		return results;
    	}
    }
    
    static boolean member(String type, String field) {
    	if(type.equals("acc")) {
    		if(field.equals("address")) {return true;}
    		else if(field.equals("password")) {return true;}
    		else if(field.equals("security")) {return true;}
    		else if(field.equals("pastpass")) {return true;}
    		else if(field.equals("addinfo")) {return true;}
    		else if(field.equals("user")) {return true;}
    		else if(field.equals("datecreated")) {return true;}
    		else {return false;}
    	} else {
    		if(field.equals("address")) {return true;}
    		else if(field.equals("password")) {return true;}
    		else if(field.equals("security")) {return true;}
    		else if(field.equals("pastpass")) {return true;}
    		else if(field.equals("addinfo")) {return true;}
    		else {return false;}
    	}
    }
    
    static String getField(Template each, String downcast, String field) { // Assumes field input is valid
    	if(downcast.equals("acc")) {
    		if(field.equals("address")) {return ((Account) each).getAddress();}
    		if(field.equals("password")) {return ((Account) each).getPassword();}
    		if(field.equals("security")) {return ((Account) each).getSecurity();}
    		if(field.equals("pastpass")) {return ((Account) each).getPastPass();}
    		if(field.equals("addinfo")) {return ((Account) each).getAddInfo();}
    		if(field.equals("user")) {return ((Account) each).getUser();}
    		else {return (Integer.toString(((Account) each).getDate()));}
    	} else {
    		if(field.equals("address")) {return ((Email) each).getAddress();}
    		if(field.equals("password")) {return ((Email) each).getPassword();}
    		if(field.equals("security")) {return ((Email) each).getSecurity();}
    		if(field.equals("pastpass")) {return ((Email) each).getPastPass();}
    		else {return ((Email) each).getAddInfo();}
    	}
    }
    
    static void get(String[] cmd, int len) {
    	List<Template> results = getOrEdit(cmd, len);
    	if(results.get(0).address.equals(BAD_INPUT)) {return;}
    	if(len == 3) {
    		for(Template each : results) {
    			if(cmd[1].equals("acc")) {
    				System.out.printf("Index: account %d\n", each.tempIndex);
    				System.out.println(((Account) each).stringRep());
    			}
    			else {
    				System.out.printf("Index: email %d\n", each.tempIndex);
    				System.out.println(((Email) each).stringRep());
    			}
    			System.out.println("");
    		}
    	} else {
    		String field = cmd[3];
    		if(cmd[1].equals("acc") && !member("acc", field)) {invalid(field); return;}
    		if(cmd[1].equals("em") && !member("em", field)) {invalid(field); return;}
    		for(Template each : results) {
    			if(cmd[1].equals("acc")) {
    				System.out.printf("Index: account %d\n", each.tempIndex);
    				System.out.println(getField(each, "acc", field));
    			}
    			else {
    				System.out.printf("Index: email %d\n", each.tempIndex);
    				System.out.println(getField(each, "em", field));
    			}
    			System.out.println("");
    		}
    	}
    	
    }
    
    static void editField(Template each, String downcast, String field, String newVal) { // Assumes field input is valid
    	
    	if(field.equals("address")) {
			if(newVal.equals(NO_NEW_VAL)) {each.updateAddress();}
   			else {each.updateAddress(newVal);}
   		}
   		else if(field.equals("password")) {
   			if(newVal.equals(NO_NEW_VAL)) {each.updatePassword();}
   			else {each.updatePassword(newVal);}
   		}
   		else if(field.equals("security")) {each.updateSecurityAddInfo("security");}
   		else if(field.equals("pastpass")) {System.out.println("Editing past passwords is currently not supported - sorry!\n");}
   		else if(field.equals("addinfo")) {each.updateSecurityAddInfo("addinfo");} // update function does .toLowerCase();
   		else if(field.equals("user")) {
   			if(newVal.equals(NO_NEW_VAL)) {((Account) each).updateUser();}
   			else {((Account) each).updateUser(newVal);}
   		}
   		else {
   			if(newVal.equals(NO_NEW_VAL)) {((Account) each).updateDateCreated();}
   			else {((Account) each).updateDateCreated(Integer.parseInt(newVal));}
   		}
    }
    
    static void edit(String[] cmd, int len) {
    	String field = cmd[3];
    	List<Template> results = getOrEdit(cmd, len);
    	if(results.get(0).getAddress().equals(BAD_INPUT)) {return;}
    	if(results.size() > 1) {
    		System.out.println("Could not find a unique entry. Were you looking for any of these?\n");
    		String getArray[] = {"get", cmd[1], cmd[2]};
    		get(getArray, 3);
    		return;
    	}
    	if(cmd[1].equals("acc") && !member("acc", field)) {invalid(field); return;}
		if(cmd[1].equals("em") && !member("em", field)) {invalid(field); return;}
		if(len == 5 && cmd[3].equals("datecreated") && !isInteger(cmd[4])) {invalid(cmd[4]); return;}
		editField(results.get(0), cmd[1], cmd[3], (len == 5 ? cmd[4] : NO_NEW_VAL));
    }
    
    static void add(String[] cmd, int len) {
    	if(badInput(cmd, len, 2)) {return;}
    	boolean isAcc = cmd[1].equals("acc");
    	boolean isEm = cmd[1].equals("em");
    	if(!isAcc && !isEm) {invalid(cmd[1]); return;}
    	String address;
    	String password;
    	String security = "";
    	String pastpass = "";
    	String addinfo = "";
    	String user = "";
    	int date = 0;
    	if(isAcc) {
    		System.out.printf("Enter the new user name: \t\t");
    		user = input.nextLine();
    		System.out.printf("Enter the date created (YYYYMMDD): \t");
    		String strDate;
    		do {
    			strDate = input.nextLine();
    			if(!isInteger(strDate)) {System.out.println("Invalid date, try again.");}
    		} while (!isInteger(strDate));
    		date = Integer.parseInt(strDate);
    	}
		System.out.printf("Enter the email address: \t\t");
		address = input.nextLine();
		System.out.printf("Enter the password: \t\t\t");
		password = input.nextLine();
		System.out.printf("Enter the security info (blank line to end input):\n");
		String buff = input.nextLine();
		while(!buff.equals("")) {
			if(security.equals("")) {security += buff;}
			else {security += ("\n" + buff);}
			buff = input.nextLine();
		}
		System.out.printf("Enter any additional info (blank line to end input):\n");
		buff = input.nextLine();
		while(!buff.equals("")) {
			if(addinfo.equals("")) {addinfo += buff;}
			else {addinfo += ("\n" + buff);}
			buff = input.nextLine();
		}
		if(isAcc) {
			Account newAcc = new Account(address, password, security, pastpass, addinfo, user, date);
			M.accounts.add(newAcc);
		} else {
			Email newEm = new Email(address, password, security, pastpass, addinfo);
			M.emails.add(newEm);
		}
		System.out.println("New entry added.\n");
    }
    
    static void del(String[] cmd, int len) {
    	if(badInput(cmd, len, 3)) {return;} // checking for length of command
    	boolean delAcc = cmd[1].equals("acc");
    	boolean delEm = cmd[1].equals("em");
    	if(!delAcc && !delEm) {invalid(cmd[1]); return;} // checking valid list
    	if(!isInteger(cmd[2])) {invalid(cmd[2]); return;} // checking index is int
    	int indexToDel = Integer.parseInt(cmd[2]);
    	int lenList = delAcc? M.accounts.size() : M.emails.size();
    	if(indexToDel < 0 || indexToDel >= lenList) {System.out.println(OUT_OF_BOUNDS); return;} // checking valid index
    	if(delAcc) {
    		lastDel = M.accounts.remove(indexToDel);
    		lastDel.tempIndex = 1;
    		System.out.printf("Accout number %d deleted.\n", indexToDel);
    	} else {
    		lastDel = M.emails.remove(indexToDel);
    		lastDel.tempIndex = 0;
    		System.out.printf("Email number %d deleted.\n", indexToDel);
    	}
    }

    static void undo(String[] cmd, int len) {
    	if(badInput(cmd, len, 2)) {return;}
    	if(!cmd[1].equals("del")) {invalid(cmd[1]); return;}
    	if(lastDel.tempIndex == EMAIL) {
    		M.emails.add((Email) lastDel); 
    		lastDel.tempIndex = UNDONE;
    		System.out.println("Email restored.");
    	}
    	else if(lastDel.tempIndex == ACCOUNT) {
    		M.accounts.add((Account) lastDel);
    		lastDel.tempIndex = UNDONE;
    		System.out.println("Account restored.");
    	}
    	else if(lastDel.tempIndex == UNDONE) {System.out.println("Delete has already been undone."); return;}
    	else if(lastDel.tempIndex == NONE_STORED) {System.out.println("There is no delete to undo."); return;}
    }
    
    static void sort(String[] cmd, int len) {
    	if(badInput(cmd, len, 3)) {return;} // checking command length
    	boolean sortAcc = cmd[1].equals("acc");
    	boolean sortEm = cmd[1].equals("em");
    	if(!sortAcc && !sortEm) {invalid(cmd[1]); return;} // checking valid list
    	boolean sortByUser = cmd[2].equals("user");
    	boolean sortByDate = cmd[2].equals("date");
    	boolean sortByEmail = cmd[2].equals("email");
    	boolean sortByAddInfo = cmd[2].equals("addinfo");
    	if(!sortByUser && !sortByDate && !sortByEmail && ! sortByAddInfo) {invalid(cmd[2]); return;}
    	if((sortByUser && sortEm) || (sortByDate && sortEm)) {invalid(cmd[2]); return;}
    	M.sort((sortAcc? M.accounts : M.emails), cmd[2]);
    }
}



