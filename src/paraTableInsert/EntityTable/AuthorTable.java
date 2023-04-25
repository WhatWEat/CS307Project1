package paraTableInsert.EntityTable;

import Entity.Author;
import TableInsert.BasicInfor;
import TableInsert.EntityInsert.AuthorInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import paraTableInsert.ParallelInfo;

public class AuthorTable extends EntityTable<Author>{
    public static long recordNumber = 0;
    public AuthorTable(ArrayList<Author> toInsert,String sql){
        super(toInsert,sql);
    }

    @Override
    protected Runnable createInsertRunnable(ArrayList<Author> block, CountDownLatch subStartSignal,
        CountDownLatch subDoneSignal) {
        recordNumber += block.size();
        return new AuthorInsert(sql,block,subStartSignal,subDoneSignal);
    }

}
