package secure_notes.ui;
import secure_notes.model.Note;
import secure_notes.model.User;
import secure_notes.model.Role;
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

        if (success) {
            System.out.println("Account created successfully!");
        } else {
            System.out.println("No account created.");
        }
    }

    private void login() throws SQLException {
        System.out.println("Username: ");
        String username = scanner.nextLine();

        System.out.println("Password: ");
        String password = scanner.nextLine();

        User user = authService.login(username, password);

        if (user != null) {
            System.out.println("Logged in as " + user.getUsername());

            if (user.getRole() == Role.ADMIN) {
                adminMenu(user);
            } else  {
                userMenu(user);
            }
        } else {
            System.out.println("Wrong username or password! Please try again.");
        }
    }

    private void adminMenu(User user) throws SQLException {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Create note");
            System.out.println("2. Show notes");
            System.out.println("3. Edit note");
            System.out.println("4. Delete note");
            System.out.println("5. Show all users notes");
            System.out.println("6. Delete any user note");
            System.out.println("7. Change password");
            System.out.println("8. Log out");
            System.out.println("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createdNote(user);
                case "2" -> showNotes(user);
                case "3" -> editOwnNote(user);
                case "4" -> deleteOwnNote(user);
                case "5" -> showAllNotes();
                case "6" -> deleteAnyNote();
                case "7" -> {
                    boolean passwordChanged = changePassword(user);

                    if (passwordChanged) {
                        loggedIn = false;
                    }
                }
                case "8" -> loggedIn = false;
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void userMenu(User user) throws SQLException {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n=== User Menu ===");
            System.out.println("1. Create note");
            System.out.println("2. Show notes");
            System.out.println("3. Edit note");
            System.out.println("4. Delete note");
            System.out.println("5. Change password");
            System.out.println("6. Log out");
            System.out.println("Choose:");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createdNote(user);
                case "2" -> showNotes(user);
                case "3" -> editOwnNote(user);
                case "4" -> deleteOwnNote(user);
                case "5" -> {
                    boolean passwordChanged = changePassword(user);

                    if (passwordChanged) {
                        loggedIn = false;
                    }
                }
                case "6" -> loggedIn = false;
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

        if (notes.isEmpty()) {
            System.out.println("You don't have any notes yet!");
            return;
        }
        System.out.println("\n=== My notes ===");

        for (Note note : notes) {
            System.out.println("--------------------");
            System.out.println("ID: " + note.getId());
            System.out.println("Title: " + note.getTitle());
            System.out.println("Content: " + note.getContent());
        }
    }

    private boolean changePassword(User user) throws SQLException {
        System.out.println("Enter your current password: ");
        String oldPassword = scanner.nextLine();

        System.out.println("Enter new password: ");
        String newPassword = scanner.nextLine();

        boolean success = authService.changePassword(user, oldPassword, newPassword);

        if (success) {
            System.out.println("Password changed successfully!");
            System.out.println("You have been logged out. Please log in again.");
            return true;
        } else {
            System.out.println("Password change failed! Please try again.");
            return false;
        }
    }

    private void deleteOwnNote(User user) throws SQLException {
        showNotes(user);

        System.out.println("Which note would you like to delete? (Enter note id): ");
        String deleteOwnNote = scanner.nextLine();

        try {
            int noteId = Integer.parseInt(deleteOwnNote);

            System.out.println("Are you sure you want to delete this note? (y/n): ");
            String confirm = scanner.nextLine();

            if(!confirm.equalsIgnoreCase("y")) {
                System.out.println("Operation aborted!");
                return;
            }

            boolean deleted = noteService.deleteOwnNote(noteId, user.getId());

            if (deleted) {
                System.out.println("Note deleted successfully!");
            } else {
                System.out.println("Note could not be deleted! Make sure note ID is correct and try again.");
            }
        } catch (NumberFormatException | SQLException e) {
            System.out.println("Invalid note ID!");
        }

    }

        private void editOwnNote(User user) throws SQLException {
            showNotes(user);

            System.out.println("Which note would you like to edit? (Enter note id): ");
            String editOwnNote = scanner.nextLine();

            try {
                int noteId = Integer.parseInt(editOwnNote);

                System.out.println("Are you sure  you want to edit this note? (y/n): ");
                String confirmEdit = scanner.nextLine();

                if(!confirmEdit.equalsIgnoreCase("y")) {
                    System.out.println("Operation aborted!");
                    return;
                }

                System.out.println("Enter new title: ");
                String newTitle = scanner.nextLine();

                System.out.println("Enter new content: ");
                String newContent = scanner.nextLine();

                boolean edited = noteService.editOwnNote(
                        noteId,
                        newTitle,
                        newContent,
                        user.getId()
                );

                if(edited) {
                    System.out.println("Note updated successfully!");
                } else {
                    System.out.println("Note could not be edited! Make sure note ID is correct.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid note ID!");
            } catch (SQLException e) {
                System.out.println("Error while trying to edit note!");
            }
        }

        private void showAllNotes() throws SQLException {
            List<Note> notes = noteService.getAllNotes();

            if(notes.isEmpty()) {
                System.out.println("There are no notes in the system.");
                return;
            }

            System.out.println("\n=== All notes ===");

            for (Note note : notes) {
                System.out.println("------------------------------");
                System.out.println("ID: " + note.getId());
                System.out.println("User ID: " + note.getUserId());
                System.out.println("Title: " + note.getTitle());
                System.out.println("Content: " + note.getContent());
            }
        }

        private void deleteAnyNote() throws SQLException {
            showAllNotes();

            System.out.println("Which note would you like to delete? (Enter note id): ");
            String deleteAnyNote = scanner.nextLine();

            try {
                int noteId = Integer.parseInt(deleteAnyNote);

                System.out.println("Are you sure you want to delete this note? (y/n): ");
                String confirm = scanner.nextLine();

                if(!confirm.equalsIgnoreCase("y")) {
                    System.out.println("Operation aborted!");
                    return;
                }

                boolean deletedNote = noteService.deleteAnyNote(noteId);

                if(deletedNote) {
                    System.out.println("Note deleted successfully!");
                } else {
                    System.out.println("Note could not be deleted. Check if the note ID exists");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid note ID! Please enter a valid number.");
            }
        }
    }



