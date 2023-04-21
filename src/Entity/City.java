package Entity;

import java.util.Objects;

public class City {
    public static long id = 0;
    public long City_ID;
    public String city;
    public String country;

    public City(String city, String country) {
        City_ID = ++id;
        this.city = city;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city1 = (City) o;
        return Objects.equals(city, city1.city) && Objects.equals(country, city1.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, country);
    }

    @Override
    public String toString() {
        return "City{" +
                "City_ID=" + City_ID +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
