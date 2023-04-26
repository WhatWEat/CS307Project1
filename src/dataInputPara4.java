import Entity.Author;
import Entity.Category;
import Entity.City;
import Entity.Post;
import Entity.Reply;
import Entity.SubReply;
import TableInsert.EntityInsert.SubReplyInsert;
import TableInsert.RelationInsert.AuthorFavoritePostInsert;
import TableInsert.EntityInsert.AuthorInsert;
import TableInsert.RelationInsert.AuthorFollowPostInsert;
import TableInsert.RelationInsert.AuthorLikePostInsert;
import TableInsert.RelationInsert.AuthorReplyInsert;
import TableInsert.RelationInsert.AuthorSharePostInsert;
import TableInsert.RelationInsert.AuthorWritePostInsert;
import TableInsert.BasicInfor;
import TableInsert.EntityInsert.CategoryInsert;
import TableInsert.EntityInsert.CityInsert;
import TableInsert.EntityInsert.PostInsert;
import TableInsert.EntityInsert.ReplyInsert;
import TableInsert.RelationInsert.PostCategoryInsert;
import TableInsert.RelationInsert.PostCityInsert;
import TableInsert.RelationInsert.PostReplyInsert;
import TableInsert.RelationInsert.SubReplyAuthorInsert;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class dataInputPara4 {

    /*
    Basic
    Total time:1242ms
    Average speed: 7373.590982286634records/s

    Total time:1204ms
    Average speed: 7605.481727574751records/s
    * */
    private static final int BLOCK_COUNT = 4;
    private static Connection con = null;

    public static void LOAD() {
        Properties prop = loadDBUser();
        openDB(prop);
        BasicInfor.con = con;
        /*entity*/
        loadData(Main.authors, "Author",BasicInfor.loadAuthor);

        loadData(Main.posts, "Post",BasicInfor.loadPost);

        loadData(Main.categories, "Category",BasicInfor.loadCategory);

        loadData(Main.cities, "City", BasicInfor.loadCity);

        loadData(Main.replies, "Reply", BasicInfor.loadReply);
        /*Post Relation*/
        loadData(Main.posts, "AuthorWritePost",BasicInfor.loadAuthorWritePost);

        loadData(Main.posts, "AuthorLikePost",BasicInfor.loadAuthorLikePost);

        loadData(Main.posts, "AuthorSharePost",BasicInfor.loadAuthorSharePost);

        loadData(Main.posts, "AuthorFavoritePost",BasicInfor.loadAuthorFavoritePost);

        loadData(Main.posts, "AuthorFollowPost",BasicInfor.loadAuthorFollowPost);

        loadData(Main.posts, "PostCategory",BasicInfor.loadPostCategory);

        loadData(Main.posts, "PostCity",BasicInfor.loadPostCity);

        loadData(Main.posts, "PostReply",BasicInfor.loadPostReply);
        /*Author Relation*/
        loadData(Main.replies, "AuthorReply",BasicInfor.loadAuthorReply);
        /* SubReply*/
        loadData(Main.replies, "SubReply",BasicInfor.loadSubReply);

        loadData(Main.subReplies, "SubReplyAuthor",BasicInfor.loadSubReplyAuthor);
        closeDB();
        System.out.println("Finish!");
        System.out.println("Total time:" + Utility.getTotalTime() + "ms");
        System.out.println("Average speed: " + Utility.getAverageTime() + "records/s");
    }

    public static <T> void loadData(ArrayList<T> toInsert, String tableName, String sql) {
        int size = 0;
        if (toInsert.size() % BLOCK_COUNT == 0) {
            size = toInsert.size() / BLOCK_COUNT;
        } else {
            size = toInsert.size() / BLOCK_COUNT + 1;
        }
        ArrayList<ArrayList<T>> blocks = BasicInfor.splitRecords(toInsert, size);
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(blocks.size());
        ExecutorService executor = Executors.newFixedThreadPool(blocks.size());
        long count = 0;
        for (ArrayList<T> block : blocks) {
            switch (tableName) {
                case "Author" -> {
                    count += block.size();
                    executor.submit(
                        new AuthorInsert(sql, (ArrayList<Author>) block, startSignal, doneSignal));
                }
                case "Post" -> {
                    count += block.size();
                    executor.submit(
                        new PostInsert(sql, (ArrayList<Post>) block, startSignal, doneSignal));
                }
                case "Category" -> {
                    count += block.size();
                    executor.submit(
                        new CategoryInsert(sql, (ArrayList<Category>) block, startSignal,
                            doneSignal));
                }
                case "City" -> {
                    count += block.size();
                    executor.submit(
                        new CityInsert(sql, (ArrayList<City>) block, startSignal, doneSignal));
                }
                case "Reply" -> {
                    count += block.size();
                    executor.submit(
                        new ReplyInsert(sql, (ArrayList<Reply>) block, startSignal, doneSignal));
                }
                case "AuthorWritePost" -> {
                    count += block.size();
                    executor.submit(
                        new AuthorWritePostInsert(sql, (ArrayList<Post>) block, startSignal,
                            doneSignal));
                }
                case "AuthorLikePost" -> {
                    ArrayList<Post> posts = (ArrayList<Post>) block;
                    for (Post i : posts) {
                        count += i.like.size();
                    }
                    executor.submit(new AuthorLikePostInsert(sql, posts, startSignal, doneSignal));
                }
                case "AuthorSharePost" -> {
                    ArrayList<Post> posts = (ArrayList<Post>) block;
                    for (Post i : posts) {
                        count += i.share.size();
                    }
                    executor.submit(new AuthorSharePostInsert(sql, posts, startSignal, doneSignal));
                }
                case "AuthorFavoritePost" -> {
                    ArrayList<Post> posts = (ArrayList<Post>) block;
                    for (Post i : posts) {
                        count += i.favorite.size();
                    }
                    executor.submit(
                        new AuthorFavoritePostInsert(sql, posts, startSignal, doneSignal));
                }
                case "AuthorFollowPost" -> {
                    ArrayList<Post> posts = (ArrayList<Post>) block;
                    for (Post i : posts) {
                        count += i.follow.size();
                    }
                    executor.submit(
                        new AuthorFollowPostInsert(sql, posts, startSignal, doneSignal));
                }
                case "AuthorReply" -> {
                    count += block.size();
                    executor.submit(
                        new AuthorReplyInsert(sql, (ArrayList<Reply>) block, startSignal,
                            doneSignal));
                }
                case "PostCategory" -> {
                    ArrayList<Post> posts = (ArrayList<Post>) block;
                    for (Post i : posts) {
                        count += i.categories.size();
                    }
                    executor.submit(new PostCategoryInsert(sql, posts, startSignal, doneSignal));
                }
                case "PostCity" -> {
                    count += block.size();
                    executor.submit(
                        new PostCityInsert(sql, (ArrayList<Post>) block, startSignal, doneSignal));
                }
                case "PostReply" -> {
                    ArrayList<Post> posts = (ArrayList<Post>) block;
                    for (Post i : posts) {
                        count += i.replies.size();
                    }
                    executor.submit(new PostReplyInsert(sql, posts, startSignal, doneSignal));
                }
                case "SubReply" -> {
                    ArrayList<Reply> replies = (ArrayList<Reply>) block;
                    for (Reply i : replies) {
                        count += i.subReplies.size();
                    }
                    executor.submit(new SubReplyInsert(sql, replies, startSignal, doneSignal));
                }
                case "SubReplyAuthor" -> {
                    count += block.size();
                    executor.submit(
                        new SubReplyAuthorInsert(sql, (ArrayList<SubReply>) block, startSignal,
                            doneSignal));
                }
            }
        }
        long start = 0, end = 0;
        try {
            System.out.println("All tasks submitted! Waiting for command to start...");
            Thread.sleep(500); // 模拟等待500ms
            //开始计时
            start = System.currentTimeMillis();
            startSignal.countDown();
            //PreparedStatement ps = con.prepareStatement(sql);//计算预编译SQL的时间
            doneSignal.await();
            //计时结束
            end = System.currentTimeMillis();
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // Handle interruption
        }
        System.out.println(tableName + "时间" + (end - start));
        Utility.addCount(end - start, count, tableName);
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
