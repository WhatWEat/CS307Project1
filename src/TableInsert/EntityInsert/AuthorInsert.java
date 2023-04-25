package TableInsert.EntityInsert;

import Entity.Author;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class AuthorInsert extends BasicInfor implements Runnable{
    ArrayList<Author> authors;

    public AuthorInsert(String sql, ArrayList<Author> authors, CountDownLatch startSignal, CountDownLatch doneSignal) {
        super(sql, startSignal, doneSignal);
        this.authors = authors;
    }

    @Override
    public void run() {
        try{
            startSignal.await();
            for(int i = 0;i < authors.size();i++){
                Author author = authors.get(i);
                sql.setString(1, author.id);
                sql.setTimestamp(2, author.registerTime);
                sql.setString(3, author.phoneNumber);
                sql.setString(4, author.name);
                sql.addBatch();
                if (i % BATCH_SIZE == 0) {
                    sql.executeBatch();
                    sql.clearBatch();
                }
            }
            finalCommit(authors.size());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            doneSignal.countDown();
        }
    }
}
