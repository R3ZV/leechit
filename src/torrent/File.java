package torrent;

public class File {
    int size;
    String name;
    String path;

    public File(int size, String name, String path) {
        this.size = size;
        this.name = name;
        this.path = path;
    }
}
