package com.airwin.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PatientNote")
public class Note {
    @Id
    private int noteid;
    private String note;
    private int patientid;
    private int userid;
    private LocalDate date;
    public Note() {
    }
    public int getId() {
        return noteid;
    }
    public void setId(int id) {
        this.noteid = id;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public int getPatientid() {
        return patientid;
    }
    public void setPatientid(int patientid) {
        this.patientid = patientid;
    }
    public int getUserid() {
        return userid;
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
}
