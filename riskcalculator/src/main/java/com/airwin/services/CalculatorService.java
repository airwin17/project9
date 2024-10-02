package com.airwin.services;
import org.springframework.stereotype.Service;


@Service
public class CalculatorService {
    
    public String getPatientHealth(int symptomecount, int age, String gender) {
        String res=null;
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
                if(symptomecount==3){
                    res="In_Danger";
                }else if(symptomecount>=5){
                    res="Early_onset";
                }
            }else if (gender=="FEMALE"){
                if(symptomecount==4){
                    res="In_Danger";
                }else if(symptomecount>=7){
                    res="Early_onset";
                }
            }
        }
        return res;
    }
}
