package secure_notes.service;
import secure_notes.model.Note;
import secure_notes.repository.NoteRepository;

import java.sql.SQLException;
import java.util.List;

public class NoteService {
    private final NoteRepository noteRepository = new NoteRepository();

    public void createNote(String title, String content, int userId) throws SQLException {
        noteRepository.save(title, content, userId);
    }

    public List<Note> getNotesForUser(int userId) throws SQLException {
        return noteRepository.findByUser(userId);
    }

    public boolean deleteOwnNote(int noteId, int userId) throws SQLException {
        return noteRepository.deleteOwnNote(noteId, userId);
    }
}
