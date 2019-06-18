package com.example.wposs_user.polariscoreandroid.java;

public class TerminalHistory {

    private String term_location,term_state,date;


    public TerminalHistory(String term_location, String term_state, String date) {
       this.term_location = term_location;
        this.term_state = term_state;
        this.date = date;
    }


    public String getTerm_location() {
        return term_location;
    }

    public void setTerm_location(String term_location) {
        this.term_location = term_location;
    }

    public String getTerm_state() {
        return term_state;
    }

    public void setTerm_state(String term_state) {
        this.term_state = term_state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

