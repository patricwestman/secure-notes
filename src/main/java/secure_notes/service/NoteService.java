package secure_notes.service;
import secure_notes.config.DatabaseConnection;
import secure_notes.model.Note;
import secure_notes.repository.NoteRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public boolean editOwnNote(int noteId, String title, String content, int userId) throws SQLException {
        return noteRepository.editOwnNote(noteId, userId, title, content);
    }

    public List<Note> findAllNotes() throws SQLException {
        List<Note> notes = new ArrayList<>();

        String sql = "SELECT * FROM notes";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                notes.add(new Note(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("user_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Couldn't get all notes from database");
        }
        return notes;
    }

    public List<Note> getAllNotes() throws SQLException {
        return noteRepository.findAllNotes();
    }

    public boolean deleteAnyNote(int noteId) throws SQLException {
        String sql = "DELETE FROM notes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, noteId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Couldn't delete note from database");
            return false;
        }
    }
}
