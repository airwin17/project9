package com.airwin.services;
import org.springframework.stereotype.Service;


@Service
public class CalculatorService {
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
    public String getPatientHealth(int symptomecount, int age, String gender) {
        String res="None";
        if(symptomecount<=2){
            res="None";
        }else if(age>=30){
            if(symptomecount<=5){
                res="Borderline";
            }else if(symptomecount<=7){
                res="In_Danger";
            }else if(symptomecount>=8){
                res="Early_onset";
            }
        }else if(age<30){
            if(gender=="MALE"){
                if(symptomecount<=4){
                    res="In_Danger";
                }else if(symptomecount>=5){
                    res="Early_onset";
                }
            }else if (gender=="FEMALE"){
                if(symptomecount<7){
                    res="In_Danger";
                }else if(symptomecount>=7){
                    res="Early_onset";
                }
            }
        }
        return res;
    }
}
