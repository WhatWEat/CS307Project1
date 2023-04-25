package paraTableInsert.EntityTable;

import Entity.Post;
import TableInsert.RelationInsert.AuthorSharePostInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class AuthorSharePostTable extends EntityTable<Post>{

    public AuthorSharePostTable(ArrayList<Post> toInsert, String sql) {
        super(toInsert, sql);
    }

    @Override
    protected Runnable createInsertRunnable(ArrayList<Post> block, CountDownLatch subStartSignal,
        CountDownLatch subDoneSignal) {
        return new AuthorSharePostInsert(sql, block, subStartSignal, subDoneSignal);
    }
}
