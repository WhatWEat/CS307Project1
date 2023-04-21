package Entity;

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
}
