package auth;

import java.util.HashMap;
import user.User;

public class Auth {
    private HashMap<String, User> users;

    public Auth() {
        this.users = new HashMap<>();

        // test users
        User jon = new User("Jon", "1234");
        User alice = new User("Alice", "foo");

        this.users.put(jon.getUsername(),  jon);
        this.users.put(alice.getUsername(), alice);
    }

    public boolean isUser(String name, String pass) {
        if (this.users.containsKey(name)) {
            return this.users.get(name).getPassword().equals(pass);
        }
        return false;
    }

    public void addUser(String name, String pass) {
        this.users.put(name, new User(name, pass));
    }

    /// Should only get called if you are sure
    /// the user exists.
    public User getUser(String name) {
        return this.users.get(name);
    }
}
