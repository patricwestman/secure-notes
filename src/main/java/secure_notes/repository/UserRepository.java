package secure_notes.repository;
import java.sql.*;

import secure_notes.config.DatabaseConnection;
import secure_notes.model.User;
import secure_notes.model.Role;

public class UserRepository {

    public boolean save(String username, String hashedPassword) throws SQLException {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'USER')";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();

            return true;

            } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("Username already exists, please try again!");
            } else {
                System.out.println("Couldn't register user");
            }
            return false;
        }
    }
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role"))
                );
            }
        } catch (SQLException e) {
            System.out.println("Couldn't find user");
        }
        return null;
    }

    public boolean updatePassword(int userId, String newHashedPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE id = ?";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, newHashedPassword);
        stmt.setInt(2, userId);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
            System.out.println("Couldn't update password");
            return false;
        }

    }
}
