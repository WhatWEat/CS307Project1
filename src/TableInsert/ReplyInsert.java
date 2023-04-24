package TableInsert;

import Entity.Reply;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReplyInsert extends BasicInfor implements Runnable {

    ArrayList<Reply> replies;

    public ReplyInsert(String sql, ArrayList<Reply> replies) {
        super(sql);
        this.replies = replies;
    }

    @Override
    public void run() {
        for (int i = 0; i < replies.size(); i++) {
            Reply reply = replies.get(i);
            try {
                sql.setLong(1, reply.reply_Id);
                sql.setString(2, reply.content);
                sql.setLong(3, reply.stars);
                sql.executeUpdate();
                if (i % BATCH_SIZE == 0) {
                    sql.executeBatch();
                    sql.clearBatch();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        finalCommit(replies.size());
    }
}
