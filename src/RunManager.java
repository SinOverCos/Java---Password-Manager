
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

class Manager {
	
	static Scanner input  = new Scanner(System.in);

	List<String> data;
	List<Template> emails = new ArrayList<Template>();
	List<Template> accounts = new ArrayList<Template>();
	
	public Manager(String FILE_NAME) {
				
		String key = getUnlockKey();
		for(int i = 0; i < 100; i++) {System.out.println("");}
		System.out.println("Here is your fresh screen. :)\n");
		
		if(!key.equals("n") && !key.equals("")) {decrypt(FILE_NAME, key);}
		
    	data = pullData(FILE_NAME);
    	int length = data.size();
    	String account_type;
    	String end_of_type;
    	for(int i = 0; i < length; i++) {
    		account_type = data.get(i);
    		end_of_type = defineEndStr(account_type);
    		String field_type;
    		String end_of_field;
    		i++;
    		Template account;
    		if(account_type.equals("<EM>")) {account = new Email();}
    		else {account = new Account();}
    		while(!data.get(i).equals(end_of_type)) {
    			
    			field_type = data.get(i);
    			end_of_field = defineEndStr(field_type);
    			i++;
    			while(!data.get(i).equals(end_of_field)) {
    				account.appendInfo(field_type, data.get(i));
    				i++;
    			}
    			i++;
    		}
    		if(account_type.equals("<EM>")) {emails.add((Email) account);}
    		else {accounts.add((Account) account);}
    	}
    }
	
	String getUnlockKey() {
		System.out.println("WARNING: This program does not check for your key's correctness. It will apply the decryption function with whatever key you give. If you give the wrong key, your information may get messed up. Remember that keys are case-sensitive.");
		String key_1;
		String key_2;
		do {
			System.out.printf("Enter your decryption key (\"n\" for no key):\t");
			key_1 = input.nextLine();
			System.out.printf("Enter it again to be sure:\t\t\t");
			key_2 = input.nextLine();
			if(!key_1.equals(key_2)) {System.out.println("Keys don't match. Try again.");}
		} while(!key_1.equals(key_2));
		return key_1;
	}
	
