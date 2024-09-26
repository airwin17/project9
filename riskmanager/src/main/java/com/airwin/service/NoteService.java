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
        List<Note> notes = noteRepository.findNoteByPatientid(patient.getId());
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
