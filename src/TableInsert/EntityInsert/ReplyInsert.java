package TableInsert.EntityInsert;

import Entity.Reply;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ReplyInsert extends BasicInfor implements Runnable {

    ArrayList<Reply> replies;

    public ReplyInsert(String sql, ArrayList<Reply> replies, CountDownLatch startSignal,
        CountDownLatch doneSignal) {
        super(sql, startSignal, doneSignal);
        this.replies = replies;
    }

    @Override
    public void run() {
        try {
            startSignal.await();
            for (int i = 0; i < replies.size(); i++) {
                Reply reply = replies.get(i);
                try {
                    sql.setLong(1, reply.reply_Id);
                    sql.setString(2, reply.content);
                    sql.setLong(3, reply.stars);
                    sql.addBatch();
                    if (i % BATCH_SIZE == 0) {
                        sql.executeBatch();
                        sql.clearBatch();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            finalCommit(replies.size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            doneSignal.countDown();
        }

    }
}
