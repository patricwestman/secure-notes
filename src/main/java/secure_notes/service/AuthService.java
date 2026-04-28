package secure_notes.service;
import secure_notes.model.User;
import secure_notes.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class AuthService {
    private final UserRepository  userRepository =  new UserRepository();

    public boolean register(String username, String password) throws SQLException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        return userRepository.save(username, hashedPassword);
    }

    public User login(String username, String password) throws SQLException {
        try {
            User user = userRepository.findByUsername(username);

            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }
        } catch (Exception e) {
            System.out.println("Login failed");
        }
        return null;
    }

}
