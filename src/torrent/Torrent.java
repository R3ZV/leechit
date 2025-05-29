package torrent;

import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import database.Database;

public class Torrent extends Decoder {
    private int id;
    private String name;
    private ArrayList<File> files;

    public Torrent(String path, String content) {
        super(content);
        this.name = this.extractName(path);
        this.id = -1;

        files = new ArrayList<>();
        String file = this.next();
        while (!file.equals("EEOF")) {
            files.add(new File(file.length(), file, file, -1));
            file = this.next();
        }
    }

    public Torrent(int id, String name, ArrayList<File> files) {
        super("");
        this.id = id;
        this.name = name;
        this.files = files;
    }

    private String extractName(String pathStr) {
        Path path = Paths.get(pathStr);
        String fileName = path.getFileName().toString();
        return fileName;
    }

    public ArrayList<File> getFiles() {
        return this.files;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }
}
