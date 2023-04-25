package paraTableInsert.EntityTable;

import Entity.Post;
import Entity.Reply;
import Entity.SubReply;
import TableInsert.EntityInsert.PostInsert;
import TableInsert.EntityInsert.SubReplyInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class SubReplyTable extends EntityTable<Reply>{
    public SubReplyTable(ArrayList<Reply> toInsert,String sql){
        super(toInsert,sql);
    }
    @Override
    protected Runnable createInsertRunnable(ArrayList<Reply> block, CountDownLatch subStartSignal,
        CountDownLatch subDoneSignal) {
        return new SubReplyInsert(sql,block,subStartSignal,subDoneSignal);
    }
}
