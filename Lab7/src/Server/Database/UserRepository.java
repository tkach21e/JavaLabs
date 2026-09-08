package Server.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/** Выполняет SQL-запросы для регистрации и проверки пользователей. */
public class UserRepository {

    private final DatabaseManager databaseManager;

    public UserRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean register(String login, String password) throws SQLException {
        String sql = "INSERT INTO lab7_users(login, password_hash) VALUES (?, ?) "
                + "ON CONFLICT (login) DO NOTHING";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            statement.setString(2, hashPassword(password));
            return statement.executeUpdate() == 1;
        }
    }

    public boolean authenticate(String login, String password) throws SQLException {
        String sql = "SELECT password_hash FROM lab7_users WHERE login = ?";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next()
                        && hashPassword(password).equals(resultSet.getString(1).trim());
            }
        }
    }

    /** SHA-512 в виде строки из 128 шестнадцатеричных символов. */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("Алгоритм SHA-512 недоступен", exception);
        }
    }
}
