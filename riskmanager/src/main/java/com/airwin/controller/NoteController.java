package com.airwin.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import com.airwin.model.Note;
import com.airwin.model.Patient;
import com.airwin.noteDTO.PatientNoteDTO;
import com.airwin.service.NoteService;

@Controller
@RequestMapping("/note")
public class NoteController {
    private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }
@GetMapping("/get")
public ResponseEntity<PatientNoteDTO> getNotesByPatientid(@RequestBody Patient patient) {
    return ResponseEntity.ok(noteService.findNoteByPatient(patient));
}

@PostMapping("/add")
public ResponseEntity<Note> addNote(@RequestBody Note note) {
    return ResponseEntity.ok(noteService.save(note));
}

}
