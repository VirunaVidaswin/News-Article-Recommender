package Model;

import Articles.Article;
import DataBase.DatabaseHandler;
import Articles.ArticleManager;

import java.util.Scanner;

public class AdminInterface {
    private static Scanner sc;
    private static User user;

    public static void setAdmin(User user) {
        AdminInterface.user = user;
    }

    public AdminInterface(Scanner sc) { // constructor to get scanner
        AdminInterface.sc = sc;
    }

    public void adminLoggedMenu() {
        while (true) {
            System.out.println("\n===============================");
            System.out.println("       Admin Dashboard        ");
            System.out.println("===============================\n");

            System.out.println("Please choose an option from the menu below:");
            System.out.println("1. Add a new article");
            System.out.println("2. Delete an article");
            System.out.println("3. Update an article");
            System.out.println("4. View all articles");
            System.out.println("5. Manage Profile");
            System.out.println("6. Log out");
            System.out.println("===============================");

            System.out.print("Enter your choice (1-5): ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> addArticle();
                case 2 -> deleteArticle();
                case 3 -> updateArticle();
                case 4 -> ArticleManager.displayArticles();
                case 5 -> userManageProfile();
                case 6 -> {
                    System.out.println("\nLogging out...\n");
                    return;
                }
                default -> System.out.println("\nInvalid option. Please try again.");
            }
        }
    }

    private void addArticle() {

        String title, content, author;

        while (true) {
            System.out.print("Enter article title: ");
            title = sc.nextLine().trim();
            if (title.isEmpty()) {
                System.out.println("Title cannot be empty. Please enter a valid title.");
                continue;
            }
            if (!title.matches(".*[a-zA-Z].*")) { // Ensures title has letters
                System.out.println("Please enter a valid title.");
                continue;
            }
            if (ArticleManager.articleExists(title)) {
                System.out.println("An article with the title '" + title + "' already exists. Please enter a different title.");
                continue;
            }
            break;
        }

        while (true) {
            System.out.print("Enter article description: ");
            content = sc.nextLine().trim();
            if (content.isEmpty()) {
                System.out.println("\nDescription cannot be empty. Please enter a valid description.");
            } else if (!content.matches(".*[a-zA-Z].*")) { // Ensures content has letters
                System.out.println("\nPlease enter a valid description.");
            } else {
                break;
            }
        }

        while (true) {
            System.out.print("Enter article author: ");
            author = sc.nextLine().trim();
            if (author.isEmpty()) {
                System.out.println("\nAuthor name cannot be empty. Please enter a valid author name.");
            } else if (!author.matches("^[a-zA-Z\\s]+$")) { // Ensures author name contains only letters and spaces
                System.out.println("\nPlease enter a valid author name.");
            } else {
                break;
            }
        }
        char choice;
        System.out.println("\n========== Article ==========");
        System.out.println("\nTitle: " + title);
        System.out.println("Description: " + content);
        System.out.println("Author: " + author);
        System.out.println("\n=============================\n");
        int newId= ArticleManager.getNextArticleId();
        String cat = null ;
        Article newArticle = new Article(newId,title,content,cat,author);
        while (true) {
            System.out.println("\nEnter Y(yes) to Add new article or N(no) to cancel");
            String input = sc.nextLine().toLowerCase();
            choice = input.charAt(0);
            if (choice == 'y' || choice == 'n'){
                break;
            }else {
                System.out.println("\nInvalid choice!!");
            }
        }
        if (choice == 'y') {
            ArticleManager.addArticle(newArticle);
        }
        else {
            System.out.println("Article not added, Operation cancelled");
        }

    }
    private void userManageProfile() {
        ManageProfile updateUser = new ManageProfile(user,sc);
        updateUser.userManageProfile();
    }

