package constants;

public class Constants {
    public static final String DB_URL = "jdbc:sqlite:./db/leechit.db";

    public static final String INSERT_USER = "INSERT INTO Users (name, password) VALUES (?, ?)";
    public static final String INSERT_TORRENT = "INSERT INTO Torrents (name) VALUES (?)";
    public static final String INSERT_FILE = "INSERT INTO Files (name, size) VALUES (?, ?)";
    public static final String INSERT_TORRENT_FILES = "INSERT INTO TorrentFiles (torrent_id, file_id) VALUES (?, ?)";
    public static final String INSERT_POST = "INSERT INTO Posts (id_user, id_torrent) VALUES (?, ?)";

    public static final String SELECT_FILES_BY_TORRENT_ID = "SELECT " +
                                            "f.id AS file_id, " +
                                            "f.name AS file_name, " +
                                            "f.size AS file_size " +
                                            "FROM Files f " +
                                            "JOIN TorrentFiles tf ON f.id = tf.file_id " +
                                            "WHERE tf.torrent_id = ?";

    public static final String SELECT_TORRENT = "SELECT * FROM Torrents where id=?";
    public static final String SELECT_USER = "SELECT * FROM Users WHERE id=?";
    public static final String SELECT_ALL_USERS = "SELECT * FROM Users";
    public static final String SELECT_ALL_POSTS_JOINED = "SELECT " +
                                           "p.id AS post_id, " +
                                           "p.timestamp, " +
                                           "u.id AS user_id, " +
                                           "u.name AS user_name, " +
                                           "t.id AS torrent_id, " +
                                           "t.name AS torrent_name " +
                                           "FROM Posts p " +
                                           "JOIN Users u ON p.id_user = u.id " +
                                           "JOIN Torrents t ON p.id_torrent = t.id";

    public static final String DELETE_POST_RETURNING = "DELETE FROM Posts WHERE id = ?" +
                                                       "RETURNING id_torrent";

    // TODO:: make sure every query is in Constants package
}
