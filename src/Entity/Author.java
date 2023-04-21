package Entity;

public class Author {
    public String id;
    public String registerTime;
    public String phoneNumber;

    public Author() {
    }

    public Author(String id, String registerTime, String phoneNumber) {
        this.id = id;
        this.registerTime = registerTime;
        this.phoneNumber = phoneNumber;
    }
}
