package secure_notes.repository;
import secure_notes.config.DatabaseConnection;
import secure_notes.model.Note;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteRepository {
    public void save(String title, String content, int userId) throws SQLException {
        String sql = "INSERT INTO notes (title,content,user_id) VALUES (?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setInt(3, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Couldn't save note");
            e.printStackTrace();
        }
    }

    public List<Note> findByUser(int userId) throws SQLException {
        List<Note> notes = new ArrayList<>();

        String sql = "SELECT * FROM notes WHERE user_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                notes.add(new Note(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("user_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Couldn't get notes");
            e.printStackTrace();
        }
        return notes;
    }

    public boolean deleteOwnNote(int noteId, int userId) throws SQLException {
        String sql = "DELETE FROM notes WHERE id=? AND user_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, noteId);
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Couldn't delete note");
            return false;
        }
    }

        public boolean editOwnNote(int noteId, int userId, String newTitle, String newContent) throws SQLException {
            String sql = "UPDATE notes SET title=?,content=? WHERE id=? AND user_id=?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, newTitle);
                stmt.setString(2, newContent);
                stmt.setInt(3, noteId);
                stmt.setInt(4, userId);

                return stmt.executeUpdate() > 0;
            }
        }
    }
