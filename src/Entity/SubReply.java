package Entity;

public class SubReply {
    public static long id;
    public long SubReply_ID;
    public String content;
    public int star;
    public SubReply( String content, int star) {
        SubReply_ID = ++id;
        this.content = content;
        this.star = star;
    }
}
