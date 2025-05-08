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

    public void display() {
        String fmt = String.format("%-25d %-15s %s", this.size,  this.name, this.path);
        System.out.println(fmt);
    }
}
