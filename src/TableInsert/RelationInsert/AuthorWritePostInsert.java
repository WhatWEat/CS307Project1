package TableInsert.RelationInsert;

import Entity.Post;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthorWritePostInsert extends BasicInfor implements Runnable{
    ArrayList<Post> posts;
    public AuthorWritePostInsert(String sql,ArrayList<Post> posts) {
        super(sql);
        this.posts = posts;
    }

    @Override
    public void run() {
        for(int i = 0;i < posts.size();i++) {
            Post post = posts.get(i);
            try {
                sql.setLong(1, post.post_id);
                sql.setString(2, post.author.id);
                sql.executeUpdate();
                if (i % BATCH_SIZE == 0) {
                    sql.executeBatch();
                    sql.clearBatch();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        finalCommit(posts.size());
    }
}
