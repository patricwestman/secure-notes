package secure_notes.ui;
import secure_notes.model.Note;
import secure_notes.model.User;
import secure_notes.service.AuthService;
import secure_notes.service.NoteService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final NoteService noteService = new NoteService();
    private final AuthService authService = new AuthService();

    public void start() throws SQLException {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Secure Notes ===");
            System.out.println("1. Create account");
            System.out.println("2. Log in");
            System.out.println("3. Exit");
            System.out.println("4. Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> running = false;
                default -> System.out.println("Invalid choice");

            }

        }
    }
    private void register() throws SQLException {
        System.out.println("Username: ");
        String username = scanner.nextLine();

        System.out.println("Password: ");
        String password = scanner.nextLine();

        boolean success = authService.register(username, password);

        if(success) {
            System.out.println("Account created successfully!");
        } else  {
            System.out.println("No account created.");
        }
    }

    private void login() throws SQLException {
        System.out.println("Username: ");
        String username = scanner.nextLine();

        System.out.println("Password: ");
        String password = scanner.nextLine();

        User user = authService.login(username, password);

        if(user != null) {
            System.out.println("Logged in as " + user.getUsername());
            userMenu(user);
        } else {
            System.out.println("Wrong username or password! Please try again.");
        }
    }
    private void userMenu(User user) throws SQLException {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. Create note");
            System.out.println("2. Show notes");
            System.out.println("3. Log out");
            System.out.println("Choose:");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createdNote(user);
                case "2" -> showNotes(user);
                case "3" -> loggedIn = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void createdNote(User user) throws SQLException {
        System.out.println("Title: ");
        String title = scanner.nextLine();

        System.out.println("Content: ");
        String content = scanner.nextLine();

        noteService.createNote(title, content, user.getId());
        System.out.println("Note created!");
    }

    private void showNotes(User user) throws SQLException {
        List<Note> notes = noteService.getNotesForUser(user.getId());

        if(notes.isEmpty()) {
            System.out.println("You don't have any notes yet!");
            return;
        }
        System.out.println("\n=== My notes ===");

        for(Note note : notes) {
            System.out.println("--------------------");
            System.out.println("ID: " + note.getId());
            System.out.println("Title: " + note.getTitle());
            System.out.println("Content: " + note.getContent());
        }
    }
}
