package com.google.sps;

import java.util.*; 

public class CandidateStats{

    public String id; 
    public String state;
    public String district;
    public String affiliation; 
    public Double conFromCandidate;
    public Double loansFromCandidate;
    public Double otherLoans;
    public Double individualCon; 
    public Double conFromPoliticalComm; 
    public Double conFromPartyComm;
    public Double totalContributions;
    
    public CandidateStats(){
    }
    public CandidateStats(String id, String state, String district, String affiliation, 
    String conFromCandidate, String loansFromCandidate, String otherLoans, String individualCon, 
    String conFromPoliticalComm, String conFromPartyComm){
        this.id = id;; 
        this.state = state;
        this.district = district;
        this.affiliation = affiliation; 
        this.conFromCandidate = Double.parseDouble(conFromCandidate);
        this.loansFromCandidate = Double.parseDouble(loansFromCandidate);
        this.otherLoans = Double.parseDouble(otherLoans);
        this.individualCon = Double.parseDouble(individualCon); 
        this.conFromPoliticalComm = Double.parseDouble(conFromPoliticalComm);
        this.conFromPartyComm = Double.parseDouble(conFromPartyComm); 
        totalContributions = this.conFromPartyComm + this.conFromPoliticalComm + this.individualCon + this.conFromCandidate;
    }
}