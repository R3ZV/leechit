package torrent;

public class File {
    private int id;
    private int size;
    private String name;
    private String path;

    public File(int size, String name, String path, int id) {
        this.id = id;
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

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
