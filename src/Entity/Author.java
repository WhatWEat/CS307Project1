package Entity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Author {
    public static long no_exist_id = 1000000000L;
    public static long get_exist_phone = (long) Math.pow(10,9);
    public String id;
    public String registerTime;
    public String phoneNumber;
    public String name;
    public Author() {
    }
    public Author(String name) {
        this.name = name;
        this.id = String.format("99%d",++no_exist_id);
        this.phoneNumber = String.format("13%d",++get_exist_phone);
        this.registerTime = generateRandomDateTime();
    }

    public Author(String id, String registerTime, String phoneNumber,String name) {
        this.id = id;
        this.registerTime = registerTime;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id) && Objects.equals(registerTime, author.registerTime) && Objects.equals(phoneNumber, author.phoneNumber) && Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, registerTime, phoneNumber, name);
    }
    public static String generateRandomDateTime() {
        // Set the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Generate a random date between 2010 and 2022
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 1, 1);
        Date startDate = calendar.getTime();
        calendar.set(2023, 12, 31);
        Date endDate = calendar.getTime();
        long randomDateTime = ThreadLocalRandom.current().nextLong(startDate.getTime(), endDate.getTime());

        // Convert the random date to a string
        return dateFormat.format(new Date(randomDateTime));
    }
}
