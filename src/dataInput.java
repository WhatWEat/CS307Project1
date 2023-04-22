import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.sql.*;

/*
0.这个文件暂时不能运行， 字段 "registration_time" 的类型为 timestamp without time zone, 但表达式的类型为 character varying
1.File -> Project Structure -> Project Settings -> Libraries。
    点击 + 按钮 -> Java
    导入postgresql-45.6.0.jar
2.txt文件读取时会跳过第一行
3.30行url更改主机和数据库名
4.setPrepareStatement()方法中更改sql语句，loadData(String line)中相应更改
5.loadDBUser()方法中更改dbUser.properties的信息
6.loadTXTFile()方法中修改txt文件的地址
7.因为存在依赖关系，clearDataInTable()方法未启用
 */
public class dataInput {
    private static Connection con = null;
    private static PreparedStatement stmt = null;
    private static void openDB(Properties prop) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
            System.exit(1);
        }
        String url = "jdbc:postgresql://localhost/project1";
        try {
            con = DriverManager.getConnection(url, prop);
            if (con != null) {
                System.out.println("Successfully connected to the database "
                        + prop.getProperty("database") + " as " + prop.getProperty("user"));
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void setPrepareStatement() {
        try {
            stmt = con.prepareStatement("INSERT INTO public.Author (author_id,registration_time,phone_number,author_name) " +
                    "VALUES (?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
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
            properties.load(new InputStreamReader(new FileInputStream("dbUser.properties")));
            return properties;
        } catch (IOException e) {
            System.err.println("can not find db user file");
            throw new RuntimeException(e);
        }
    }

    /*
    修改读取位置
     */
    private static List<String> loadTXTFile() {
        try {
            return Files.readAllLines(Path.of("Author.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadData(String line) {
        String[] lineData = line.split(";");
        if (con != null) {
            try {
                stmt.setLong(1, Long.parseLong(lineData[0]));
                stmt.setString(2, lineData[1]);
                stmt.setString(3, lineData[2]);
                stmt.setString(4, lineData[3]);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public static void clearDataInTable() {
        Statement stmt0;
        if (con != null) {
            try {
                stmt0 = con.createStatement();
                stmt0.executeUpdate("drop table Author;");
                stmt0.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS Author
                        (
                            author_id         VARCHAR(20) primary key,
                            registration_time TIMESTAMP          not null,
                            phone_number      VARCHAR(20) unique not null
                        );""");
                stmt0.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void main(String[] args) {
        Properties prop = loadDBUser();
        List<String> lines = loadTXTFile();

        // Empty target table
        openDB(prop);
//        clearDataInTable();
        closeDB();

        int cnt = 0;

        long start = System.currentTimeMillis();
        openDB(prop);
        setPrepareStatement();
        boolean isFirstIteration = true;
        for (String line : lines) {
            if (isFirstIteration) {
                isFirstIteration = false;
                continue;
            } // skip the first line
            loadData(line);//do insert command
            cnt++;
            if (cnt % 1000 == 0) {
                System.out.println("insert " + 1000 + " data successfully!");
            }
        }

        closeDB();
        long end = System.currentTimeMillis();
        System.out.println(cnt + " records successfully loaded");
        System.out.println("Loading speed : " + (cnt * 1000L) / (end - start) + " records/s");
    }


}
