package DataBase;

import Articles.Article;
import Articles.ArticleManager;
import Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private static final String URL = "jdbc:sqlite:Users.db";

    // Method to connect to the database
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;

    }

    public static void initializeUserDatabase() {  // method of create Users table in database
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                Email TEXT NOT NULL UNIQUE,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                Role TEXT NOT NULL
            );
        """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) { //To execute the sql command
             stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void initializeArticleDatabase() {            // Method to create articles table in database
        String sql = """
                CREATE TABLE IF NOT EXISTS articles (
                 id INTEGER PRIMARY KEY AUTOINCREMENT,
                 title TEXT NOT NULL UNIQUE,
                 Description TEXT,
                 category TEXT,
                 Author TEXT
                 );
                """;
        try (Connection conn = connect();
             Statement stmt1 = conn.createStatement()) {  // stmt1 for creating table
            stmt1.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String countQuery = "SELECT COUNT(*) FROM articles";
        try (Connection conn = connect();
             Statement stmt2 = conn.createStatement();
             ResultSet rs = stmt2.executeQuery(countQuery)) {
            if (rs.next() && rs.getInt(1) != 25) {
                ArticleManager.addArticle();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void initializeUserPreference() {
        String sql = """
            CREATE TABLE IF NOT EXISTS user_preferences (
                  username TEXT NOT NULL,
                  article_id INTEGER NOT NULL,
                  preference INTEGER NOT NULL,
                  PRIMARY KEY (username, article_id)
            );
       """;
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Articles fetching
    public static List<Article> fetchUncategorizedArticles() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT id, title FROM articles WHERE category IS NULL";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                articles.add(new Article(id, title, null, null, null));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching articles: " + e.getMessage());
        }
        return articles;
    }
    public static void updateArticleCategory(int id, String category) {
        String sql = "UPDATE articles SET category = ? WHERE id = ?";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Article> fetchArticlesByCategory(String category, int limit, int offset) {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT * FROM articles WHERE category = ? LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category);
            stmt.setInt(2, limit);  // Limit is set to 1 to fetch one article at a time
            stmt.setInt(3, offset);  // Offset is used to navigate to the next article

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Article article = new Article(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getString("author")
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return articles;
    }
    public static List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT id, title, Description, category, Author FROM articles";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("Description");
                String category = rs.getString("category");
                String author = rs.getString("Author");

                // Create an Article object and add it to the list
                articles.add(new Article(id, title, description, category, author));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all articles: " + e.getMessage());
        }
        return articles;
    }

    public static Article getArticleById(int articleId) {
        String sql = "SELECT id, title, Description, category, Author FROM articles WHERE id = ?";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, articleId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("Description");
                String category = rs.getString("category");
                String author = rs.getString("Author");

                return new Article(id, title, content, category, author);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching article by ID: " + e.getMessage());
        }
        return null;
    }


// User managing
    public void addUser(User user) {
    String sql = "INSERT INTO users(Email,username, password, Role) VALUES(?, ?, ?, ?)";

    try (Connection conn = DatabaseHandler.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, user.getEmail());
        pstmt.setString(2, user.getUsername());
        pstmt.setString(3, user.getPassword());
        pstmt.setString(4, user.getRole());

        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}
    public static boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ? LIMIT 1";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ? LIMIT 1";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }
    public static boolean checkPassword(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public String GetUserRole(String username) {
        String sql = "SELECT Role FROM users WHERE username = ?";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String getRole = rs.getString("Role");
                    if ("admin".equalsIgnoreCase(getRole)) {
                        return "admin";
                    } else {
                        return "user";
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static List<Article> retrievePreferences(String  username, List<Article> articles) {
        String querySQL = "SELECT article_id, preference FROM User_preferences WHERE username = ?";
        List<Article> preferences = new ArrayList<>();

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(querySQL)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int articleID = rs.getInt("article_id");
                    int rating = rs.getInt("preference");

                    for (Article article : articles) {
                        if (article.getId() == articleID) {
                            article.setRating(rating);
                            preferences.add(article);
                            break;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving preferences: " + e.getMessage());
        }

        return preferences;
    }
    public static void savePreferenceToDatabase(Article article, int preferenceValue, User user) {
        String selectSql = "SELECT COUNT(*) FROM user_preferences WHERE username = ? AND article_id = ?";
        String updateSql = """
        UPDATE user_preferences
        SET preference = ?
        WHERE username = ? AND article_id = ?
    """;
        String insertSql = """
        INSERT INTO user_preferences (username, article_id, preference)
        VALUES (?, ?, ?)
    """;

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // Check if the article is already rated by the user
            selectStmt.setString(1, user.getUsername());
            selectStmt.setInt(2, article.getId());
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // If the record exists, update the preference
                updateStmt.setInt(1, preferenceValue);
                updateStmt.setString(2, user.getUsername());
                updateStmt.setInt(3, article.getId());
                updateStmt.executeUpdate();

            } else {
                // If the record does not exist, insert a new preference
                insertStmt.setString(1, user.getUsername());
                insertStmt.setInt(2, article.getId());
                insertStmt.setInt(3, preferenceValue);
                insertStmt.executeUpdate();

            }

        } catch (SQLException e) {
            System.out.println("Error saving preference: " + e.getMessage());
        }
    }

    public static boolean changeUsername(String oldUsername, String newUsername) {
        // Check if the new username already exists
        if (DatabaseHandler.usernameExists(newUsername)) {
            System.out.println("Username already exists!");
            return false;
        }

        // SQL query to update user_preferences table
        String updatePreferencesSql = "UPDATE user_preferences SET username = ? WHERE username = ?";
        String updateUserSql = "UPDATE users SET username = ? WHERE username = ?";

        try (Connection conn = DatabaseHandler.connect()) {
            conn.setAutoCommit(false);

            // Update the user_preferences table
            try (PreparedStatement pstmt = conn.prepareStatement(updatePreferencesSql)) {
                pstmt.setString(1, newUsername);
                pstmt.setString(2, oldUsername);
                pstmt.executeUpdate();
            }

            // Update the users table
            try (PreparedStatement pstmt = conn.prepareStatement(updateUserSql)) {
                pstmt.setString(1, newUsername);
                pstmt.setString(2, oldUsername);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Commit transaction if both updates succeed
                    conn.commit();
                    return true;
                } else {
                    // Rollback if users table update fails
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating username: " + e.getMessage());
            return false;
        }
    }


    public static boolean changePassword(String username, String currentPassword, String newPassword) {
        if (!DatabaseHandler.checkPassword(username, currentPassword)) {
            System.out.println("Current password is incorrect.");
            return false;
        }

        String sql = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean changeEmail(String oldEmail, String newEmail) {

        if (DatabaseHandler.emailExists(newEmail)) {
            System.out.println("Email already exists!");
            return false;
        }

        String sql = "UPDATE users SET email = ? WHERE email = ?";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set parameters for the query
            pstmt.setString(1, newEmail);
            pstmt.setString(2, oldEmail);
            int rowsAffected = pstmt.executeUpdate();

            // Check if the update was successful
            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error updating email: " + e.getMessage());
            return false;
        }
    }

    public static String getEmail(String username) {
        String sql = "SELECT email FROM users WHERE username = ?";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the username parameter
            pstmt.setString(1, username);

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            // If a result is found, return the email
            if (rs.next()) {
                return rs.getString("email");
            } else {
                System.out.println("No email found for the given username.");
                return null; // Return null if no email is found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving email: " + e.getMessage());
            return null; // Return null in case of an exception
        }
    }



}
