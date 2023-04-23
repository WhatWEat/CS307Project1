package Entity;

public class SubReply {
    public static long id;
    public long subReply_ID;
    public String content;
    public long stars;
    public Author author;
    public SubReply( String content, long stars, Author author) {
        subReply_ID = ++id;
        this.content = content;
        this.stars = stars;
        this.author = author;
    }
}