	void decrypt(String fileName, String key) {
		char[] keyChars = key.toCharArray();
		int keyLen = key.length();
		int index = 0;
		String ciphertext = "";
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(fileName));
			String buff;
			while(true) {
				buff = in.readLine();
				if(buff == null) {break;}
				else {ciphertext += buff;}
			}
			in.close();
		} catch(IOException e) {
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
				out.write("");
				out.close();
			} catch (IOException e2) {
				System.out.printf("There was a problem on line 156: " + e);
			}
		}
		int cipherLen = ciphertext.length();
		char[] ciphertextArray = ciphertext.toCharArray();
		
		for(int i = 0; i < cipherLen; i++) {
			int temp = (int) ciphertextArray[i] - (int) keyChars[index%keyLen];
			ciphertextArray[i] = (char) ((temp < 0)? (128 + temp) : temp);
			index++;
		}
				
		String plaintext = new String(ciphertextArray);
		try {
			FileWriter out = new FileWriter(fileName);
			out.write(plaintext);
			out.close();
		} catch(Exception e) {
			System.out.println("There was a problem on line 172: " + e);
		}
		
	}

    String defineEndStr(String begin) {
    	if(begin.equals("<EM>")) {return "</EM>";}
    	else if(begin.equals("<ACC>")) {return "</ACC>";}
    	else if(begin.equals("<email>")) {return "</email>";}
    	else if(begin.equals("<pass>")) {return "</pass>";}
    	else if(begin.equals("<security>")) {return "</security>";}
    	else if(begin.equals("<past_pass>")) {return "</past_pass>";}
    	else if(begin.equals("<addinfo>")) {return "</addinfo>";}
    	else if(begin.equals("<user>")) {return "</user>";}
    	else {return "</date>";}
    }
        
    void sort(List<Template> toSort, String field) {
    	quickSort(toSort, 0, toSort.size()-1, field);
    }
    
    void quickSort(List<Template> toSort, int first, int last, String field) {
    	if(last <= first) {return;}
    	Template pivot = toSort.get(first);	
    	int pos = last;
    	
    	for(int i = last; i >= first + 1; i--) {
    		if(compare(toSort.get(i), pivot, field) >= 0) {
    			Collections.swap(toSort, i, pos);
    			pos--;
    		}
    	}
    	Collections.swap(toSort, first, pos);
    	quickSort(toSort, first, pos-1, field);
    	quickSort(toSort, pos+1, last, field);
    }
    
    int compare(Template ae1, Template ae2, String field) { // user, date, or email, or addinfo
    	if(field.equals("user")) {
    		Account a1 = (Account) ae1;
    		Account a2 = (Account) ae2;
    		int comparison = a1.user.compareTo(a2.user);
    		if(comparison < 0) { return -1; }
    		else if(comparison > 0) { return 1;	}
    		else { return 0; }
    	} else if(field.equals("date")) {
    		Account a1 = (Account) ae1;
    		Account a2 = (Account) ae2;
    		if(a1.dateCreated == a2.dateCreated) {return 0;}
    		else if(a1.dateCreated < a2.dateCreated) {return -1;}
    		else {return 1;}
    	} else if(field.equals("email")){
    		int comparison = ae1.address.compareTo(ae2.address);
    		if(comparison < 0) { return -1; }
    		else if(comparison > 0) { return 1;	}
    		else { return 0; }
    	} else {
    		int comparison = ae1.addInfo.compareTo(ae2.addInfo);
    		if(comparison < 0) {return -1;}
    		else if(comparison > 0) {return 1;}
    		else {return 0;}
    	}
    }
    
    void printList(String downcast) { // em or acc
    	int len;
    	if(downcast.equals("em")) {len = emails.size();}
    	else {len = accounts.size();}
    	for(int i = 0; i < len; i++) {
    		if(downcast.equals("em")) {
    			System.out.printf("Index: email %d\n", i);
    			System.out.println(emails.get(i).stringRep()); // println adds \n to end
    		} else {
    			System.out.printf("Index: account %d\n", i);
    			System.out.println(accounts.get(i).stringRep()); // println adds \n to end
    		}
    		System.out.println("");
    	}
    }
    
    void addNew(String type) { // "acc" or "em"
    	int date = 0;
    	String user = "";
    	if(type.equals("acc")) {
    		System.out.printf("New entry's date of creation: ");
    		String dateStr = input.nextLine();
    		date = Integer.parseInt(dateStr);
    		System.out.printf("New entry's username: ");
    		user = input.nextLine();
    	}
    	
    	System.out.printf("New entry's email:");
    	String address = input.nextLine();
    	System.out.printf("New entry's password: ");
    	String pass = input.nextLine();
    	
    	System.out.println("New entry's past passwords (one per line, blank line to end): ");
    	String pastPass = "";
    	String buff = input.nextLine();
    	while(!buff.equals("")) {
    		pastPass += (pastPass.equals("") ? buff : ("\n" + buff));
    		buff = input.nextLine();
    	}
    	
    	System.out.println("New entry's security (one per line, blank line to end): ");
    	String security = "";
    	buff = input.nextLine();
    	while(!buff.equals("")) {
    		security += (security.equals("") ? buff : ("\n" + buff));
    		buff = input.nextLine();
    	}
    	
    	System.out.println("New entry's additional info (blank line to end): ");
    	String addInfo = "";
    	buff = input.nextLine();
    	while(!buff.equals("")) {
    		addInfo += (addInfo.equals("") ? buff : ("\n" + buff));
    		buff = input.nextLine();
    	}
    	
    	if(type.equals("acc")) {
    		Account newAcc = new Account(address, pass, security, pastPass, addInfo, user, date);
    		accounts.add(newAcc);
    	} else {
    		Email newEm = new Email(address, pass, security, pastPass, addInfo);
    		emails.add(newEm);
    	}
    	    	
    }
    
	Template remove(String list, int index) {
		Template deleted;
		if(list.equals("acc")) {deleted = accounts.remove(index);}
		else {deleted = emails.remove(index);}
		return deleted;
	}

    /*
    void search(String list, String term) { // "acc" or "em"
    	int len;
    	if(list.equals("acc")) {len = accounts.size();}
    	else {len = emails.size();}
    	for(int i = 0; i < len; i++) {
    		String strRep;
    		if(list.equals("acc")) {strRep = accounts.get(i).stringRep();}
    		else {strRep = emails.get(i).stringRep();}
    		if(strRep.toLowerCase().contains(term.toLowerCase())) {System.out.printf("%s", strRep);}
    	}
    }
    */
    List<String> pullData(String FILE_NAME) {
        BufferedReader in;
        List<String> lines = new ArrayList<String>();
        try {
            in = new BufferedReader(new FileReader(FILE_NAME));
            String buff;
            while(true) {
            	buff = in.readLine();
            	if(buff == null) {break;}
            	lines.add(buff);
            }
            in.close();
            return lines;
        } catch(IOException e) {
        	try {
        		BufferedWriter out = new BufferedWriter(new FileWriter(FILE_NAME));
        		out.write("");
        		out.close();
        		return lines;
        	} catch(IOException e2) {
        		System.out.println("There was a problem on line 367: " + e);
        		return lines;
        	}
        }
    }
    
    String getLockKey() {
    	System.out.println("WARNING: This program doesn't keep track of your key and will decrypt with whatever key you give later. Type in \"n\" for no key.");
    	String key_1;
    	String key_2;
    	do {
    		System.out.printf("Enter your encryption key:\t\t");
    		key_1 = input.nextLine();
    		System.out.printf("Enter your encryption key again:\t");
    		key_2 = input.nextLine();
    		if(!key_1.equals(key_2)) {System.out.println("Keys don't match; try again.");}
    	} while (!key_1.equals(key_2));
    	return key_1;
    }
    
    String encrypt(String allInfo, String key) {
    	char[] infoChars = allInfo.toCharArray();
    	char[] keyChars = key.toCharArray();
    	int infoLen = allInfo.length();
    	int keyLen = key.length();
    	int index = 0;
    	for(int i = 0; i < infoLen; i++) {
    		infoChars[i] = (char) (((int) infoChars[i] + (int) keyChars[index%keyLen])%128);
    		index++;
    	}
    	allInfo = new String(infoChars);
    	System.out.printf("%s\n", new String(infoChars));
    	return new String(infoChars);
    }
    
    void quit(String FILE_NAME) {
    	String key = getLockKey();
    	String allInfo = "";
    	
    	for(Template temp : emails) {
    		allInfo += temp.writtenFormat();
    	}
    	for(Template temp : accounts) {
    		allInfo += temp.writtenFormat();
    	}
    	
    	String toWrite;
    	if(!key.equals("n") && !key.equals("")) {toWrite = encrypt(allInfo, key);}
    	else {toWrite = allInfo;}
    	
    	try {
    		FileWriter out = new FileWriter(FILE_NAME);
    		out.write("");
        	out.append(toWrite);
    		out.close();
    	} catch(Exception e) {
    		System.out.println("There was a problem on line 421: " + e);
    	}
    }
}

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

class Email extends Template {
	
	Email() {
		super();
	}
	
	Email(String address, String password, String security, String pastPass, String addInfo) {
		super(address, password, security, pastPass, addInfo);
	}
	
}

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
