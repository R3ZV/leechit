package torrent;

public class File {
    private int size;
    private String name;
    private String path;

    public File(int size, String name, String path) {
        this.size = size;
        this.name = name;
        this.path = path;
    }

    public void display() {
        String fmt = String.format("%-25d %-15s %s", this.size,  this.name, this.path);
        System.out.println(fmt);
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public int getSize() {
        return this.size;
    }
}
