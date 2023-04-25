package paraTableInsert.EntityTable;

import Entity.Post;
import Entity.Reply;
import TableInsert.EntityInsert.PostInsert;
import TableInsert.EntityInsert.ReplyInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ReplyTable extends EntityTable<Reply>{
    public ReplyTable(ArrayList<Reply> toInsert,String sql){
        super(toInsert,sql);
    }
    @Override
    protected Runnable createInsertRunnable(ArrayList<Reply> block, CountDownLatch subStartSignal,
        CountDownLatch subDoneSignal) {
        return new ReplyInsert(sql,block,subStartSignal,subDoneSignal);
    }

}
