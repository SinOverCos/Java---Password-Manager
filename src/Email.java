import java.io.*;
import java.util.*;

class Email extends Template {
	
	Email() {
		super();
	}
	
	Email(String address, String password, String security, String pastPass, String addInfo) {
		super(address, password, security, pastPass, addInfo);
	}
	
}

