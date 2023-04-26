package paraTableInsert;

import TableInsert.BasicInfor;
import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelInfo {
    public static Connection con = null;
    public static final int totalTable = 6;
    public static final int parallel = 6;
    protected static CountDownLatch startSignal;
    protected static CountDownLatch doneSignal;
    static final int parallelPerThread = 2;
    protected CountDownLatch subStartSignal = new CountDownLatch(1);
    protected CountDownLatch subDoneSignal;
    protected ExecutorService executorTable = Executors.newFixedThreadPool(parallelPerThread);
    protected String sql;
    public ParallelInfo(String sql){
        this.sql = sql;
    }
    protected static int getSize(int count){
        int size = 0;
        if (count % parallelPerThread == 0) {
            size = count / parallelPerThread;
        } else {
            size = count / parallelPerThread + 1;
        }
        return size;
    }
    public static void setCon(Connection con){
        ParallelInfo.con = con;
        BasicInfor.con = con;
    }
    public static void setSignal(CountDownLatch startSignal, CountDownLatch doneSignal){
        ParallelInfo.startSignal = startSignal;
        ParallelInfo.doneSignal = doneSignal;
    }
}
