package torrent;

import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Torrent extends Decoder {
    private String name;
    private ArrayList<File> files;

    public Torrent(String path, String content) {
        super(content);
        this.name = this.extractName(path);

        files = new ArrayList<>();
        String file = this.next();
        while (!file.equals("EEOF")) {
            files.add(new File(file.length(), file, file));
            file = this.next();
        }
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
}
