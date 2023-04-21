package Entity;
//example
public class Post {
    public Long post_id;
    public String title;
    public String content;
    public String post_time;

    public Post() {
    }

    public Post(Long post_id, String title, String content, String post_time) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.post_time = post_time;
    }
}
