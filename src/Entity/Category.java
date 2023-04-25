package Entity;

import java.io.Serializable;
import java.util.Objects;

public class Category implements Serializable {
    public static long id;
    public long Category_ID;
    public String category;

    public Category( String category) {
        Category_ID = ++id;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category1 = (Category) o;
        return Objects.equals(category, category1.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category);
    }

    @Override
    public String toString() {
        return "Category{" +
                "Category_ID=" + Category_ID +
                ", category='" + category + '\'' +
                '}';
    }
}
