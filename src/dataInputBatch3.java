import Entity.Author;
import Entity.Category;
import Entity.City;
import Entity.Post;
import Entity.Reply;
import Entity.SubReply;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

public class dataInputBatch3 {

    private static final int BATCH_SIZE = 2000;
    private static Connection con = null;
    private static PreparedStatement stmt = null;

    public static void LOAD() {
        Properties prop = loadDBUser();
        openDB(prop);
        loadAuthor();
        loadPost();
        loadReply();
        loadPostingCity();
        loadSubReply();
        loadCategory();

        loadAuthorWritePost();
        loadAuthorLikePost();
        loadAuthorSharePost();
        loadAuthorFavoritePost();
        loadAuthorFollowPost();
        loadAuthorReply();
        loadPostCategory();
        loadPostCity();
        loadPostReply();
        loadSubReplyAuthor();

        closeDB();
        System.out.println("Finish!");
        System.out.println("Total time:" + Utility.getTotalTime() + "ms");
        System.out.println("Average speed: " + Utility.getAverageTime() + "records/s");
    }

    private static void openDB(Properties prop) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
            System.exit(1);
        }
        String url = "jdbc:postgresql://localhost:5432/Lab9";
        try {
            con = DriverManager.getConnection(url, prop);

            if (con != null) {
                System.out.println("Successfully connected to the database "
                    + prop.getProperty("database") + " as " + prop.getProperty("user"));
                con.setAutoCommit(false);
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void closeDB() {
        if (con != null) {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                con.close();
                con = null;
            } catch (Exception ignored) {
            }
        }

    }

    private static Properties loadDBUser() {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream("./src/dbUser.properties")));
            return properties;
        } catch (IOException e) {
            System.err.println("can not find db user file");
            throw new RuntimeException(e);
        }
    }

    public static void loadAuthor() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement(
                "INSERT INTO public.Author (author_id,registration_time,phone_number,name) " +
                    "VALUES (?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        while (count <= Main.authors.size()) {
            Author author = Main.authors.get(count - 1);
            try {
                Timestamp timestamp = Timestamp.valueOf(author.registerTime);
                stmt.setString(1, author.id);
                stmt.setTimestamp(2, timestamp);
                stmt.setString(3, author.phoneNumber);
                stmt.setString(4, author.name);
                stmt.executeUpdate();
                if (count % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }

        try {
            if (count % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }

        long end = System.currentTimeMillis();
        Utility.addCount(end - start, count, "Author");
    }

    public static void loadPost() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement(
                "INSERT INTO public.Post (post_id,title,content,posting_time) " +
                    "VALUES (?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        while (count <= Main.posts.size()) {
            Post post = Main.posts.get(count - 1);
            try {
                Timestamp timestamp = Timestamp.valueOf(post.post_time);
                stmt.setLong(1, post.post_id);
                stmt.setString(2, post.title);
                stmt.setString(3, post.content);
                stmt.setTimestamp(4, timestamp);
                stmt.executeUpdate();
                if (count % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }
        try {
            if (count % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, count, "Post");
    }

    public static void loadAuthorWritePost() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement("INSERT INTO public.AuthorWritePost (post_id,author_id) " +
                "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        while (count <= Main.posts.size()) {
            Post post = Main.posts.get(count - 1);
            try {
                stmt.setLong(1, post.post_id);
                stmt.setString(2, post.author.id);
                stmt.executeUpdate();
                if (count % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            count++;
        }
        try {
            if (count % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, count, "AuthorWritePost");
    }

    public static void loadAuthorLikePost() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement("INSERT INTO public.AuthorLikePost (post_id,author_id) " +
                "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        int counter = 1;
        while (count <= Main.posts.size()) {
            Post post = Main.posts.get(count - 1);
            try {
                for (int i = 0; i < post.like.size(); i++) {
                    stmt.setLong(1, post.post_id);
                    stmt.setString(2, post.like.get(i).id);
                    stmt.executeUpdate();
                    if (counter % BATCH_SIZE == 0) {
                        stmt.executeBatch();
                        stmt.clearBatch();
                    }
                    counter++;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }
        try {
            if (counter % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, counter, "AuthorLikePost");
    }

    public static void loadAuthorSharePost() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement("INSERT INTO public.AuthorSharePost (post_id,author_id) " +
                "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        int counter = 1;
        while (count <= Main.posts.size()) {
            Post post = Main.posts.get(count - 1);
            try {
                for (int i = 0; i < post.share.size(); i++) {
                    stmt.setLong(1, post.post_id);
                    stmt.setString(2, post.share.get(i).id);
                    stmt.executeUpdate();
                    if (counter % BATCH_SIZE == 0) {
                        stmt.executeBatch();
                        stmt.clearBatch();
                    }
                    counter++;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }
        try {
            if (counter % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, counter, "AuthorSharePost");
    }

    public static void loadAuthorFavoritePost() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement(
                "INSERT INTO public.AuthorFavoritePost (post_id,author_id) " +
                    "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        int counter = 1;
        while (count <= Main.posts.size()) {
            Post post = Main.posts.get(count - 1);
            try {
                for (int i = 0; i < post.favorite.size(); i++) {
                    stmt.setLong(1, post.post_id);
                    stmt.setString(2, post.favorite.get(i).id);
                    stmt.executeUpdate();
                    if (counter % BATCH_SIZE == 0) {
                        stmt.executeBatch();
                        stmt.clearBatch();
                    }
                    counter++;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }
        try {
            if (counter % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, counter, "AuthorFavoritePost");
    }

    public static void loadAuthorFollowPost() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement("INSERT INTO public.AuthorFollowPost (post_id,author_id) " +
                "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        int counter = 1;
        while (count <= Main.posts.size()) {
            Post post = Main.posts.get(count - 1);
            try {
                for (int i = 0; i < post.follow.size(); i++) {
                    stmt.setLong(1, post.post_id);
                    stmt.setString(2, post.follow.get(i).id);
                    stmt.executeUpdate();
                    if (counter % BATCH_SIZE == 0) {
                        stmt.executeBatch();
                        stmt.clearBatch();
                    }
                    counter++;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }
        try {
            if (counter % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, counter, "AuthorFollowPost");
    }

    public static void loadAuthorReply() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement("INSERT INTO public.AuthorReply (author_id,reply_id) " +
                "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        while (count <= Main.replies.size()) {
            Reply reply = Main.replies.get(count - 1);
            try {
                stmt.setString(1, reply.author.id);
                stmt.setLong(2, reply.reply_Id);
                stmt.executeUpdate();
                if (count % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }
        try {
            if (count % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, count, "AuthorReply");
    }

    public static void loadCategory() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement("INSERT INTO public.Category (category_id,category) " +
                "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        while (count <= Main.categories.size()) {
            Category category = Main.categories.get(count - 1);
            try {
                stmt.setLong(1, category.Category_ID);
                stmt.setString(2, category.category);
                stmt.executeUpdate();
                if (count % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }
        try {
            if (count % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, count, "Category");
    }

    public static void loadPostCategory() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement("INSERT INTO public.PostCategory (category_id,post_id) " +
                "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        int counter = 1;
        while (count <= Main.posts.size()) {
            Post post = Main.posts.get(count - 1);
            try {
                for (int i = 0; i < post.categories.size(); i++) {
                    stmt.setLong(1, post.categories.get(i).Category_ID);
                    stmt.setLong(2, post.post_id);
                    stmt.executeUpdate();
                    if (counter % BATCH_SIZE == 0) {
                        stmt.executeBatch();
                        stmt.clearBatch();
                    }
                    counter++;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }
        try {
            if (counter % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, counter, "PostCategory");
    }

    public static void loadPostCity() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement("INSERT INTO public.PostCity (post_id,city_id) " +
                "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        while (count <= Main.posts.size()) {
            Post post = Main.posts.get(count - 1);
            try {
                stmt.setLong(1, post.post_id);
                stmt.setLong(2, post.city.City_ID);
                stmt.executeUpdate();
                if (count % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }
        try {
            if (count % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, count, "PostCity");
    }

    public static void loadPostingCity() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement("INSERT INTO public.PostingCity (city_id,city,country) " +
                "VALUES (?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        while (count <= Main.cities.size()) {
            City city = Main.cities.get(count - 1);
            try {
                stmt.setLong(1, city.City_ID);
                stmt.setString(2, city.city);
                stmt.setString(3, city.country);
                stmt.executeUpdate();
                if (count % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }
        try {
            if (count % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, count, "PostingCity");
    }

    public static void loadPostReply() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement("INSERT INTO public.PostReply (post_id,reply_id) " +
                "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        int counter = 1;
        while (count <= Main.posts.size()) {
            Post post = Main.posts.get(count - 1);
            try {
                for (int i = 0; i < post.replies.size(); i++) {
                    stmt.setLong(1, post.post_id);
                    stmt.setLong(2, post.replies.get(i).reply_Id);
                    stmt.executeUpdate();
                    if (counter % BATCH_SIZE == 0) {
                        stmt.executeBatch();
                        stmt.clearBatch();
                    }
                    counter++;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }
        try {
            if (counter % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, counter, "PostReply");
    }

    public static void loadReply() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement("INSERT INTO public.Reply (reply_id,content,stars) " +
                "VALUES (?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        while (count <= Main.replies.size()) {
            Reply reply = Main.replies.get(count - 1);
            try {
                stmt.setLong(1, reply.reply_Id);
                stmt.setString(2, reply.content);
                stmt.setLong(3, reply.stars);
                stmt.executeUpdate();
                if (count % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }
        try {
            if (count % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, count, "Reply");
    }

    public static void loadSubReply() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement(
                "INSERT INTO public.SubReply (reply_id,sub_reply_id,content,stars) " +
                    "VALUES (?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        int counter = 1;
        while (count <= Main.replies.size()) {
            Reply reply = Main.replies.get(count - 1);
            try {
                for (int i = 0; i < reply.subReplies.size(); i++) {
                    stmt.setLong(1, reply.reply_Id);
                    stmt.setLong(2, reply.subReplies.get(i).subReply_ID);
                    stmt.setString(3, reply.subReplies.get(i).content);
                    stmt.setLong(4, reply.subReplies.get(i).stars);
                    stmt.executeUpdate();
                    if (counter % BATCH_SIZE == 0) {
                        stmt.executeBatch();
                        stmt.clearBatch();
                    }
                    counter++;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }
        try {
            if (counter % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, counter, "SubReply");
    }

    public static void loadSubReplyAuthor() {
        long start = System.currentTimeMillis();
        try {
            stmt = con.prepareStatement(
                "INSERT INTO public.SubReplyAuthor (author_id,sub_reply_id) " +
                    "VALUES (?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        int count = 1;
        while (count <= Main.subReplies.size()) {
            SubReply subReply = Main.subReplies.get(count - 1);
            try {
                stmt.setString(1, subReply.author.id);
                stmt.setLong(2, subReply.subReply_ID);
                stmt.executeUpdate();
                if (count % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            count++;
        }
        try {
            if (count % BATCH_SIZE != 1) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
        long end = System.currentTimeMillis();
        Utility.addCount(end - start, count, " SubReplyAuthor");
    }

}
