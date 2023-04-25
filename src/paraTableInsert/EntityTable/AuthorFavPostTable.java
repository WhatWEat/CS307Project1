package paraTableInsert.EntityTable;

import Entity.Post;
import TableInsert.RelationInsert.AuthorFavoritePostInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class AuthorFavPostTable extends EntityTable<Post>{

    public AuthorFavPostTable(ArrayList<Post> toInsert, String sql) {
        super(toInsert, sql);
    }

    @Override
    protected Runnable createInsertRunnable(ArrayList<Post> block, CountDownLatch subStartSignal,
        CountDownLatch subDoneSignal) {
        return new AuthorFavoritePostInsert(sql, block, subStartSignal, subDoneSignal);
    }
}
