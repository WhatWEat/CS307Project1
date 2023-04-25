package TableInsert.EntityInsert;

import Entity.City;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class CityInsert extends BasicInfor implements Runnable{
    ArrayList<City> cities;
    public CityInsert(String sql, ArrayList<City> cities, CountDownLatch startSignal, CountDownLatch doneSignal) {
        super(sql,startSignal,doneSignal);
        this.cities = cities;
    }

    @Override
    public void run() {
        try{
            startSignal.await();
            for(int i = 0;i < cities.size();i++){
                City city = cities.get(i);
                try {
                    sql.setLong(1, city.City_ID);
                    sql.setString(2, city.city);
                    sql.setString(3, city.country);
                    sql.addBatch();
                    if (i % BATCH_SIZE == 0) {
                        sql.executeBatch();
                        sql.clearBatch();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            finalCommit(cities.size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            doneSignal.countDown();
        }
    }
}
