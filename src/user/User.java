package user;

public class User {
    private String username;
    private String password;

    public User(String name, String pass) {
        this.username = name;
        this.password = pass;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
