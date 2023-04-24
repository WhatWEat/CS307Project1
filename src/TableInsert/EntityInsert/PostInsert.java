package TableInsert.EntityInsert;

import Entity.Post;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class PostInsert extends BasicInfor implements Runnable {

    ArrayList<Post> posts;

    public PostInsert(String sql, ArrayList<Post> posts) {
        super(sql);
        this.posts = posts;
    }

    @Override
    public void run() {
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            try {
                Timestamp timestamp = Timestamp.valueOf(post.post_time);
                sql.setLong(1, post.post_id);
                sql.setString(2, post.title);
                sql.setString(3, post.content);
                sql.setTimestamp(4, timestamp);
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
