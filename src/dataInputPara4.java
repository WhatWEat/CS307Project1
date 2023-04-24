import Entity.Author;
import TableInsert.AuthorInsert;
import TableInsert.BasicInfor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
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
    private static final int BLOCK_SIZE = 5000;
    private static Connection con = null;

    public static void LOAD() {
        Properties prop = loadDBUser();
        openDB(prop);
        BasicInfor.con = con;

        loadData(Main.authors, "INSERT INTO public.Author (author_id,registration_time,phone_number,name) " +
                "VALUES (?,?,?,?);");

        closeDB();
        System.out.println("Finish!");
        System.out.println("Total time:" + Utility.getTotalTime() + "ms");
        System.out.println("Average speed: " + Utility.getAverageTime() + "records/s");
    }

    public static <T> void loadData(ArrayList<T> toInsert, String sql) {
        ArrayList<ArrayList<T>> blocks = BasicInfor.splitRecords(toInsert, BLOCK_SIZE);
        ExecutorService executor = Executors.newFixedThreadPool(blocks.size());
        T t = toInsert.get(0);
        long count = 0;
        for(ArrayList<T> block : blocks){
            if(t instanceof Author){
                count += block.size();
                executor.submit(new AuthorInsert(sql,(ArrayList<Author>) block));
            }
        }
        long start = System.currentTimeMillis();
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            //con.commit();
        } catch (InterruptedException e) {
            // Handle interruption
        }


        long end = System.currentTimeMillis();
        Utility.addCount(end - start, count, "Author");
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
