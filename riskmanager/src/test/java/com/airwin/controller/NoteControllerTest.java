package com.airwin.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import com.airwin.model.Note;
import com.airwin.model.Patient;
import com.airwin.model.PatientGender;
import com.airwin.noteDTO.PatientNoteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerTest {
    @Autowired
    private MockMvc mockmvc;
    private static Note note;
    @BeforeAll
    public static void setup() {
        note=new Note();
        note.setDate(LocalDate.now());
        note.setUserid(1);
        note.setPatientid(1);
        note.setNote("ooo");
    }
    @Test
    public void testAddNote() throws Exception {
        Patient patient=new Patient();
        patient.setPatientid(1);
        patient.setGender(PatientGender.MALE);
        patient.setBirthdate(LocalDate.of(2000, 12, 24));
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        String result = mockmvc.perform(MockMvcRequestBuilders
        .get("/note/get")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(patient)))
        .andReturn().getResponse().getContentAsString();
        assertEquals("ooo", mapper.readValue(result, PatientNoteDTO.class).getNote().get(0).getNote());
    }

    @Test
    public void testGetNotesByPatientid() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        String result = mockmvc.perform(MockMvcRequestBuilders
        .post("/note/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(note)))
        .andReturn().getResponse().getContentAsString();
        
        Note note=mapper.readValue(result, Note.class);
        assertEquals(note.getNote(), "ooo");
    }
}
