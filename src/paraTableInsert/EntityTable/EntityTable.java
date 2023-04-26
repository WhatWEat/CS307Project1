package paraTableInsert.EntityTable;

import Entity.Author;
import TableInsert.BasicInfor;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import paraTableInsert.ParallelInfo;

public abstract class EntityTable<T> extends ParallelInfo implements Runnable{
    protected ArrayList<ArrayList<T>> blocks;
    public EntityTable(ArrayList<T> toInsert, String sql){
        super(sql);
        int size = getSize(toInsert.size());
        blocks = BasicInfor.splitRecords(toInsert, size);
        subDoneSignal = new CountDownLatch(blocks.size());

        for (ArrayList<T> block : blocks) {
            executorTable.submit(createInsertRunnable(block, subStartSignal, subDoneSignal));
        }

    }
    @Override
    public void run() {
        try {
            startSignal.await();
            subStartSignal.countDown();

            subDoneSignal.await();
            executorTable.shutdown();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            doneSignal.countDown();
        }
    }
    protected abstract Runnable createInsertRunnable(ArrayList<T> block,
        CountDownLatch subStartSignal,
        CountDownLatch subDoneSignal);
}
