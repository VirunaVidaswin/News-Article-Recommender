package Articles;

import java.sql.*;

import DataBase.DatabaseHandler;

import static DataBase.DatabaseHandler.getArticleById;

public class ArticleManager {
    // Method to initialize the articles
    public static void addArticle() {
        String sql = "INSERT INTO articles (title, Description, Author) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String[][] articles = {
                    {"10 Tips for Starting Your Own Business", "Advice and insights for aspiring entrepreneurs to build a successful startup.", "Harold Spencer"},
                    {"The Most Inspiring Female Entrepreneurs of 2024", "Celebrating the women leading successful startups in 2024.", "Amanda Singh"},
                    {"The Best Ways to Stay Productive While Working From Home", "Top strategies to remain focused and productive while working remotely.", "Catherine Smith"},
                    {"How to Launch a Successful Online Store", "Step-by-step advice for building and growing your own e-commerce business.", "Jill Williams"},
                    {"How to Start a Successful Blog in 2024", "Essential tips for launching and growing a blog in the digital age.", "Claire White"},
                    {"The Science Behind Meditation and Mindfulness", "Exploring how meditation can improve mental and physical health.", "Dr. Emily Martin"},
                    {"The Ultimate Guide to Yoga for Beginners", "A comprehensive guide to starting your yoga journey.", "Maya Patel"},
                    {"The Secret to Maintaining Healthy Skin", "Expert tips and tricks to keep your skin looking youthful and radiant.", "Lily Williams"},
                    {"How to Deal with Stress in a Healthy Way", "Practical methods for reducing stress and promoting mental wellness.", "Julie Foster"},
                    {"Top 5 Tips for Staying Fit and Healthy After 40", "Maintaining physical health and fitness as you age.", "John Carter"},
                    {"Why Space Exploration is Still Important", "A discussion on why we should continue to invest in space exploration.", "Dr. Neil Richards"},
                    {"The Fascinating World of Deep Sea Creatures", "Discovering the strange and wonderful creatures that live deep underwater.", "Dr. Lisa Cole"},
                    {"What Happens When a Star Explodes? A Look at Supernovae", "Exploring the spectacular event of a supernova and its cosmic significance.", "Dr. Patrick Lee"},
                    {"The Impact of Climate Change on Wildlife", "Understanding how climate change is affecting the animal kingdom.", "Dr. Paul Richards"},
                    {"The Science Behind Why We Dream", "What modern science tells us about the phenomenon of dreaming.", "Dr. Tom Hayes"},
                    {"The Evolution of Social Media: From MySpace to TikTok", "A history of social media and its impact on society over the years.", "Lauren King"},
                    {"The Art of Storytelling in the Digital Age", "How storytelling has evolved with the rise of digital media.", "Anna Martinez"},
                    {"The Rise of Virtual Reality in Entertainment", "How VR is transforming the entertainment industry and what’s next.", "Kevin Liu"},
                    {"The Role of Artificial Intelligence in Healthcare", "Exploring the potential of AI to revolutionize healthcare.", "Dr. Alice Patterson"},
                    {"Understanding the Basics of Blockchain Technology", "A simple guide to blockchain and its potential applications.", "David Collins"},
                    {"Top 10 Hikes You Need to Try in the U.S.", "Exploring the best hiking trails across the United States.", "Tom Harris"},
                    {"Exploring the Best National Parks in Europe", "A guide to the most beautiful and serene national parks in Europe.", "Sophie Davies"},
                    {"The Most Beautiful Beaches in the World", "A roundup of the most stunning and tranquil beaches to visit.", "Lena Adams"},
                    {"How to Travel Solo and Stay Safe", "Essential tips for solo travelers to ensure a safe and enjoyable trip.", "Nina Roberts"},
                    {"Why Traveling is Good for Your Mental Health", "How exploring new places can improve your mental wellness.", "Emma King"}
            };

            for (String[] article : articles) {
                pstmt.setString(1, article[0]); // title
                pstmt.setString(2, article[1]); // description
                pstmt.setString(3, article[2]); // author
                try {
                    pstmt.executeUpdate();
                } catch (SQLException ignored) {
                }
            }
            ArticleCategorizer.categorizeAndUpdateArticles();

        } catch (SQLException e) {
            System.out.println("\nError inserting data: " + e.getMessage());
        }
    }
    // method overloading
    public static void addArticle(Article article) {

        // If the title does not exist, proceed with the insertion
        String sql = "INSERT INTO articles(title, Description,Author) VALUES(?, ?,?)";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getAuthor());
            pstmt.executeUpdate();
            System.out.println("Article added successfully.");
            ArticleCategorizer.categorizeAndUpdateArticles();

        } catch (SQLException e) {
            System.out.println("Error adding article: " + e.getMessage());
        }
    }

    public static boolean articleExists(String title) {
        String sql = "SELECT COUNT(*) FROM articles WHERE title = ?";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // If count is greater than 0, the title exists
            }

        } catch (SQLException e) {
            System.out.println("Error checking article existence: " + e.getMessage());
        }

        return false; // Default to false if any error occurs
    }

    public static void deleteArticle(int articleId) {
        String sql = "DELETE FROM articles WHERE id = ?";

        try (Connection conn = DatabaseHandler.connect()) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, articleId);
                pstmt.executeUpdate();
                conn.commit(); // Commit the transaction
                System.out.println("Article deleted successfully.");
            } catch (SQLException e) {
                conn.rollback(); // Rollback if an error occurs
                System.out.println("Error deleting article: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error with database connection: " + e.getMessage());
        }
    }

    public static void updateArticle(Article Newarticle) {
        Article article = getArticleById(Newarticle.getId());
        if (article != null) {
            article.setTitle(Newarticle.getTitle());
            article.setContent(Newarticle.getContent());
            article.setAuthor(Newarticle.getAuthor());

            String sql = "UPDATE articles SET title = ?, Description = ?, Author = ? WHERE id = ?";
            try (Connection conn = DatabaseHandler.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, Newarticle.getTitle());
                pstmt.setString(2, Newarticle.getContent());
                pstmt.setString(3, Newarticle.getAuthor());
                pstmt.setInt(4, Newarticle.getId());
                pstmt.executeUpdate();
                System.out.println("Article updated successfully.");
                ArticleCategorizer.categorizeAndUpdateArticles();
            } catch (SQLException e) {
                System.out.println("Error updating article: " + e.getMessage());
            }
        } else {
            System.out.println("Article with ID " + Newarticle.getId() + " not found.");
        }
    }
    public static int getNextArticleId() {
        String query = "SELECT MAX(id) FROM articles";  // SQL query to get the max ID
        int nextId = 1; // Default value if there are no articles in the database
        try (Statement stmt = DatabaseHandler.connect().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                nextId = rs.getInt(1) + 1;  // Add 1 to the maximum ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nextId;
    }


    public static void displayArticles() {
        String sql = "SELECT id, title, Description FROM articles";

        try (Connection conn = DatabaseHandler.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("Description");
                System.out.println("ID: " + id + ", Title: " + title + ", Description: " + content);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}

