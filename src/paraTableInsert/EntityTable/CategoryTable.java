package paraTableInsert.EntityTable;

import Entity.Author;
import Entity.Category;
import TableInsert.BasicInfor;
import TableInsert.EntityInsert.AuthorInsert;
import TableInsert.EntityInsert.CategoryInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import paraTableInsert.ParallelInfo;

public class CategoryTable extends EntityTable<Category>{
    public static long recordNumber = 0;
    public CategoryTable(ArrayList<Category> toInsert,String sql){
        super(toInsert,sql);
    }

    @Override
    protected Runnable createInsertRunnable(ArrayList<Category> block,
        CountDownLatch subStartSignal, CountDownLatch subDoneSignal) {
        recordNumber += block.size();
        return new CategoryInsert(sql,block,subStartSignal,subDoneSignal);
    }

}
