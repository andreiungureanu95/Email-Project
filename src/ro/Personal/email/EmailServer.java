package ro.Personal.email;

import java.util.List;

import ro.Personal.email.model.Account;
import ro.Personal.email.model.Email;

import java.io.FileWriter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;

public class EmailServer {

	public static final String DOMAIN = "@itschool.ro";
	
	private List<Account> accounts;
	private FileWriter fileWriter;

	public EmailServer() {
		this.accounts = new ArrayList<Account>();
		
		try {
			this.fileWriter = new FileWriter("D:/passwords.txt");
		}catch(Exception e ) {
			e.printStackTrace();
		}
	}

	public void createAccount(String username, String plainPassword, String firstName, String lastName,
			LocalDate dateOfBirth) {
		try {
			
			boolean isPasswordComplex = this.checkPasswordComplexity(plainPassword);
			if (!isPasswordComplex) {
				System.out.println("Your password does not match our complexity rules.");
				return;
			}
			
			String passwordHash = this.hashPassword(plainPassword);
			
			
			
			Account freshAccount = new Account(username, firstName, lastName, passwordHash);

			if (this.accounts.contains(freshAccount)) {
				System.out.println(
						"Account with username " + username + " already exists. Please select a differenta account");
				return;
			}

			if (dateOfBirth != null) {
				freshAccount.setDateOfBirth(dateOfBirth);
			}

			this.accounts.add(freshAccount);
		} catch (Exception e) {
			System.out.println("There was an error creating your account");
			e.printStackTrace();
		}
	}

	public Account login(String emailAddress, String plainPassword) {
		try {
			Account accountInList = findAccount(emailAddress);

			if (accountInList == null) {
				throw new Exception(emailAddress + " does not exist on our server");
			} else {
				String passwordHash = this.hashPassword(plainPassword);
				
				if (accountInList.checkPassword(passwordHash)) {
					// successful login
					return accountInList;
				} else {
					throw new Exception("Wrong username or password");
				}
			}
		} catch (Exception e) {
			System.out.println("There was a problem logging you in. Please try again.");
		}
		
		return null;
	}

	private Account findAccount(String emailAddress) {
		for (int index = 0; index < this.accounts.size(); index++) {
			Account accountAtIndex = this.accounts.get(index);

			if (accountAtIndex.getEmailAddress().equalsIgnoreCase(emailAddress)) {
				return accountAtIndex;
			}
		}

		return null;
	}

	private String hashPassword(String plainPassword) throws Exception {
		try {
			MessageDigest digester = MessageDigest.getInstance("SHA-256");
			byte[] passwordBytes = digester.digest(plainPassword.getBytes("UTF-8"));
			String passwordHash = Base64.getEncoder().encodeToString(passwordBytes);
			
			return passwordHash;
		} catch (Exception e) {
			throw e;
		}
	}
	
	private boolean checkPasswordComplexity(String plainPassword) {
		if (plainPassword == null) {
			return false;
		}
		
		String complexityRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{5,}$";
		
		boolean result = plainPassword.matches(complexityRegex);
		
		return result;
	}
	
	public List<Account> getAccountsForAddresses(String csvAccounts) {
		List<Account> accounts = new ArrayList<Account>();
		
		String[] emailAddresses = csvAccounts.split(",");
		
		for (String emailAddress : emailAddresses) {
			if (!emailAddress.endsWith(DOMAIN)) {
				emailAddress = emailAddress + DOMAIN;
			}
			
			Account account = this.findAccount(emailAddress);
			accounts.add(account);
		}
		
		return accounts;
	}
	
	private List<Account> getOldAccounts() {
		List<Account> accounts = new ArrayList<Account>();
		
		for (Account account : this.accounts) {
			if (account.getSessionAge() > 10) {
				accounts.add(account);
			}
		}
		
		return accounts;
	}
	
	public void sendEmail(Email email) {
		List<Account> destinations = email.getDestinations();
		
		for(Account account : destinations) {
			account.pushNewEmail(email);
		}
		
		email.getSource().sendEmail(email);
	}
}
