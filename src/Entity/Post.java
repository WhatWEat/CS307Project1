package Entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

//example
public class Post implements Serializable {
    public Long post_id;
    public String title;
    public String content;
    public Timestamp post_time;
    public Author author = new Author();

    public ArrayList<Author> follow = new ArrayList<>();
    public ArrayList<Author> like = new ArrayList<>();
    public ArrayList<Author> favorite = new ArrayList<>();
    public ArrayList<Author> share = new ArrayList<>();
    public ArrayList<Category> categories = new ArrayList<>();
    public ArrayList<Reply> replies = new ArrayList<>();
    public City city;
    public Post() {
    }

    public Post(Long post_id, String title, String content, String post_time) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.post_time = Timestamp.valueOf(post_time);
    }
}
