
package Engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import Articles.Article;

public class RecommendationEngine {

    List<Article> articlesInput = null;
    List<Article> userHistory = null;
    public static String articlesToPlainText(List<Article> articles) {
        StringBuilder plainText = new StringBuilder();
        for (Article article : articles) {
            plainText.append("Title: ").append(article.getTitle()).append("\n");
            plainText.append("Description: ").append(article.getContent()).append("\n\n");
        }
        return plainText.toString().trim(); // Remove trailing newline
    }
    public void setArticlesInput(List<Article> articlesInput) {
        this.articlesInput = articlesInput;
    }

    public List<Article> getArticlesInput() {
        return this.articlesInput;
    }

    public void setUserHistory(List<Article> userHistory) {
        this.userHistory = userHistory;
    }

    public List<Article> getUserHistory() {
        return this.userHistory;
    }
    public void Recommend() throws IOException {
        String modelName = "llama3.2";

        // Extract plain text content for the dataset and preferences
        String datasetPlainText = articlesToPlainText(articlesInput);
        String preferencesPlainText = articlesToPlainText(userHistory);

        // Construct the prompt
        String promptText = String.format(
                "Given the following dataset of articles:\n%s\n\nand the following preference articles:\n%s\n\n"
                        + "Find the top 5 articles from the dataset that have the highest similarity to the preference articles. "
                        + "Output the result in descending order of similarity, with each article's similarity score. No explanation,No note is needed, the output should only be 5 article titles with their similarity scores.",
                datasetPlainText, preferencesPlainText
        );

        // Build the JSON payload using JSONObject
        JSONObject payload = new JSONObject();
        payload.put("model", modelName);
        payload.put("prompt", promptText);
        payload.put("stream", false);

        // Setup the URL and connection
        URL url = new URL("http://localhost:11434/api/generate");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        // Send the request payload
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Check response code
        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read successful response
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                String responseText = jsonResponse.getString("response");
                System.out.println(responseText);
            }
        } else {
            // Handle non-200 responses (e.g., 400)
            try (BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorResponse.append(line);
                }
                System.out.println("Error Response: " + errorResponse);
            }
        }
    }
}

