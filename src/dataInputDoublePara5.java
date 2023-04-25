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
        executor.submit(new AuthorTable(Main.authors,
            "INSERT INTO public.Author (author_id,registration_time,phone_number,name) "
                + "VALUES (?,?,?,?);"));

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
