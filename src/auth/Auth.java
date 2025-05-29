package auth;

import java.sql.*;
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

    public void addUser(String name, String pass) throws SQLException {
        Database db = Database.getInstance();
        try {
            db.insertUser(name, pass);
            this.users = db.getAllUsers();
        } catch (SQLException e) {
            throw e;
        }
    }

    /// Should only get called if you are sure
    /// the user exists.
    public User getUser(String name) {
        return this.users.get(name);
    }
}
