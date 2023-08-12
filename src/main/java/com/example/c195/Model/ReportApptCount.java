package com.example.c195.Model;

import java.util.Hashtable;

public class ReportApptCount {
    private String month;
    private String type;
    private int count;
    public static Hashtable<Integer, String> monthHash = new Hashtable<>();

    /** ReportApptCount class constructor */
    public ReportApptCount(String month, String type, int count) {
        this.month = month;
        this.type = type;
        this.count = count;
    }
    /** @return the appointment month */
    public String getMonth() {
        return month;
    }
    /** @param month the appointment month to set */
    public void setMonth(String month) {
        this.month = month;
    }
    /** @return the appointment type */
    public String getType() {
        return type;
    }
    /** @param type the appointment type to set */
    public void setType(String type) {
        this.type = type;
    }
    /** @return the appointment count */
    public int getCount() {
        return count;
    }
    /** @param count the appointment count to set */
    public void setCount(int count) {
        this.count = count;
    }
    /** adds the month key/value pairs to the hashtable - monthHash */
    public static void getMonthHT() {
        monthHash.clear();
        monthHash.put(1, "January");
        monthHash.put(2, "February");
        monthHash.put(3, "March");
        monthHash.put(4, "April");
        monthHash.put(5, "May");
        monthHash.put(6, "June");
        monthHash.put(7, "July");
        monthHash.put(8, "August");
        monthHash.put(9, "September");
        monthHash.put(10, "October");
        monthHash.put(11, "November");
        monthHash.put(12, "December");
    }
}
