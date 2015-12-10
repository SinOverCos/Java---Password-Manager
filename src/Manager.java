import java.io.*;
import java.util.*;

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
