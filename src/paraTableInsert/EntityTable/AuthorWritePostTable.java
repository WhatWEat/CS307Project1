package paraTableInsert.EntityTable;

import Entity.Post;
import TableInsert.RelationInsert.AuthorWritePostInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class AuthorWritePostTable extends EntityTable<Post>{

    public AuthorWritePostTable(ArrayList<Post> toInsert, String sql) {
        super(toInsert, sql);
    }

    @Override
    protected Runnable createInsertRunnable(ArrayList<Post> block, CountDownLatch subStartSignal,
        CountDownLatch subDoneSignal) {
        return new AuthorWritePostInsert(sql, block, subStartSignal, subDoneSignal);
    }
}
