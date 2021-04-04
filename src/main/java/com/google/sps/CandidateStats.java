package com.google.sps;

import java.util.*; 

public class CandidateStats{

    public String id; 
    public String state;
    public String district;
    public String affiliation; 
    public String conFromCandidate;
    public String loansFromCandidate;
    public String otherLoans;
    public String individualCon; 
    public String conFromPoliticalComm; 
    public String conFromPartyComm; 
    
    public CandidateStats(){
    }
    public CandidateStats(String id, String state, String district, String affiliation, 
    String conFromCandidate, String loansFromCandidate, String otherLoans, String individualCon, 
    String conFromPoliticalComm, String conFromPartyComm){
        this.id = id;; 
        this.state = state;
        this.district = district;
        this.affiliation = affiliation; 
        this.conFromCandidate = conFromCandidate;
        this.loansFromCandidate = loansFromCandidate;
        this.otherLoans = otherLoans;
        this.individualCon = individualCon; 
        this.conFromPoliticalComm = conFromPoliticalComm;
        this.conFromPartyComm = conFromPartyComm; 
    }
}