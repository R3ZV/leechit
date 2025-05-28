package constants;

public class Constants {
    public static final String DB_URL = "jdbc:sqlite:./db/leechit.db";

    public static final String INSERT_USER = "INSERT INTO Users (name, password) VALUES (?, ?)";
    public static final String INSERT_TORRENT = "INSERT INTO Torrents (name) VALUES (?)";
    public static final String INSERT_FILE = "INSERT INTO Files (name, size) VALUES (?, ?)";
    public static final String INSERT_TORRENT_FILES = "INSERT INTO TorrentFiles (torrent_id, file_id) VALUES (?, ?)";
    public static final String INSERT_POST = "INSERT INTO Posts (id_user, id_torrent) VALUES (?, ?)";

    public static final String SELECT_ALL_USERS = "SELECT * FROM Users";
    public static final String SELECT_ALL_POSTS = "SELECT * FROM Posts";

    public static final String REMOVE_POST = "DELETE FROM Posts where id = ?";
}
