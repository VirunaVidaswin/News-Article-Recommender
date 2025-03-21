package Articles;

import DataBase.DatabaseHandler;
import opennlp.tools.tokenize.SimpleTokenizer;
import java.util.*;


public class ArticleCategorizer {
    private static final Map<String, List<String>> CATEGORY_KEYWORDS = new HashMap<>() {{
        put("Business", Arrays.asList(
                "business", "startup", "entrepreneurs", "productive", "work from home", "e-commerce",
                "online store", "blog", "marketing", "finance", "investment", "revenue", "leadership", "growth",
                "strategy", "profit", "sales", "branding", "successful"
        ));
        put("Health", Arrays.asList(
                "health", "wellness", "fitness", "diet", "nutrition", "exercise", "mental health", "meditation",
                "mindfulness", "yoga", "skin care", "stress", "self-care", "therapy", "rehabilitation",
                "weight loss", "sleep", "well-being", "lifestyle", "hygiene", "healthy"
        ));
        put("Science", Arrays.asList(
                "science", "space", "exploration", "astronomy", "biology", "physics", "chemistry", "ecology",
                "environment", "genetics", "ocean", "deep sea", "creatures", "climate change", "wildlife",
                "ecosystem", "conservation", "research", "experiment", "innovation", "star"
        ));
        put("Technology", Arrays.asList(
                "technology", "tech", "digital", "software", "hardware", "blockchain", "AI", "artificial", "intelligence",
                "machine learning", "neural network", "internet", "gadgets", "social", "virtual", "reality",
                "augmented reality", "cybersecurity", "innovation", "automation", "programming", "computing", "media"
        ));
        put("Travel", Arrays.asList(
                "travel", "tourism", "explore", "destination", "solo travel", "safety", "backpacking", "adventure",
                "hikes", "beaches", "mountain", "national", "park", "city", "tour", "getaway", "road", "trip", "cultural sites",
                "vacation", "relaxation", "exploring", "mental health", "travel guide", "local"
        ));
    }};
    private static final List<String> Defaults = Arrays.asList("the", "is", "at", "which", "on", "a", "an", "of", "and", "in");

    // Methods to categorize an article newly added
    public static void categorizeAndUpdateArticles() {
        List<Article> articles = DatabaseHandler.fetchUncategorizedArticles();

        for (Article article : articles) {
            String category = categorizeArticle(article.getTitle());
            DatabaseHandler.updateArticleCategory(article.getId(), category);
        }

    }   //  Method to determine the categories of each article

 // method to extract  articles without a set category

    private static String categorizeArticle(String title) {
        String title1 = title.toLowerCase();
        List<String> tokens = extractKeywords(title1);
        Map<String, Integer> categoryScores = new HashMap<>();

        // Looping through all categories and counts keyword matches for each
        for (Map.Entry<String, List<String>> entry : CATEGORY_KEYWORDS.entrySet()) {
            String category = entry.getKey();
            List<String> keywords = entry.getValue();
            int score = 0;
            // Count the matches of each keyword from its category
            for (String keyword : keywords) {
                if (tokens.contains(keyword)) {
                    score++;
                }
            }
            if (score > 0) {
                categoryScores.put(category, score);
            }
        }
        if (categoryScores.isEmpty()) {
            return null;
        }
        return categoryScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();

    }    // method to categorize each article by its title ,returns the category

    private static List<String> extractKeywords(String content) {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(content.toLowerCase());
        List<String> processedTokens = new ArrayList<>();
        for (String token : tokens) {
            if (!Defaults.contains(token)) {
                processedTokens.add(token);
            }
        }
        return processedTokens;
    }   // Takes the title and breaks the keywords in the title




}
