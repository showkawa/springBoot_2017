package com.kawa.java8.test;

public class Group {
    public String domain;
    public String trxType;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTrxType() {
        return trxType;
    }

    public void setTrxType(String trxType) {
        this.trxType = trxType;
    }

    public Group() {
    }

    public Group(String domain, String trxType) {
        this.domain = domain;
        this.trxType = trxType;
    }

    @Override
    public String toString() {
        return "{" +
                "domain:'" + domain + '\'' +
                ", trxType:'" + trxType + '\'' +
                '}';
    }
}
