package database;

import java.sql.*;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

import constants.Constants;
import user.User;
import torrent.Torrent;
import torrent.File;
import registry.Post;

public class Database {
    private static Database instance;
    private Connection connection;

    private Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(Constants.DB_URL);
            initDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private void initDatabase() {
        try (Statement stmt = connection.createStatement()) {
            String userTable = "CREATE TABLE IF NOT EXISTS Users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL)";
            stmt.execute(userTable);

            String filesTable = "CREATE TABLE IF NOT EXISTS Files (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "size INTEGER NOT NULL)";
            stmt.execute(filesTable);

            String torrentsTable = "CREATE TABLE IF NOT EXISTS Torrents (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL)";
            stmt.execute(torrentsTable);

            String torrentsFilesTable = "CREATE TABLE IF NOT EXISTS TorrentFiles (" +
                    "torrent_id INTEGER NOT NULL," +
                    "file_id INTEGER NOT NULL," +
                    "PRIMARY KEY (torrent_id, file_id)," +
                    "FOREIGN KEY (torrent_id) REFERENCES Torrents(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (file_id) REFERENCES Files(id) ON DELETE CASCADE)";
            stmt.execute(torrentsFilesTable);

            String postsTable = "CREATE TABLE IF NOT EXISTS Posts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_user INTEGER NOT NULL," +
                    "id_torrent INTEGER NOT NULL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (id_user) REFERENCES Users(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (id_torrent) REFERENCES Torrents(id) ON DELETE CASCADE)";
            stmt.execute(postsTable);
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public HashMap<String, User> getAllUsers() {
        HashMap<String, User> users = new HashMap<>();

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(Constants.SELECT_ALL_USERS)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                User user = new User(name, password, id);
                users.put(name, user);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all users: " + e.getMessage());
        }
        return users;
    }

    public Set<Post> getAllPosts() {
        Set<Post> posts = new HashSet<>();

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(Constants.SELECT_ALL_POSTS_JOINED)) {
            while (rs.next()) {
                int postId = rs.getInt("post_id");
                String timestamp = rs.getString("timestamp");

                int userId = rs.getInt("user_id");
                String userName = rs.getString("user_name");
                User author = getUser(userId);

                int torrentId = rs.getInt("torrent_id");
                String torrentName = rs.getString("torrent_name");
                Torrent torrent = getTorrent(torrentId);

                posts.add(new Post(author, torrent, timestamp, postId));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all posts: " + e.getMessage());
        }
        return posts;
    }

    public User getUser(int id) {
        User user = null;
        try (PreparedStatement pstmt = connection.prepareStatement(Constants.SELECT_USER)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String userName = rs.getString("name");
                    String userPassword = rs.getString("password");

                    user = new User(userName, userPassword, userId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user: " + e.getMessage());
        }

        assert user != null;
        return user;
    }

    public Torrent getTorrent(int torrentId) {
        Torrent torrent = null;
        try (PreparedStatement pstmt = connection.prepareStatement(Constants.SELECT_TORRENT)) {
            pstmt.setInt(1, torrentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    ArrayList<File> files = getFilesWithTorrent(id);
                    torrent = new Torrent(id, name, files);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving torrent with ID " + torrentId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return torrent;
    }

     public ArrayList<File> getFilesWithTorrent(int torrentId) {
        ArrayList<File> files = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(Constants.SELECT_FILES_BY_TORRENT_ID)) {
            pstmt.setInt(1, torrentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int fileId = rs.getInt("file_id");
                    String fileName = rs.getString("file_name");
                    int fileSize = rs.getInt("file_size");
                    files.add(new File(fileSize, fileName, fileName, fileId));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving files for torrent ID " + torrentId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return files;
    }

    public void insertTorrentFiles(int torrentId, ArrayList<File> files) {
        for (File file : files) {
            try (PreparedStatement pstmt = connection.prepareStatement(Constants.INSERT_TORRENT_FILES)) {
                pstmt.setString(1, String.valueOf(torrentId));
                pstmt.setString(2, String.valueOf(file.getId()));
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error inserting into TorrentFiles: " + e.getMessage());
            }
        }
    }

    public void insertPost(int authorId, int torrentId) {
        try (PreparedStatement pstmt = connection.prepareStatement(Constants.INSERT_POST)) {
            pstmt.setString(1, String.valueOf(authorId));
            pstmt.setString(2, String.valueOf(torrentId));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting into TorrentFiles: " + e.getMessage());
        }
    }

    // TODO: instead of String.valueOf() change it to set int everywhere
    public void removePost(int postId) {
        int idTorrent = -1;
        try (PreparedStatement pstmt = connection.prepareStatement(Constants.DELETE_POST_RETURNING)) {
            pstmt.setInt(1, postId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idTorrent = rs.getInt("id_torrent");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error deleting post with ID " + postId + ": " + e.getMessage());
            e.printStackTrace();
        }

        if (idTorrent != -1) {
            removeTorrent(idTorrent);
        }
    }

    public void removeTorrent(int torrentId) throws RuntimeException {
        Set<Integer> filesIdToDelete = new HashSet<>();
        try {
            String SELECT_FILE_IDS_FOR_TORRENT = "SELECT file_id FROM TorrentFiles WHERE torrent_id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(SELECT_FILE_IDS_FOR_TORRENT)) {
                pstmt.setInt(1, torrentId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        filesIdToDelete.add(rs.getInt("file_id"));
                    }
                }
            }

            String DELETE_TORRENT_SQL = "DELETE FROM Torrents WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(DELETE_TORRENT_SQL)) {
                pstmt.setInt(1, torrentId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove torrent and its files", e);
        }

        try {
            removeFiles(filesIdToDelete);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove files attached to torrent", e);
        }
    }


    public void removeFiles(Set<Integer> fileIds) throws SQLException {
        if (fileIds.isEmpty()) {
            return;
        }

        String DELETE_FILE_SQL = "DELETE FROM Files WHERE id = ?";

        for (int fileId : fileIds) {
            try (PreparedStatement pstmt = connection.prepareStatement(DELETE_FILE_SQL)) {
                pstmt.setInt(1, fileId);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    public int insertUser(String name, String password) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(Constants.INSERT_USER,
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw e;
        }
        return -1;
    }

    public int insertTorrent(Torrent torrent) {
        try (PreparedStatement pstmt = connection.prepareStatement(Constants.INSERT_TORRENT,
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, torrent.getName());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
        }
        return -1;
    }

    /// files are going to be changed, they will receive the generated
    /// id from the database
    public void insertFiles(ArrayList<File> files) {
        for (File file : files) {
            try (PreparedStatement pstmt = connection.prepareStatement(Constants.INSERT_FILE,
                    Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, file.getName());
                pstmt.setString(2, String.valueOf(file.getSize()));
                pstmt.executeUpdate();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        file.setId(rs.getInt(1));
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error inserting user: " + e.getMessage());
            }
        }
    }

    private boolean hasData(String tableName) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            String sql = "SELECT COUNT(*) FROM " + tableName;
            ResultSet rs = stmt.executeQuery(sql);
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error checking if table has data: " + e.getMessage());
        }
        return false;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
