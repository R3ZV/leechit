package auth;

import java.util.HashMap;
import user.User;
import database.Database;

public class Auth {
    private HashMap<String, User> users;

    public Auth() {
        this.users = new HashMap<>();

        Database db = Database.getInstance();
        this.users = db.getAllUsers();
    }

    public boolean isUser(String name, String pass) {
        if (this.users.containsKey(name)) {
            return this.users.get(name).getPassword().equals(pass);
        }
        return false;
    }

    public void addUser(String name, String pass) {
        Database db = Database.getInstance();
        db.insertUser(name, pass);
        this.users = db.getAllUsers();
    }

    /// Should only get called if you are sure
    /// the user exists.
    public User getUser(String name) {
        return this.users.get(name);
    }
}
