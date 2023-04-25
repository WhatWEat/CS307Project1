package TableInsert.RelationInsert;

import Entity.SubReply;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.util.ArrayList;

public class SubReplyAuthorInsert extends BasicInfor implements Runnable{
    ArrayList<SubReply> subReplies;

    public SubReplyAuthorInsert(String sql, ArrayList<SubReply> subReplies) {
        super(sql);
        this.subReplies = subReplies;
    }

    @Override
    public void run() {
        for(int i = 0;i < subReplies.size();i++){
            SubReply subReply = subReplies.get(i);
            try {
                sql.setString(1, subReply.author.id);
                sql.setLong(2, subReply.subReply_ID);
                sql.executeUpdate();
                if (i % BATCH_SIZE == 0) {
                    sql.executeBatch();
                    sql.clearBatch();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        finalCommit(subReplies.size());
    }
}
