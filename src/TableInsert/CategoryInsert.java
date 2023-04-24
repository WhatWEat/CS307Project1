package TableInsert;

import Entity.Category;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryInsert extends BasicInfor implements Runnable{
    ArrayList<Category> categories;
    public CategoryInsert(String sql,ArrayList<Category> categories) {
        super(sql);
        this.categories = categories;
    }

    @Override
    public void run() {
        for(int i = 0;i < categories.size();i++){
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
        finalCommit(categories.size());
    }
}