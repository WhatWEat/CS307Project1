import Entity.Author;
import Entity.Category;
import Entity.City;
import Entity.Post;
import Entity.Reply;
import Entity.SubReply;
import TableInsert.BasicInfor;
import TableInsert.EntityInsert.AuthorInsert;
import TableInsert.EntityInsert.CategoryInsert;
import TableInsert.EntityInsert.CityInsert;
import TableInsert.EntityInsert.PostInsert;
import TableInsert.EntityInsert.ReplyInsert;
import TableInsert.EntityInsert.SubReplyInsert;
import TableInsert.RelationInsert.AuthorFavoritePostInsert;
import TableInsert.RelationInsert.AuthorFollowPostInsert;
import TableInsert.RelationInsert.AuthorLikePostInsert;
import TableInsert.RelationInsert.AuthorReplyInsert;
import TableInsert.RelationInsert.AuthorSharePostInsert;
import TableInsert.RelationInsert.AuthorWritePostInsert;
import TableInsert.RelationInsert.PostCategoryInsert;
import TableInsert.RelationInsert.PostCityInsert;
import TableInsert.RelationInsert.PostReplyInsert;
import TableInsert.RelationInsert.SubReplyAuthorInsert;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import paraTableInsert.EntityTable.AuthorFavPostTable;
import paraTableInsert.EntityTable.AuthorFollowPostTable;
import paraTableInsert.EntityTable.AuthorLikePostTable;
import paraTableInsert.EntityTable.AuthorReplyTable;
import paraTableInsert.EntityTable.AuthorSharePostTable;
import paraTableInsert.EntityTable.AuthorTable;
import paraTableInsert.EntityTable.AuthorWritePostTable;
import paraTableInsert.EntityTable.CategoryTable;
import paraTableInsert.EntityTable.CityTable;
import paraTableInsert.EntityTable.PostCategoryTable;
import paraTableInsert.EntityTable.PostCityTable;
import paraTableInsert.EntityTable.PostReplyTable;
import paraTableInsert.EntityTable.PostTable;
import paraTableInsert.EntityTable.ReplyTable;
import paraTableInsert.EntityTable.SubReplyAuthorTable;
import paraTableInsert.EntityTable.SubReplyTable;
import paraTableInsert.ParallelInfo;

public class dataInputDoublePara5 {

    private static Connection con = null;
    private static CountDownLatch startSignal;
    private static CountDownLatch doneSignal;

    public static void LOAD() {
        Properties prop = loadDBUser();
        openDB(prop);
        ParallelInfo.setCon(con);
        long totalTime = allocateThread();

        closeDB();
        System.out.println("Finish!");
        System.out.println("Total time:" + totalTime + "ms");
//        System.out.println("Average speed: " + Utility.getAverageTime() + "records/s");
    }

    private static long allocateThread() {
        ExecutorService executor = Executors.newFixedThreadPool(ParallelInfo.parallel);
        // Entity
        startSignal = new CountDownLatch(1);
        doneSignal = new CountDownLatch(ParallelInfo.totalTable);
        ParallelInfo.setSignal(startSignal, doneSignal);
        executor.submit(new AuthorTable(Main.authors, BasicInfor.loadAuthor));
        executor.submit(new PostTable(Main.posts, BasicInfor.loadPost));
        executor.submit(new CategoryTable(Main.categories, BasicInfor.loadCategory));
        executor.submit(new CityTable(Main.cities, BasicInfor.loadCity));
        executor.submit(new ReplyTable(Main.replies, BasicInfor.loadReply));
        executor.submit(new SubReplyTable(Main.replies, BasicInfor.loadSubReply));
        long t = getTime("Entity", executor);
//        //Relation
        startSignal = new CountDownLatch(1);
        doneSignal = new CountDownLatch(12 - ParallelInfo.totalTable);
        ParallelInfo.setSignal(startSignal, doneSignal);
        executor.submit(new AuthorWritePostTable(Main.posts, BasicInfor.loadAuthorWritePost));
        executor.submit(new AuthorLikePostTable(Main.posts, BasicInfor.loadAuthorLikePost));
        executor.submit(new AuthorSharePostTable(Main.posts, BasicInfor.loadAuthorSharePost));
        executor.submit(new AuthorFavPostTable(Main.posts, BasicInfor.loadAuthorFavoritePost));
        executor.submit(new AuthorFollowPostTable(Main.posts, BasicInfor.loadAuthorFollowPost));
        executor.submit(new PostCategoryTable(Main.posts, BasicInfor.loadPostCategory));
        executor.submit(new PostCityTable(Main.posts, BasicInfor.loadPostCity));
        executor.submit(new PostReplyTable(Main.posts, BasicInfor.loadPostReply));
        executor.submit(new AuthorReplyTable(Main.replies, BasicInfor.loadAuthorReply));
        executor.submit(new SubReplyAuthorTable(Main.subReplies, BasicInfor.loadSubReplyAuthor));
        t += getTime("Relation", executor);
        long start = System.currentTimeMillis();
        try {
            //计算编译各个sql的时间
            PreparedStatement stmt = con.prepareStatement(BasicInfor.loadReply);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadAuthor);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadPost);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadCategory);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadCity);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadSubReply);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadAuthorWritePost);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadAuthorLikePost);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadAuthorSharePost);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadAuthorFavoritePost);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadAuthorFollowPost);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadPostCategory);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadPostCity);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadPostReply);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadAuthorReply);stmt.close();
            stmt = con.prepareStatement(BasicInfor.loadSubReplyAuthor);stmt.close();
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        t += (System.currentTimeMillis() - start);
        return t;
    }

    public static long getTime(String taskName, ExecutorService executor) {
        try {
            Thread.sleep(500);
            System.out.println("Task Submit");
            long start = System.currentTimeMillis();
            startSignal.countDown();
            System.out.println(taskName + "start");

            doneSignal.await();
            System.out.println("executor shutdown");
            long end = System.currentTimeMillis();
            System.out.println("执行" + taskName + "总用时" + (end - start));
            return end - start;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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

                Statement stmt = con.createStatement();
                stmt.executeUpdate("ALTER TABLE subreply DISABLE TRIGGER ALL");
                System.out.println("Foreign key constraint disabled successfully.");

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
                Statement stmt = con.createStatement();
                stmt.executeUpdate("ALTER TABLE subreply DISABLE TRIGGER ALL");
                con.commit();
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
}
