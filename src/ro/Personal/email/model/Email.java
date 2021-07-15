package ro.Personal.email.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an Email instance sent from 1 source to N destinations (N > 0)
 * @author csikz
 *
 */
public class Email {
	
	private Account source;
	
	private List<Account> destinations;
	
	private String subject;
	
	private String text;
	
	private boolean wasSent;
	
	private LocalDateTime dateTimeSent;
	
	private byte[] attachment;
	
	private boolean wasDeleted;
	
	/**
	 * Creates an instance of Email but only if you provide valid (non-null & non-empty)
	 * source, destinations and subject.
	 * 
	 * @param source
	 * @param destinations
	 * @param subject
	 * @throws Exception
	 */
	public Email(Account source, List<Account> destinations, String subject) throws Exception {
		if (source == null) {
			Exception exception = new Exception("Source cannot be null");
			throw exception;
		}
		
		if (destinations == null || destinations.isEmpty()) {
			Exception exception = new Exception("Destinations cannot be missing");
			throw exception;
		}
		
		if (subject == null || subject.isEmpty()) {
			Exception exception = new Exception("Subject cannot be empty");
			throw exception;
		}
		
		this.source = source;
		this.destinations = destinations;
		this.subject = subject;
		
		this.wasSent = false;
	}
	
	public Account getSource() {
		return this.source;
	}
	
	public List<Account> getDestinations() {
		return this.destinations;
	}
	
	/***
	 * Will check if the given destination is already present in the list of
	 * destinations. If it is, it will return.
	 * @param destination
	 */
	public void addDestination(Account destination) {
		if (this.destinations.contains(destination)) {
			return;
		}
		
		this.destinations.add(destination);
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) throws Exception {
		if (subject == null || subject.isEmpty()) {
			Exception exception = new Exception("Subject cannot be empty");
			throw exception;
		}
		
		this.subject = subject;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean wasDeleted() {
		return this.wasDeleted;
	}
	
	public void setWasDeleted(boolean wasDeleted) {
		this.wasDeleted = wasDeleted;
	}
	
	public boolean wasSent() {
		return this.wasSent;
	}
	
	/**
	 * Marks this Email instance as "sent" and ALSO: 
	 * sets this.dateTimeSent to the current date and time
	 * @param wasSent
	 */
	public void setWasSent(boolean wasSent) {
		this.wasSent = wasSent;
		
		if (wasSent) {
			this.dateTimeSent = LocalDateTime.now();
		}
	}
	
	public LocalDateTime getDateTimeSent() {
		return this.dateTimeSent;
	}
	
	public byte[] getAttachment() {
		return attachment;
	}
	
	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}
	
	public String getSourceFullName() {
		return this.source.getFullName();
	}
	
	@Override
	public String toString() {
		String representation = "From: " + this.getSourceFullName() +
				System.lineSeparator() +
				"To: ";
		
		for (Account destination : this.destinations) {
			representation = representation + destination.getFullName() + ", ";
		}
		
		// Remove the trailing ', ' characters
		representation = representation.substring(0, representation.length() - 2);
		
		representation = representation + System.lineSeparator();
		
		representation += "Subject: " + this.subject;
		
		representation += System.lineSeparator();
		
		if (this.text != null && this.text.length() > 0) {
			representation += "Text:" + this.text;
		}
		
		return representation;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Email)) {
			return false;
		}
		Email other = (Email) obj;
		if (dateTimeSent == null) {
			if (other.dateTimeSent != null) {
				return false;
			}
		} else if (!dateTimeSent.equals(other.dateTimeSent)) {
			return false;
		}
		if (source == null) {
			if (other.source != null) {
				return false;
			}
		} else if (!source.equals(other.source)) {
			return false;
		}
		if (subject == null) {
			if (other.subject != null) {
				return false;
			}
		} else if (!subject.equals(other.subject)) {
			return false;
		}
		return true;
	}
}
