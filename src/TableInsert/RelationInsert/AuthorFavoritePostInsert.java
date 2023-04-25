package TableInsert.RelationInsert;

import Entity.Post;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class AuthorFavoritePostInsert extends BasicInfor implements Runnable {

    ArrayList<Post> posts;

    public AuthorFavoritePostInsert(String sql, ArrayList<Post> posts, CountDownLatch startSignal,
        CountDownLatch doneSignal) {
        super(sql, startSignal, doneSignal);
        this.posts = posts;
    }

    @Override
    public void run() {
        long counter = 0;
        try {
            startSignal.await();
            for (Post post : posts) {
                try {
                    for (int j = 0; j < post.favorite.size(); j++) {
                        sql.setLong(1, post.post_id);
                        sql.setString(2, post.favorite.get(j).id);
                        sql.addBatch();
                        if (counter % BATCH_SIZE == 0) {
                            sql.executeBatch();
                            sql.clearBatch();
                        }
                        counter++;
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            finalCommit(counter);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            doneSignal.countDown();
        }

    }
}
