package paraTableInsert.EntityTable;

import Entity.Post;
import TableInsert.EntityInsert.PostInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class PostTable extends EntityTable<Post>{
    public static long recordNumber = 0;
    public PostTable(ArrayList<Post> toInsert,String sql){
        super(toInsert,sql);
    }
    @Override
    protected Runnable createInsertRunnable(ArrayList<Post> block, CountDownLatch subStartSignal,
        CountDownLatch subDoneSignal) {
        return new PostInsert(sql,block,subStartSignal,subDoneSignal);
    }
}
