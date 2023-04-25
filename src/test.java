import java.util.ArrayList;

public class test {

    public static void main(String[] args) {
        ArrayList<String> a = new ArrayList<>();
        a.add("1");
        ArrayList<String> b = a;
        b.add("2");
        System.out.println(a);
        System.out.println(b);
    }
}
