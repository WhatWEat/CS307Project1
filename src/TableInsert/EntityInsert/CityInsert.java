package TableInsert.EntityInsert;

import Entity.City;
import TableInsert.BasicInfor;
import java.sql.SQLException;
import java.util.ArrayList;

public class CityInsert extends BasicInfor implements Runnable{
    ArrayList<City> cities;
    public CityInsert(String sql, ArrayList<City> cities) {
        super(sql);
        this.cities = cities;
    }

    @Override
    public void run() {
        for(int i = 0;i < cities.size();i++){
            City city = cities.get(i);
            try {
                sql.setLong(1, city.City_ID);
                sql.setString(2, city.city);
                sql.setString(3, city.country);
                sql.executeUpdate();
                if (i % BATCH_SIZE == 0) {
                    sql.executeBatch();
                    sql.clearBatch();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        finalCommit(cities.size());
    }
}
