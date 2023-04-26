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
                    sql.addBatch();
                    if (i % BATCH_SIZE == 0) {
                        sql.executeBatch();
                        sql.clearBatch();
                    }
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    System.out.println(ex.getNextException().getMessage());
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
