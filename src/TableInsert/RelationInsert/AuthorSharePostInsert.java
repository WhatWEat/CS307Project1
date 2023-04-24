package TableInsert.RelationInsert;

import Entity.Post;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthorSharePostInsert extends BasicInfor implements Runnable{
        ArrayList<Post> posts;

        public AuthorSharePostInsert(String sql, ArrayList<Post> posts) {
            super(sql);
            this.posts = posts;
        }

        @Override
        public void run() {
            long counter = 0;
            for (Post post : posts) {
                try {
                    for (int j = 0; j < post.share.size(); j++) {
                        sql.setLong(1, post.post_id);
                        sql.setString(2, post.share.get(j).id);
                        sql.executeUpdate();
                        if (counter % BATCH_SIZE == 0) {
                            sql.executeBatch();
                            sql.clearBatch();
                        }
                        counter++;
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            finalCommit(counter);
        }
}
