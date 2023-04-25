package paraTableInsert.EntityTable;

import Entity.Post;
import TableInsert.RelationInsert.PostCityInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class PostCategoryTable extends EntityTable<Post>{

    public PostCategoryTable(ArrayList<Post> toInsert, String sql) {
        super(toInsert, sql);
    }

    @Override
    protected Runnable createInsertRunnable(ArrayList<Post> block, CountDownLatch subStartSignal,
        CountDownLatch subDoneSignal) {
        return new PostCityInsert(sql, block, subStartSignal, subDoneSignal);
    }
}
