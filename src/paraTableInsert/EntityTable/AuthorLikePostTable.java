package paraTableInsert.EntityTable;

import Entity.Post;
import TableInsert.RelationInsert.AuthorLikePostInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class AuthorLikePostTable extends EntityTable<Post>{

    public AuthorLikePostTable(ArrayList<Post> toInsert, String sql) {
        super(toInsert, sql);
    }

    @Override
    protected Runnable createInsertRunnable(ArrayList<Post> block, CountDownLatch subStartSignal,
        CountDownLatch subDoneSignal) {
        return new AuthorLikePostInsert(sql,block,subStartSignal,subDoneSignal);
    }
}
