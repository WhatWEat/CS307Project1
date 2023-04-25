package paraTableInsert.EntityTable;

import Entity.Reply;
import TableInsert.RelationInsert.AuthorReplyInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class AuthorReplyTable extends EntityTable<Reply> {

    public AuthorReplyTable(ArrayList<Reply> toInsert, String sql) {
        super(toInsert, sql);
    }

    @Override
    protected Runnable createInsertRunnable(ArrayList<Reply> block, CountDownLatch subStartSignal,
        CountDownLatch subDoneSignal) {
        return new AuthorReplyInsert(sql, block, subStartSignal, subDoneSignal);
    }
}
