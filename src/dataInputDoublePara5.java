import Entity.Post;
import TableInsert.BasicInfor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import paraTableInsert.EntityTable.AuthorFavPostTable;
import paraTableInsert.EntityTable.AuthorFollowPostTable;
import paraTableInsert.EntityTable.AuthorLikePostTable;
import paraTableInsert.EntityTable.AuthorReplyTable;
import paraTableInsert.EntityTable.AuthorSharePostTable;
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
import paraTableInsert.EntityTable.AuthorTable;

public class dataInputDoublePara5 {

    private static final int BLOCK_SIZE = 5000;
    private static Connection con = null;
    private static final CountDownLatch startSignal = new CountDownLatch(1);
    private static final CountDownLatch doneSignal = new CountDownLatch(ParallelInfo.totalTable);

    public static void LOAD() {
        Properties prop = loadDBUser();
        openDB(prop);
        ParallelInfo.setCon(con);
        ParallelInfo.setSignal(startSignal, doneSignal);

        allocateThread();

        closeDB();
        System.out.println("Finish!");
        System.out.println("Total time:" + Utility.getTotalTime() + "ms");
        System.out.println("Average speed: " + Utility.getAverageTime() + "records/s");
    }

    private static void allocateThread() {
        ExecutorService executor = Executors.newFixedThreadPool(ParallelInfo.parallel);
        // Entity
        executor.submit(new AuthorTable(Main.authors, BasicInfor.loadAuthor));
        executor.submit(new PostTable(Main.posts, BasicInfor.loadPost));
        executor.submit(new CategoryTable(Main.categories,BasicInfor.loadCategory));
        executor.submit(new CityTable(Main.cities, BasicInfor.loadCity));
        executor.submit(new ReplyTable(Main.replies, BasicInfor.loadReply));
        //Relation
        executor.submit(new AuthorWritePostTable(Main.posts,BasicInfor.loadAuthorWritePost));
        executor.submit(new AuthorLikePostTable(Main.posts,BasicInfor.loadAuthorLikePost));
        executor.submit(new AuthorSharePostTable(Main.posts,BasicInfor.loadAuthorSharePost));
        executor.submit(new AuthorFavPostTable(Main.posts,BasicInfor.loadAuthorFavoritePost));
        executor.submit(new AuthorFollowPostTable(Main.posts,BasicInfor.loadAuthorFollowPost));
        executor.submit(new PostCategoryTable(Main.posts,BasicInfor.loadPostCategory));
        executor.submit(new PostCityTable(Main.posts,BasicInfor.loadPostCity));
        executor.submit(new PostReplyTable(Main.posts,BasicInfor.loadPostReply));
        executor.submit(new AuthorReplyTable(Main.replies,BasicInfor.loadAuthorReply));
        executor.submit(new SubReplyTable(Main.replies,BasicInfor.loadSubReply));
        executor.submit(new SubReplyAuthorTable(Main.subReplies,BasicInfor.loadSubReplyAuthor));
        try {
            Thread.sleep(500);
            System.out.println("ALL Task Submit");
            long start = System.currentTimeMillis();
            startSignal.countDown();

            doneSignal.await();
            long end = System.currentTimeMillis();
            executor.shutdown();
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
