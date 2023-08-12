package com.example.c195.Model;
import java.time.LocalDateTime;

public class Appointment {
    private int apptId;
    private String title;
    private String desc;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private int userId;
    private int contactId;
    private String contactName;
    /** Appointment class constructor */
    public Appointment(int id, String title, String desc, String location, String type,
                       LocalDateTime start, LocalDateTime end, int customerId, int userId,
                       int contactId, String contactName) {
        this.apptId = id;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactName = contactName;
    }
    /** @return the appointment id */
    public int getApptId() {
        return apptId;
    }
    /** @param apptId the appointment id to set */
    public void setApptId(int apptId) {
        this.apptId = apptId;
    }
    /** @return the appointment title */
    public String getTitle() {
        return title;
    }
    /** @param title the appointment title to set */
    public void setTitle(String title) {
        this.title = title;
    }
    /** @return the appointment description */
    public String getDesc() {
        return desc;
    }
    /** @param desc the appointment description to set */
    public void setDesc(String desc) {
        this.desc = desc;
    }
    /** @return the appointment location */
    public String getLocation() {
        return location;
    }
    /** @param location the appointment location to set */
    public void setLocation(String location) {
        this.location = location;
    }
    /** @return the appointment type */
    public String getType() {
        return type;
    }
    /** @param type the appointment type to set */
    public void setType(String type) {
        this.type = type;
    }
    /** @return the appointment start date/time */
    public LocalDateTime getStart() {
        return start;
    }
    /** @param start the appointment start date/time to set */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }
    /** @return the appointment end date/time */
    public LocalDateTime getEnd() {
        return end;
    }
    /** @param end the appointment end date/time to set */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
    /** @return the customer id */
    public int getCustomerId() {
        return customerId;
    }
    /** @param customerId the customer id to set */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    /** @return the user id */
    public int getUserId() {
        return userId;
    }
    /** @param userId the user idto set */
    public void setUserId(int userId) {
        this.userId = userId;
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

}
