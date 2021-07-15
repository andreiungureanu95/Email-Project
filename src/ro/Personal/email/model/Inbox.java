package ro.Personal.email.model;

import java.util.ArrayList;
import java.util.List;

public class Inbox {

	private List<Email> readEmails;
	private List<Email> unreadEmails;
	private List<Email> sentEmails;
	private List<Email> deletedEmails;
	
	public Inbox() {
		this.readEmails = new ArrayList<Email>();
		this.unreadEmails = new ArrayList<Email>();
		this.sentEmails = new ArrayList<Email>();
		this.deletedEmails = new ArrayList<Email>();
	}
	
	public void receivedEmail(Email email) {
		if (email == null) {
			return;
		}
		
		if (this.unreadEmails.contains(email)) {
			return;
		}
		
		this.unreadEmails.add(email);
	}
	
	public List<Email> getUnreadEmails() {
		return this.unreadEmails;
	}
	
	public List<Email> getReceivedEmails() {
		List<Email> receivedEmails = new ArrayList<Email>();
		
		receivedEmails.addAll(this.unreadEmails);
		receivedEmails.addAll(this.readEmails);
		
		return receivedEmails;
	}
	
	public void sendEmail(Email email) {
		if (email == null) {
			return;
		}
		
		if (this.sentEmails.contains(email)) {
			return ;
		}
		
		this.sentEmails.add(email);
	}
	
	public void readEmail(Email email) {
		System.out.println(email);
		
		if (this.unreadEmails.contains(email)) {
			this.unreadEmails.remove(email);
			this.readEmails.add(email);
		}
	}
	
	public void deleteEmail(Email email) {
		if (this.unreadEmails.contains(email)) {
			this.unreadEmails.remove(email);
		} else if (this.readEmails.contains(email)) {
			this.readEmails.remove(email);
		} else if (this.sentEmails.contains(email)){
			this.sentEmails.remove(email);
		}
		
		this.deletedEmails.add(email);
	}
	
	public void cleanTrash() {
		this.deletedEmails.clear();
	}
}
