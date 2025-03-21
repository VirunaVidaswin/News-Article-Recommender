package Articles;

public class Article {
    private final int id;
    private String title;
    private String content;
    private final String category;
    private String Author;
    public int Rating;

    public Article(int id, String title, String content,String category,String author) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.content = content;
        this.Author = author;
        this.Rating = 0;

    }
    // Getters and setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthor() { return Author; }
    public String getCategory() { return category; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setAuthor(String author) { this.Author = author; }

    public void setRating(int rating) {
        this.Rating = rating;
    }
    public int getRating() {
        return Rating;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + content + '\'' +
                ", category='" + category + '\'' +
                ", author='" + Author + '\'' +
                '}';
    }



}
