package com.kawa.java8.lambda;


public class Account {
    private String userName;
    private String bankName;
    private String year;
    private String vipLevel;

    public Account() {
    }

    public Account(String userName, String bankName, String year, String vipLevel) {
        this.userName = userName;
        this.bankName = bankName;
        this.year = year;
        this.vipLevel = vipLevel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }

    public static int compareYear(Account a1, Account a2) {
        return a1.getYear().compareToIgnoreCase(a2.getYear());
    }

    public int compareUserName(Account a2) {
        return this.getUserName().compareToIgnoreCase(a2.getUserName());
    }
}
