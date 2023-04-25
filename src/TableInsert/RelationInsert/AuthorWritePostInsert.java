package TableInsert.RelationInsert;

import Entity.Post;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class AuthorWritePostInsert extends BasicInfor implements Runnable {

    ArrayList<Post> posts;

    public AuthorWritePostInsert(String sql, ArrayList<Post> posts, CountDownLatch startSignal,
        CountDownLatch doneSignal) {
        super(sql, startSignal, doneSignal);
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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            doneSignal.countDown();
        }
        finalCommit(posts.size());
    }
}