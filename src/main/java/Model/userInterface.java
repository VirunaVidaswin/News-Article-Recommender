package Model;


import Articles.Article;
import DataBase.DatabaseHandler;
import Engine.RecommendationEngine;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class userInterface {
    private static Scanner sc;
    private static User user;

    public static void setUser(User user) {
        userInterface.user = user;
    }

    public userInterface(Scanner sc) {
        userInterface.sc = sc;
    }
    public void userLoggedMenu() throws IOException {
        while (true) {
            System.out.println("\n===============================");
            System.out.println("        User Dashboard        ");
            System.out.println("===============================\n");

            // Display available actions
            System.out.println("Please choose an option from the menu below:");
            System.out.println("1. View Article");
            System.out.println("2. Get Recommendations");
            System.out.println("3. Manage profile");
            System.out.println("4. Log out");
            System.out.println("===============================");

            System.out.print("Enter your choice (1-4): ");
            int choice = getIntInput();
            switch (choice) {
                case 1 -> viewArticle();
                case 2 -> getRecommendations();
                case 3 -> userManageProfile();
                case 4 -> {
                    System.out.println("\nLogging out...\n");
                    return;
                }
                default -> System.out.println("\nInvalid option. Please try again.");
            }
        }
    }

    private void userManageProfile() {
        ManageProfile updateUser = new ManageProfile(user,sc);
        updateUser.userManageProfile();
    }

    private void viewArticle() {
        System.out.println("\nArticle categories:");
        System.out.println("1. Business, 2. Technology, 3. Science");
        System.out.println("4. Health, 5. Travel");
        System.out.println("\nSelect an article category to view:");
        int choice = getIntInput();

        switch (choice) {
            case 1 -> viewArticlesByCategory("Business",user,sc);
            case 2 -> viewArticlesByCategory("Technology",user,sc);
            case 3 -> viewArticlesByCategory("Science",user,sc);
            case 4 -> viewArticlesByCategory("Health",user,sc);
            case 5 -> viewArticlesByCategory("Travel",user,sc);
            default -> System.out.println("\nInvalid option. Please try again.");
        }

    }
    public static void viewArticlesByCategory(String category, User user, Scanner sc) {
        int currentPage = 0;
        int limit = 1;
        boolean moreArticles = true;

        while (moreArticles) {
            List<Article> articles = DatabaseHandler.fetchArticlesByCategory(category, limit, currentPage * limit);

            if (articles.isEmpty()) {
                System.out.println("No more articles in this category.");
                break;  // Exit the loop if no articles are found
            }

            Article article = articles.get(0);

            System.out.println("\n========== Article ==========");
            System.out.println("\nTitle: " + article.getTitle());
            System.out.println("Description: " + article.getContent());
            System.out.println("Author: " + article.getAuthor());
            System.out.println("\n=============================\n");


            boolean navigating = true;
            while (navigating) {
                System.out.println("Options:");
                System.out.println("1. Next article,        2. Previous article");
                System.out.println("3. Rate this article,   4. Exit");
                System.out.println("Enter your choice:");

                int navigationChoice;
                try {
                    navigationChoice = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 4.");
                    continue;
                }

                switch (navigationChoice) {
                    case 1:
                        currentPage++;
                        navigating = false;
                        break;
                    case 2:
                        if (currentPage > 0) {
                            currentPage--;
                            navigating = false;
                        } else {
                            System.out.println("You are already at the first article.");
                        }
                        break;
                    case 3:
                        rateArticle(article, user, sc);
                        navigating = false;
                        break;
                    case 4:
                        System.out.println("Exiting article view...");
                        navigating = false;  // Exit navigation loop
                        moreArticles = false;  // Stop showing more articles
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                        break;
                }
            }
        }
    }
    public static void rateArticle(Article article, User user, Scanner sc) {


        System.out.println("\nRate this article:");
        System.out.println("1. Like");
        System.out.println("2. Dislike");
        System.out.println("3. Exit");
        System.out.print("Enter your choice (1-3): ");

        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
            String preference;


            switch (choice) {
                case 1:
                    preference = "like";
                    System.out.println("Thank you! You liked this article.");
                    addUserPreference(article ,preference,user);

                    break;
                case 2:
                    preference = "dislike";
                    System.out.println("Thank you! You disliked this article.");
                    addUserPreference( article, preference,user);

                    break;
                case 3:
                    preference = "neutral";
                    System.out.println("Exited rating ");
                    addUserPreference(article, preference,user);

                    break;
                default:
                    System.out.println("Invalid input. Please enter a number between 1 and 3.");
                    break;
            }


        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number between 1 and 3.");

        }

    }

    public static void addUserPreference(Article article, String preference, User user) {
        int preferenceValue = 0;
        if (preference.equalsIgnoreCase("like")) {
            preferenceValue = 1;
        } else if (preference.equalsIgnoreCase("dislike")) {
            preferenceValue = -1;
        }

        DatabaseHandler.savePreferenceToDatabase(article, preferenceValue,user);

    }
    private void getRecommendations() throws IOException {
        List<Article> userHistory = null;
        List<Article> allArticles = DatabaseHandler.getAllArticles();
        System.out.println("\n====================Recommended Articles Top 5 ====================");
        System.out.println("Recommendation is generating please wait.......... ");

        userHistory = DatabaseHandler.retrievePreferences(user.getUsername(), allArticles);
        allArticles.removeAll(userHistory);

        RecommendationEngine model = new RecommendationEngine();
        model.setArticlesInput(allArticles);
        model.setUserHistory(userHistory);
        model.Recommend();




    }

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid integer: ");
            }
        }
    }
}