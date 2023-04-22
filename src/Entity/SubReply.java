package Entity;

public class SubReply {
    public static long id;
    public long SubReply_ID;
    public String content;
    public long star;
    public Author author;
    public SubReply( String content, long star, Author author) {
        SubReply_ID = ++id;
        this.content = content;
        this.star = star;
        this.author = author;
    }
}