    private void deleteArticle() {
        System.out.println("\n\n");
        ArticleManager.displayArticles();
        System.out.println("\n\n");
        System.out.print("Enter article ID to delete:");
        int articleId = getIntInput();
        Article Article = DatabaseHandler.getArticleById(articleId);

        char choice;
        System.out.println("\n========== Article ==========");
        assert Article != null;
        System.out.println("\nTitle: " + Article.getTitle());
        System.out.println("Description: " + Article.getContent());
        System.out.println("Category: " + Article.getCategory());
        System.out.println("Author: " + Article.getAuthor());
        System.out.println("\n=============================\n");
        while (true) {
            System.out.println("\nEnter Y(yes) to Delete article or N(no) to cancel");
            String input = sc.nextLine().toLowerCase();
            choice = input.charAt(0);
            if (choice == 'y' || choice == 'n'){
                break;
            }else {
                System.out.println("\nInvalid choice!!");
            }
        }
        if (choice == 'y') {
            ArticleManager.deleteArticle(articleId);
        }
        else {
            System.out.println("Article not deleted, Operation cancelled");
        }
    }

    private void updateArticle() {
        System.out.println("\n\n");
        ArticleManager.displayArticles();
        System.out.println("\n\n");
        System.out.print("Enter article ID to update: ");
        int articleId = getIntInput();

        // Fetch the article by ID
        Article article = DatabaseHandler.getArticleById(articleId);
        if (article == null) {
            System.out.println("\nArticle with ID " + articleId + " not found.");
            return;
        }

        System.out.println("\n========== Article ==========");
        System.out.println("\nTitle: " + article.getTitle());
        System.out.println("Content: " + article.getContent());
        System.out.println("Author: " + article.getAuthor());
        System.out.println("\n=============================\n");

        // Update Title
        String newTitle = article.getTitle();
        System.out.print("\nDo you want to update the title? (y/n): ");
        String choice = sc.nextLine().trim().toLowerCase();
        if (choice.equals("y")) {
            while (true) {
                System.out.print("Enter new title: ");
                String inputTitle = sc.nextLine().trim();

                if (inputTitle.isEmpty()) {
                    System.out.println("\nTitle cannot be empty. Please enter a valid title.");
                    continue;
                }
                if (!inputTitle.matches(".*[a-zA-Z].*")) {
                    System.out.println("\nPlease enter a valid title with letters.");
                    continue;
                }
                if (inputTitle.equals(article.getTitle())) {
                    System.out.println("\nAn article with the title '" + inputTitle + "' already exists. Please enter a different title.");
                    continue;
                }
                newTitle = inputTitle;
                break;
            }
        } else {
            System.out.println("Current title left unchanged");
        }

        // Update Content
        String newContent = article.getContent();
        System.out.print("Do you want to update the Description? (y/n): ");
        choice = sc.nextLine().trim().toLowerCase();
        if (choice.equals("y")) {
            System.out.print("Enter new content: ");
            String inputContent = sc.nextLine().trim();
            if (!inputContent.isEmpty()) {
                newContent = inputContent;
            } else {
                System.out.println("\nDescription cannot be empty. Keeping current content.");
            }
        } else {
            System.out.println("Current description left unchanged");
        }
        // Update Author
        String newAuthor = article.getAuthor();
        System.out.print("Do you want to update the author? (y/n): ");
        choice = sc.nextLine().trim().toLowerCase();
        if (choice.equals("y")) {
            System.out.print("Enter new author: ");
            String inputAuthor = sc.nextLine().trim();
            if (!inputAuthor.isEmpty()) {
                newAuthor = inputAuthor;
            } else {
                System.out.println("\nAuthor cannot be empty. Keeping current author.");
            }
        } else {
            System.out.println("Keeping current author.");
        }
        char option;
        article.setTitle(newTitle);
        article.setContent(newContent);
        article.setAuthor(newAuthor);
        System.out.println("\n========== Updated Article ==========");
        System.out.println("\nTitle: " + article.getTitle());
        System.out.println("Description: " + article.getContent());
        System.out.println("Author: " + article.getAuthor());
        System.out.println("\n=============================\n");
        while (true) {
            System.out.println("\nEnter Y(yes) to update article or N(no) to cancel");
            String input = sc.nextLine().toLowerCase();
            option = input.charAt(0);
            if (option == 'y' || option == 'n'){
                break;
            }else {
                System.out.println("\nInvalid choice!!");
            }
        }
        if (option == 'y') {
            ArticleManager.updateArticle(article);
        }
        else {
            System.out.println("Article not updated, Operation cancelled");
        }

    }

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("\nInvalid input. Please enter a valid integer: ");
            }
        }
    }
}