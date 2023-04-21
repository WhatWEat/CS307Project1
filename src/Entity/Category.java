package Entity;

public class Category {
    public static long id;
    public long Category_ID;
    public String category;

    public Category( String category) {
        Category_ID = ++id;
        this.category = category;
    }
}
