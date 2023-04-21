package Entity;

import java.util.ArrayList;
import java.util.Objects;

public class Author {
    public String id;
    public String registerTime;
    public String phoneNumber;

    public String name;
    public Author() {
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
}
