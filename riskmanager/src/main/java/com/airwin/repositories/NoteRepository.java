package com.airwin.repositories;
import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.airwin.model.Note;
@RepositoryDefinition(domainClass = Note.class, idClass = String.class)
public interface NoteRepository {
    List<Note> findNoteByPatientid(int patientId);
    Note save(Note note);
}
