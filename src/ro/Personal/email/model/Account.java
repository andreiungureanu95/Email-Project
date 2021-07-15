package ro.Personal.email.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import ro.Personal.email.EmailServer;

public class Account {
	
	private String username;
	
	private String firstName;
	
	private String lastName;
	
	private LocalDate dateOfBirth;
	
	private String passwordHash;
	
	private Inbox inbox;
	
	private boolean hasAlreadyLoggedIn;
	
	private LocalDateTime lastActivity;
	
	public Account(String username, 
			String firstName, 
			String lastName, 
			String passwordHash) throws Exception {
		
		if (username == null || username.isEmpty()) {
			throw new Exception("Username cannot be empty");
		}
		
		if (firstName == null || firstName.isEmpty()) {
			throw new Exception("First Name cannot be empty");
		}
		
		if (lastName == null || lastName.isEmpty()) {
			throw new Exception("Last Name cannot be empty");
		}
		
		if (passwordHash == null || passwordHash.isEmpty()) {
			throw new Exception("Password hash cannot be empty");
		}
		
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.passwordHash = passwordHash;
		
		this.inbox = new Inbox();
		this.lastActivity = LocalDateTime.now();
	}
	
	public void pushNewEmail(Email email) {
		System.out.println(this.getFullName() + " has received a new mail!");
		this.inbox.receivedEmail(email);
	}
	
	public void sendEmail(Email email) {
		this.inbox.sendEmail(email);
		this.lastActivity = LocalDateTime.now();
	}
	
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	
	public long getSessionAge() {
		return ChronoUnit.MINUTES.between(LocalDateTime.now(), this.lastActivity);
	}
	
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public boolean checkPassword(String inputPasswordHash) {
		boolean willLogin = this.passwordHash.equals(inputPasswordHash);
		
		if (willLogin) {
			this.hasAlreadyLoggedIn = true;
		}
		
		return willLogin;
	}
	
	public boolean hasAlreadyLoggedIn() {
		return this.hasAlreadyLoggedIn;
	}
	
	public String getEmailAddress() {
		return this.username + EmailServer.DOMAIN;
	}
	
	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Account)) {
			return false;
		}
		Account other = (Account) obj;
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}

	public List<Email> getUnreadEmails() {
		return this.inbox.getUnreadEmails();
	}
	
	public List<Email> getReceivedEmails() {
		return this.inbox.getReceivedEmails();
	}
	
	public void readEmail(Email email) {
		this.inbox.readEmail(email);
	}
}
