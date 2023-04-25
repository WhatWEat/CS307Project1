package paraTableInsert.EntityTable;

import Entity.Post;
import TableInsert.RelationInsert.AuthorFollowPostInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class AuthorFollowPostTable extends EntityTable<Post>{

    public AuthorFollowPostTable(ArrayList<Post> toInsert, String sql) {
        super(toInsert, sql);
    }

    @Override
    protected Runnable createInsertRunnable(ArrayList<Post> block, CountDownLatch subStartSignal,
        CountDownLatch subDoneSignal) {
        return new AuthorFollowPostInsert(sql,block,subStartSignal,subDoneSignal);
    }
}
