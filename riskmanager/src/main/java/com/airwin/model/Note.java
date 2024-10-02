package com.airwin.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "PatientNote")
public class Note {
    @Id
    private String noteid;
    private String note;
    private int patientid;
    private String author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    public Note() {
    }
    public String getNoteid() {
        return noteid;
    }
    public void setNoteid(String id) {
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
    public String getAuthor() {
        return author;
    }
    public void setauthor(String author) {
        this.author = author;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
}
