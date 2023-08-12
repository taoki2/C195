package com.example.c195.Model;

public class ReportCustCount {
    private String div;
    private int count;
    /** ReportCustCount class constructor */
    public ReportCustCount(String div, int count) {
        this.div = div;
        this.count = count;
    }
    /** @return the division name*/
    public String getDiv() {
        return div;
    }
    /** @param div the division name to set */
    public void setDiv(String div) {
        this.div = div;
    }
    /** @return the customer count */
    public int getCount() {
        return count;
    }
    /** @param count the customer count to set */
    public void setCount(int count) {
        this.count = count;
    }
}
