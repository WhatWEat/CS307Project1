package paraTableInsert.EntityTable;

import Entity.SubReply;
import TableInsert.RelationInsert.SubReplyAuthorInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class SubReplyAuthorTable extends EntityTable<SubReply> {

    public SubReplyAuthorTable(ArrayList<SubReply> toInsert, String sql) {
        super(toInsert, sql);
    }
    @Override
    protected Runnable createInsertRunnable(ArrayList<SubReply> block,
        CountDownLatch subStartSignal, CountDownLatch subDoneSignal) {
        return new SubReplyAuthorInsert(sql, block, subStartSignal, subDoneSignal);
    }
}
