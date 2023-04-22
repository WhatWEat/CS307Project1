package Entity;

import java.util.ArrayList;

// example of reply
public class Reply {
    public static Long id = 0L;
    public long reply_Id;
    public String content;
    public long Stars;
    public Author author;
    public ArrayList<SubReply> subReplies;
    public Reply() {
    }

    public Reply( String content, long stars, Author author) {
        reply_Id = ++id;
        this.content = content;
        this.Stars = stars;
        this.author = author;
        subReplies = new ArrayList<>();
    }
}
