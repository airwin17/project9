package com.airwin.noteDTO;

import java.util.List;

import com.airwin.model.Note;
import com.airwin.model.Patient;
import com.airwin.model.PatientHealth;

public class PatientNoteDTO {
    private Patient patient;
    private List<Note> notes;
    private PatientHealth health;
    
    public PatientNoteDTO() {
    }
    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patientid) {
        this.patient = patientid;
    }
    public List<Note> getNotes() {
        return notes;
    }
    public void setNotes(List<Note> note) {
        this.notes = note;
    }
    public PatientHealth getHealth() {
        return health;
    }
    public void setHealth(PatientHealth health) {
        this.health = health;
    }
    
}
