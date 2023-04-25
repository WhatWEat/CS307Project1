package Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

// example of reply
public class Reply implements Serializable {
    public static Long id = 0L;
    public long reply_Id;
    public String content;
    public long stars;
    public Author author;
    public ArrayList<SubReply> subReplies;
    public Reply() {
    }

    public Reply( String content, long stars, Author author) {
        reply_Id = ++id;
        this.content = content;
        this.stars = stars;
        this.author = author;
        subReplies = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reply reply)) {
            return false;
        }
        return reply_Id == reply.reply_Id && stars == reply.stars && Objects.equals(content,
            reply.content) && Objects.equals(author, reply.author)
            && Objects.equals(subReplies, reply.subReplies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reply_Id, content, stars, author, subReplies);
    }
}
