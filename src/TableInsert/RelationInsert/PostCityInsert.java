package TableInsert.RelationInsert;

import Entity.Post;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class PostCityInsert extends BasicInfor implements Runnable {

    ArrayList<Post> posts;

    public PostCityInsert(String sql, ArrayList<Post> posts, CountDownLatch startSignal,
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
                    sql.setLong(2, post.city.City_ID);
                    sql.addBatch();
                    if (i % BATCH_SIZE == 0) {
                        sql.executeBatch();
                        sql.clearBatch();
                    }
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    System.err.println(ex.getNextException().getMessage());
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
