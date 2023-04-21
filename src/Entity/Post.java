package Entity;

import java.util.ArrayList;

//example
public class Post {
    public Long post_id;
    public String title;
    public String content;
    public String post_time;
    public Author author = new Author();
    public ArrayList<Author> follow = new ArrayList<>();
    public ArrayList<Author> like = new ArrayList<>();
    public ArrayList<Author> favorite = new ArrayList<>();
    public ArrayList<Author> share = new ArrayList<>();
    public ArrayList<Category> categories = new ArrayList<>();
    public City city;
    public Post() {
    }

    public Post(Long post_id, String title, String content, String post_time) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.post_time = post_time;
    }
}
