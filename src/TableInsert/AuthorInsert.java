package TableInsert;

import Entity.Author;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class AuthorInsert extends BasicInfor implements Runnable{
    ArrayList<Author> authors;

    public AuthorInsert(String sql, ArrayList<Author> authors) {
        super(sql);
        this.authors = authors;
    }

    @Override
    public void run() {
        try{
            for(int i = 0;i < authors.size();i++){
                Author author = authors.get(i);
                Timestamp timestamp = Timestamp.valueOf(author.registerTime);
                sql.setString(1, author.id);
                sql.setTimestamp(2, timestamp);
                sql.setString(3, author.phoneNumber);
                sql.setString(4, author.name);
                sql.executeUpdate();
                if (i % BATCH_SIZE == 0) {
                    sql.executeBatch();
                    sql.clearBatch();
                }
            }
            if(authors.size() % BATCH_SIZE != 0){
                sql.executeBatch();
                sql.clearBatch();
            }
            con.commit();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
