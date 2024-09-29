package com.airwin.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.airwin.model.Note;
import com.airwin.model.Patient;
import com.airwin.model.PatientGender;
import com.airwin.model.PatientHealth;
import com.airwin.noteDTO.PatientNoteDTO;
import com.airwin.repositories.NoteRepository;

@Service
public class NoteService {
    private NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note save(Note note) {
        return noteRepository.save(note);
    }
    /**
     * Find note by patient with health condition
     * @param patient
     * @return {@link PatientNoteDTO} with patient, note and health
     */
    public PatientNoteDTO findNoteByPatient(Patient patient) {
        String[] symptomes = {
                "Hémoglobine A1C",
                "Microalbumine",
                "Taille",
                "Poids",
                "Fumeur",
                " Fumeuse",
                "Anormal",
                "Cholestérol",
                "Vertiges",
                "Rechute",
                "Réaction",
                "Anticorps"
        };
        PatientNoteDTO patientNoteDTO = new PatientNoteDTO();
        List<Note> notes = noteRepository.findNoteByPatientid(patient.getPatientid());
        String allnotes = "";
        for (int i = 0; i < notes.size(); i++) {
            allnotes += notes.get(i).getNote();
        }
        int symptomecount=0;
        int age = getPatentAge(patient);
        for(int i = 0; i < symptomes.length; i++) 
            if(allnotes.contains(symptomes[i])) 
                symptomecount++;
        patientNoteDTO.setHealth(getPatientHealth(symptomecount, age, patient.getGender()));
        patientNoteDTO.setNote(notes);
        patientNoteDTO.setPatient(patient);
        return patientNoteDTO;
        }
    /**
     * Get patient health from following rules:
     * <ul>
     * <li>None: The patient’s file contains no doctor’s notes with triggers (terminology).</li>
     * <li>Borderline: The patient’s file contains between two and five triggers, and the patient is over 30 years old.</li>
     * <li>In Danger: Depends on the patient’s age and gender. If the patient is a man under 30 years old,
     * then three trigger terms must be present. If the patient is a woman under 30 years old,
     * four trigger terms are required. If the patient is over 30 years old, then six or seven 
     * trigger terms are needed.</li>
     * <li>Early onset: Again, this depends on age and gender. If the patient is a man under 30 years old, 
     * at least five trigger terms are necessary. If the patient is a woman under 30 years old, 
     * at least seven trigger terms are required. If the patient is over 30 years old, 
     * then eight or more trigger terms are needed.</li>
     * </ul>
     * @param symptomecount fond in dorctors notes
     * @param age of the patient in years
     * @param gender of the patient in enum {@link PatientGender}
     * @return {@link PatientHealth} 
     */
    public PatientHealth getPatientHealth(int  symptomecount, int age, PatientGender gender) {
        PatientHealth res=null;
        if(symptomecount<=1){
            res=PatientHealth.None;
        }else if(age>=30){
            if(symptomecount<=5){
                res=PatientHealth.Borderline;
            }else if(symptomecount<=7){
                res=PatientHealth.In_Danger;
            }else if(symptomecount>=8){
                res=PatientHealth.Early_onset;
            }
        }else if(age<30){
            if(gender==PatientGender.MALE){
                if(symptomecount==3){
                    res=PatientHealth.In_Danger;
                }else if(symptomecount>=5){
                    res=PatientHealth.Early_onset;
                }
            }else if (gender==PatientGender.FEMALE){
                if(symptomecount==4){
                    res=PatientHealth.In_Danger;
                }else if(symptomecount>=7){
                    res=PatientHealth.Early_onset;
                }
            }
        }
        return res;
    }
    /**
     * Get patient age from birthdate in years
     * @param {@link Patient}
     * @return {@code int} age
     */
    public int getPatentAge(Patient patient) {
        int res=0;
        LocalDate now = LocalDate.now();
        int year = now.getYear() - patient.getBirthdate().getYear();
        int month = now.getMonthValue() - patient.getBirthdate().getMonthValue();
        int day = now.getDayOfMonth() - patient.getBirthdate().getDayOfMonth();
        
        if(month < 0 || (month == 0 && day < 0)){
            year--;
        }
        res=year;
        return res;
    }
}
