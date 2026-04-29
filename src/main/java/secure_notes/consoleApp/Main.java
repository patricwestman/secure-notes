package secure_notes.consoleApp;
import secure_notes.ui.ConsoleMenu;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        ConsoleMenu menu = new ConsoleMenu();
        menu.start();

    }
}
