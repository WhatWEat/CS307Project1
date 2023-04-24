package TableInsert.RelationInsert;

import Entity.Reply;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthorReplyInsert extends BasicInfor implements Runnable {
    ArrayList<Reply> replies;
    public AuthorReplyInsert(String sql, ArrayList<Reply> replies) {
        super(sql);
        this.replies = replies;
    }

    @Override
    public void run() {
        for(int i = 0;i < replies.size();i++){
            Reply reply = replies.get(i);
            try {
                sql.setString(1, reply.author.id);
                sql.setLong(2, reply.reply_Id);
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
