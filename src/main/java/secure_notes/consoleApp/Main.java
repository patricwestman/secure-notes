package secure_notes.consoleApp;
import secure_notes.config.DatabaseConnection;
import java.sql.Connection;
public class Main {
    public static void main(String[] args) {

        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Successfully connected to database.");
        } catch (Exception e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
    }
}
