package TableInsert.EntityInsert;

import Entity.Reply;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class SubReplyInsert extends BasicInfor implements Runnable {

    ArrayList<Reply> replies;

    public SubReplyInsert(String sql, ArrayList<Reply> replies, CountDownLatch startSignal,
        CountDownLatch doneSignal) {
        super(sql, startSignal, doneSignal);
        this.replies = replies;
    }

    @Override
    public void run() {
        try {
            startSignal.await();
            int counter = 0;
            for (Reply reply : replies) {
                try {
                    for (int i = 0; i < reply.subReplies.size(); i++) {
                        sql.setLong(1, reply.reply_Id);
                        sql.setLong(2, reply.subReplies.get(i).subReply_ID);
                        sql.setString(3, reply.subReplies.get(i).content);
                        sql.setLong(4, reply.subReplies.get(i).stars);
                        sql.executeUpdate();
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
