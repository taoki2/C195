package com.example.c195.Model;

public class User {
    private int userId;
    private String userName;
    private String password;
    /** User class constructor */
    public User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }
    /** @return the user id*/
    public int getUserId() {
        return userId;
    }
    /** @param userId the user id to set */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    /** @return the username*/
    public String getUserName() {
        return userName;
    }
    /** @param userName the username to set */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /** @return the password*/
    public String getPassword() {
        return password;
    }
    /** @param password the password to set */
    public void setPassword(String password) {
        this.password = password;
    }
    /** Override the class toString() method to display
     * the user id in the combo box */
    @Override public String toString() {
        return (String.valueOf(userId));
    }
}
