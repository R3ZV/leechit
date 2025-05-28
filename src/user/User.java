package user;

public class User {
    private int id;
    private String username;
    private String password;

    public User(String name, String pass, int id) {
        this.username = name;
        this.password = pass;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }
}
