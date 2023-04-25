package TableInsert.RelationInsert;

import Entity.Post;
import TableInsert.BasicInfor;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class PostCategoryInsert extends BasicInfor implements Runnable {

    ArrayList<Post> posts = new ArrayList<>();

    public PostCategoryInsert(String sql, ArrayList<Post> posts, CountDownLatch startSignal,
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
                    for (int j = 0; j < post.categories.size(); j++) {
                        sql.setLong(1, post.categories.get(j).Category_ID);
                        sql.setLong(2, post.post_id);
                        sql.executeUpdate();
                        if (counter % BATCH_SIZE == 0) {
                            sql.executeBatch();
                            sql.clearBatch();
                        }
                        counter++;
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            doneSignal.countDown();
        }
        finalCommit(counter);
    }
}
