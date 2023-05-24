import Entity.Author;
import Entity.Post;
import Entity.Reply;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class dataInputProject2 {
    private static final int BATCH_SIZE = 2000;
    private static Connection con = null;
    private static PreparedStatement stmt = null;
    public static void importData(){
        openDB();
        importAuthor();
        importPost();
        importPost();
        closeDB();
    }
    public static void importReply(){
        compileSQL("INSERT INTO public.replies (reply_id,reply_time,content,stars) " +
            "VALUES (?,?,?,?);");
        for(Post post:Main.posts){
            try{
                stmt.setLong(1,post.post_id);
                stmt.setTimestamp(2,post.post_time);
                stmt.setString(3,post.title);
                stmt.setString(4,post.content);
                stmt.setLong(5,post.shared);
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
    public static void importPost(){
        compileSQL("INSERT INTO public.posts (post_id,posting_time,title,content,shared) " +
            "VALUES (?,?,?,?,?);");
        ArrayList<Post> posts = Main.posts;
        for(int i = 0;i < posts.size();i++){
            Post post = posts.get(i);
            try{
                stmt.setLong(1,post.post_id);
                stmt.setTimestamp(2,post.post_time);
                stmt.setString(3,post.title);
                stmt.setString(4,post.content);
                stmt.setLong(5,post.shared);
                for(Author author:post.share){
                    posts.add(new Post((long) posts.size(),post.title,post.content,post.post_time,
                        author,post.post_id));
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
    private static void importAuthor(){
        compileSQL("INSERT INTO public.users (username,registration_time,phone_number,user_id,password) " +
            "VALUES (?,?,?,?,?);");

        for(Author author:Main.authors){
            try{
                stmt.setString(1,author.name);
                stmt.setTimestamp(2,author.registerTime);
                stmt.setString(3,author.phoneNumber);
                stmt.setString(4,author.id);
                stmt.setString(5,"123456");
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
    private static void compileSQL(String sql){
        try {
            stmt = con.prepareStatement(sql);
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }
    private static void finishInsert(){
        try{
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
