package TableInsert.EntityInsert;

import Entity.Category;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class CategoryInsert extends BasicInfor implements Runnable {

    ArrayList<Category> categories;

    public CategoryInsert(String sql, ArrayList<Category> categories, CountDownLatch startSignal,
        CountDownLatch doneSignal) {
        super(sql, startSignal, doneSignal);
        this.categories = categories;
    }

    @Override
    public void run() {
        try {
            startSignal.await();
            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                try {
                    sql.setLong(1, category.Category_ID);
                    sql.setString(2, category.category);
                    sql.executeUpdate();
                    if (i % BATCH_SIZE == 0) {
                        sql.executeBatch();
                        sql.clearBatch();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            doneSignal.countDown();
        }
        finalCommit(categories.size());
    }
}
