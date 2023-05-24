import Entity.Author;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class dataInputProject2 {

    private static Connection con = null;
    private static PreparedStatement stmt = null;

    public static void importData() {
        openDB();
        importAuthor();
        importPost();
        importReply();
        importUserLikePost();
        importUserFavor();
        importUserLikeReply();

        closeDB();
    }
    public static void importUserLikeReply(){
        compileSQL("INSERT INTO public.userlikereply (reply_id,user_name) " +
            "VALUES (?,?);");
        for (Post post : Main.posts) {
            for(Reply reply: post.replies){
                setAuthorPost(Main.authors.subList(0, (int) reply.stars),
                    reply.reply_Id);
                for(SubReply subReply: reply.subReplies){
                    setAuthorPost(Main.authors.subList(0, (int) subReply.stars),
                        subReply.subReply_ID);
                }
            }
        }
        finishInsert();
    }
    public static void importUserFavor(){
        compileSQL("INSERT INTO public.userfavoritepost (post_id,user_name) " +
            "VALUES (?,?);");
        for (Post post : Main.posts) {
            setAuthorPost(post.favorite, post.post_id);
        }
        finishInsert();
    }
    public static void importUserLikePost() {
        compileSQL("INSERT INTO public.userlikepost (post_id,user_name) " +
            "VALUES (?,?);");
        for (Post post : Main.posts) {
            setAuthorPost(post.like, post.post_id);
        }
        finishInsert();
    }
    static void setAuthorPost(List<Author> post, Long post_id){
        for (Author author : post) {
            try {
                stmt.setLong(1, post_id);
                stmt.setString(2, author.name);
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Insert statement failed in AuthorPost");
                System.err.println(e.getMessage());
                closeDB();
                System.exit(1);
            }
        }
    }
    public static void importReply() {
        compileSQL(
            "INSERT INTO public.replies (reply_id,replying_time,content,anonymous,parent_id) " +
                "VALUES (?,?,?,?,?);");
        for (Post post : Main.posts) {
            for (Reply reply : post.replies) {
                try {
                    stmt.setLong(1, reply.reply_Id);
                    stmt.setTimestamp(2, post.post_time);
                    stmt.setString(3, reply.content);
                    stmt.setBoolean(4, false);
                    stmt.setLong(5, -1);
                    stmt.executeUpdate();
                    ArrayList<SubReply> subReplies = reply.subReplies;
                    for (SubReply subReply : subReplies) {
                        stmt.setLong(1, subReply.subReply_ID);
                        stmt.setTimestamp(2, post.post_time);
                        stmt.setString(3, subReply.content);
                        stmt.setBoolean(4, false);
                        stmt.setLong(5, reply.reply_Id);
                        stmt.executeUpdate();
                    }
                } catch (SQLException e) {
                    System.err.println("Insert statement failed in post");
                    System.err.println(e.getMessage());
                    closeDB();
                    System.exit(1);
                }
            }
        }
        finishInsert();
    }

    public static void importPost() {
        compileSQL("INSERT INTO public.posts (post_id,posting_time,title,content,shared) " +
            "VALUES (?,?,?,?,?);");
        ArrayList<Post> posts = Main.posts;
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            try {
                stmt.setLong(1, post.post_id);
                stmt.setTimestamp(2, post.post_time);
                stmt.setString(3, post.title);
                stmt.setString(4, post.content);
                stmt.setLong(5, post.shared);
                for (Author author : post.share) {
                    posts.add(
                        new Post((long) posts.size(), post.title, post.content, post.post_time,
                            author, post.post_id));
                }
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Insert statement failed in post");
                System.err.println(e.getMessage());
                closeDB();
                System.exit(1);
            }
        }
        finishInsert();
    }

    private static void importAuthor() {
        compileSQL(
            "INSERT INTO public.users (username,registration_time,phone_number,user_id,password) " +
                "VALUES (?,?,?,?,?);");

        for (Author author : Main.authors) {
            try {
                stmt.setString(1, author.name);
                stmt.setTimestamp(2, author.registerTime);
                stmt.setString(3, author.phoneNumber);
                stmt.setString(4, author.id);
                stmt.setString(5, "123456");
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Insert statement failed in author");
                System.err.println(e.getMessage());
                closeDB();
                System.exit(1);
            }
        }
        finishInsert();
    }

    private static void compileSQL(String sql) {
        try {
            stmt = con.prepareStatement(sql);
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static void finishInsert() {
        try {
            con.commit();
        } catch (SQLException e) {
            System.err.println("Commit failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static void openDB() {
        Properties prop = loadDBUser();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
            System.exit(1);
        }
        String url = "jdbc:postgresql://localhost:5432/cs307";
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
}
