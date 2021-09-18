package com.kawa.java8.test;

public class Req {

    public String domain;
    public String trxType;
    public String stateName;


    public Req() {
    }

    public Req(String domain, String trxType, String stateName) {
        this.domain = domain;
        this.trxType = trxType;
        this.stateName = stateName;
    }

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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return "{" +
                "domain:'" + domain + '\'' +
                ", trxType:'" + trxType + '\'' +
                ", stateName:'" + stateName + '\'' +
                '}';
    }
}
