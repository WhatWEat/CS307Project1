package Entity;
// example of reply
public class Reply {
    public Long id;
    public String content;
    public String Stars;

    public Reply() {
    }

    public Reply(Long id, String content, String stars) {
        this.id = id;
        this.content = content;
        Stars = stars;
    }
}
