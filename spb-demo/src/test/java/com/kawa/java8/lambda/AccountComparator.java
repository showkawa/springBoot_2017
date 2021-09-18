package com.kawa.java8.lambda;


public class AccountComparator {

    public int compareUserName(Account a1, Account a2) {
        return a1.getUserName().compareToIgnoreCase(a2.getUserName());
    }
}
 