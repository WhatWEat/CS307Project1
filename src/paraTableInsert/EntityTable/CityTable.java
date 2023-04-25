package paraTableInsert.EntityTable;

import Entity.City;
import TableInsert.EntityInsert.CategoryInsert;
import TableInsert.EntityInsert.CityInsert;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class CityTable extends EntityTable<City> {

    public CityTable(ArrayList<City> toInsert, String sql) {
        super(toInsert, sql);
    }

    @Override
    protected Runnable createInsertRunnable(ArrayList<City> block, CountDownLatch subStartSignal,
        CountDownLatch subDoneSignal) {
        return new CityInsert(sql, block, subStartSignal, subDoneSignal);
    }

}
