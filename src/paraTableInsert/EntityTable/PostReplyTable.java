package paraTableInsert.EntityTable;

import Entity.Post;
import Entity.Reply;
import TableInsert.RelationInsert.PostReplyInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class PostReplyTable extends EntityTable<Post>{

    public PostReplyTable(ArrayList<Post> toInsert, String sql) {
        super(toInsert, sql);
    }

    @Override
    protected Runnable createInsertRunnable(ArrayList<Post> block, CountDownLatch subStartSignal,
        CountDownLatch subDoneSignal) {
        return new PostReplyInsert(sql, block, subStartSignal, subDoneSignal);
    }
}
