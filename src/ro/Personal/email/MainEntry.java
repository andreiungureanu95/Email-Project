package ro.Personal.email;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ro.Personal.email.model.Account;
import ro.Personal.email.model.Email;

public class MainEntry {

	private static EmailServer emailServer;
	private static Account currentAccount = null;
	private static Scanner scanner;

	public static void main(String[] args) {
		emailServer = new EmailServer();
		scanner = new Scanner(System.in);
		
		Thread cleanupThread = new Thread(new CleanupTask());
		cleanupThread.start();
		
		addSomeTestAccounts();
		
		print("Welcome to ITS Mail");
		showMainMenu();
	}

	private static void showMainMenu() {
		boolean isRunning = true;

		do {
			print("Please sign in, or sign up");
			print("1 - Sign up");
			print("2 - Login");
			print("0 - Exit");
			
			print("Please enter your choice");
			
			byte choice = scanner.nextByte();
			
			switch(choice) {
				case 1: startSignUp(); break;
				case 2: login(); break;
				case 0: isRunning = false; break;
				default: print("You entered an invalid option. Try again");
			}
		} while (isRunning);
		
		print("Exiting....");
	}
	
	private static void logout() {
		print("Logging out " + currentAccount.getFullName());
		currentAccount = null;
	}
	
	private static void login() {
		print("Email/Username: ");
		scanner.nextLine();
		String emailAddress = scanner.nextLine();
		if (!emailAddress.endsWith(EmailServer.DOMAIN)) {
			emailAddress = emailAddress + EmailServer.DOMAIN;
		}
		
		print("Password: ");
		String password = scanner.nextLine();
		
		currentAccount = emailServer.login(emailAddress, password);
		
		if (currentAccount == null) {
			return;
		} else {
			showSecondaryMenu();
		}
	}
	
	private static void showSecondaryMenu() {
		boolean isRunning = true;
		
		do {
			print("Choose an option below:");
			
			print("1 - Send email");
			print("2 - Check unread");
			print("3 - Read email");
			print("4 - Delete email");
			print("0 - Logout");
			
			byte choice = scanner.nextByte();
			
			switch(choice) {
				case 1: sendEmail(); break;
				case 2: checkUnread(); break;
				case 3: readEmail(); break;
				case 0: isRunning = false; break;
				default: print("You entered an invalid option. Try again");
			}
		}while(isRunning);
		
		print("Logged out " + currentAccount.getFullName());
		logout();
	}
	
	private static void sendEmail() {
		try {
			print("Enter destinations (coma separated):");
			scanner.nextLine();

			String emailAddresses = scanner.nextLine();
			List<Account> accounts = emailServer
					.getAccountsForAddresses(emailAddresses);
			
			print("Enter the subject: ");
			String subject = scanner.nextLine();
			
			print("Enter the text: ");
			String text = scanner.nextLine();
			
			Email email = new Email(currentAccount, accounts, subject);
			email.setText(text);
			
			emailServer.sendEmail(email);
			
			print("Your email has been sent to: " + emailAddresses);
		}catch(Exception e) {
			print("Error sending email");
		}
	}
	
	private static void startSignUp() {
		print("Username: ");
		scanner.nextLine();
		String username = scanner.nextLine();
		
		print("Password: ");
		String plainPassword = scanner.nextLine();
		
		print("First name: ");
		String firstName = scanner.nextLine();
		
		print("Last name: ");
		String lastName = scanner.nextLine();
		
		print("Date of birth (optional) such as 2007-12-03: ");
		String dateOfBirthStr = scanner.nextLine();
		
		LocalDate dateOfBirth = null;
		if (dateOfBirthStr.length() > 0) {
			dateOfBirth = LocalDate.parse(dateOfBirthStr);
		}
		
		emailServer.createAccount(username, plainPassword, firstName, lastName, dateOfBirth);
	}
	
	private static void checkUnread() {
		if (currentAccount == null) {
			print("You are not logged in. Please login with an account.");
			return;
		}
		
		List<Email> unreadEmails = currentAccount.getUnreadEmails();
		
		if (unreadEmails.size() == 0) {
			print("You have no unread emails at this time");
			return;
		}
		
		print("You have " + unreadEmails.size() + " new emails!");
		
		for (Email email : unreadEmails) {
			print(email.getSubject());
			print("-----------------------------");
		}
	}

	private static void print(String text) {
		System.out.println(text);
	}
	
	private static void addSomeTestAccounts() {
		emailServer.createAccount("zcsik", "1a2B3#56", "Zsolt", "Csik", LocalDate.of(1989, 5, 19));
		emailServer.createAccount("acsik", "65#3B2a1", "Alexandru", "Csik", LocalDate.of(2017, 5, 19));
	}
	
	private static void loginToZcsik() {
		emailServer.login("zcsik@itschool.ro", "1a2B3#56");
	}
	
	private static void readEmail() {
		List<Email> emails = currentAccount.getReceivedEmails();
		
		for(int i = 0; i < emails.size(); i++) {
			Email email = emails.get(i);
			print(i + " - " + email.getSubject());
		}
		
		scanner.nextLine();
		
		int choice = scanner.nextInt();
		
		Email emailToRead = emails.get(choice);
		
		currentAccount.readEmail(emailToRead);
	}
	
	public static void cleanupOldAccounts() {
		if (currentAccount != null && currentAccount.getSessionAge() > 10) {
			logout();
		}
	}
}
