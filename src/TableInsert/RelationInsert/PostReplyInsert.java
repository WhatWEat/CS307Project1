package TableInsert.RelationInsert;

import Entity.Post;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class PostReplyInsert extends BasicInfor implements Runnable {

    ArrayList<Post> posts = new ArrayList<>();

    public PostReplyInsert(String sql, ArrayList<Post> posts, CountDownLatch startSignal,
        CountDownLatch doneSignal) {
        super(sql, startSignal, doneSignal);
        this.posts = posts;
    }

    @Override
    public void run() {
        int counter = 0;
        try {
            startSignal.await();
            for (Post post : posts) {
                try {
                    for (int i = 0; i < post.replies.size(); i++) {
                        sql.setLong(1, post.post_id);
                        sql.setLong(2, post.replies.get(i).reply_Id);
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
