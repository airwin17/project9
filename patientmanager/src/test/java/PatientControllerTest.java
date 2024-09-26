
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.airwin.PatientmanagerApplication;
import com.airwin.model.Patient;
import com.airwin.model.PatientGender;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@AutoConfigureMockMvc
@SpringBootTest(classes = PatientmanagerApplication.class)

public class PatientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private static Patient patient;
    @BeforeAll
    public static void setup() {
        patient=new Patient();
        patient.setFirstname("John");
        patient.setLastname("Doe");
        patient.setGender(PatientGender.MALE);
        patient.setBirthdate(LocalDate.parse("2000-01-01"));
        patient.setPhone("1234567890");
        patient.setZipcode("12345");
    }
    @Test
    public void testSave() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mockMvc.perform(MockMvcRequestBuilders.post("/addPatient")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andDo(result->{
                patient.setPatientid(Integer.valueOf(result.getResponse().getContentAsString()));
                });
                
    }
    
    
    @Test
    public void testUpdate() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mockMvc.perform(MockMvcRequestBuilders.put("/updatePatient")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(content().string("Patient updated successfully"));
    }
    
    @Test
    public void testDelete() throws Exception {
        testSave();
        mockMvc.perform(MockMvcRequestBuilders.delete("/deletePatient?id="+patient.getPatientid().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Patient deleted successfully"));
    }
    
    @Test
    public void testFindByNames() throws Exception {
        
        testSave();
        mockMvc.perform(MockMvcRequestBuilders.get("/findPatientByNames?firstname="+patient.getFirstname()+"&lastname="+patient.getLastname()))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testFindAll() throws Exception {
        testSave();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/findAllPatients"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
                LinkedList<Patient> patients = mapper.readValue(result, new TypeReference<LinkedList<Patient>>() {});
                    assertTrue(patients.size()>0);
    }
}
