package com.example.c195.Model;

public class Contact {
    private int contactId;
    private String contactName;
    /** Contact class constructor */
    public Contact(int contactId, String contactName) {
        this.contactId = contactId;
        this.contactName = contactName;
    }
    /** @return the contact id */
    public int getContactId() {
        return contactId;
    }
    /** @param contactId the contact id to set */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    /** @return the contact name */
    public String getContactName() {
        return contactName;
    }
    /** @param contactName the contact name to set */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    /** Override the class toString() method to display
    * the contact name in the combo box */
    @Override public String toString() {
        return (contactName);
    }
}
