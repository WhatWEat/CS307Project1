package TableInsert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class BasicInfor {
    public static Connection con = null;
    public PreparedStatement sql = null;
    static final int BATCH_SIZE = 1000;

    public BasicInfor(Connection con,String sql) {
        if(BasicInfor.con == null) BasicInfor.con = con;
        try {
            this.sql = con.prepareStatement(sql);
        } catch (SQLException e) {
            System.err.println("create SQL sentence failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    public BasicInfor(String sql) {
        try {
            this.sql = con.prepareStatement(sql);
        } catch (SQLException e) {
            System.err.println("create SQL sentence failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    static void closeDB(){
        try{
            if(con != null) con.close();
        } catch (SQLException e) {
            System.err.println("close DB failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
    public static <T> ArrayList<ArrayList<T>> splitRecords(ArrayList<T> records,int size){
        ArrayList<ArrayList<T>> blocks = new ArrayList<>();
        for(int i = 0;i < records.size();i += size){
            blocks.add(new ArrayList<>(records.subList(i, Math.min(i + size, records.size()))));
        }
        return blocks;
    }
}
