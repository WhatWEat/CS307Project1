package TableInsert.EntityInsert;

import Entity.Post;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class PostInsert extends BasicInfor implements Runnable {

    ArrayList<Post> posts;

    public PostInsert(String sql, ArrayList<Post> posts,CountDownLatch startSignal, CountDownLatch doneSignal) {
        super(sql,startSignal,doneSignal);
        this.posts = posts;
    }

    @Override
    public void run() {
        try {
            startSignal.await();
            for (int i = 0; i < posts.size(); i++) {
                Post post = posts.get(i);
                try {
                    sql.setLong(1, post.post_id);
                    sql.setString(2, post.title);
                    sql.setString(3, post.content);
                  
                    sql.setTimestamp(4, post.post_time);
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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            doneSignal.countDown();
        }
    }
}
