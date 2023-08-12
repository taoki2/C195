package com.example.c195.Model;

public class Customer {
    private int custId;
    private String name;
    private String address;
    private String phone;
    private String divName;
    private String country;
    private String postal;
    /** Customer class constructor */
    public Customer(int custId, String name, String address, String postal,
                    String phone, String divName, String country) {
        this.custId = custId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.divName = divName;
        this.country = country;
        this.postal = postal;
    }
    /** @return the customer id */
    public int getCustId() {
        return custId;
    }
    /** @param custId the contact id to set */
    public void setCustId(int custId) {
        this.custId = custId;
    }
    /** @return the customer name */
    public String getName() {
        return name;
    }
    /** @param name the contact name to set */
    public void setName(String name) {
        this.name = name;
    }
    /** @return the address */
    public String getAddress() {
        return address;
    }
    /** @param address the address to set */
    public void setAddress(String address) {
        this.address = address;
    }
    /** @return the phone number */
    public String getPhone() {
        return phone;
    }
    /** @param phone the phone number to set */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /** @return the division */
    public String getDivName() {
        return divName;
    }
    /** @param divName the division to set */
    public void setDivName(String divName) {
        this.divName = divName;
    }
    /** @return the country */
    public String getCountry() {
        return country;
    }
    /** @param country the country to set */
    public void setCountry(String country) {
        this.country = country;
    }
    /** @return the postal code */
    public String getPostal() {
        return postal;
    }
    /** @param postal the postal code to set */
    public void setPostal(String postal) {
        this.postal = postal;
    }
    /** Override the class toString() method to display
     * the customer id in the combo box */
    @Override public String toString() {
        return (String.valueOf(custId));
    }
}
