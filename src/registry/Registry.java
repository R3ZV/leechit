package registry;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import torrent.Torrent;

public class Registry {
    ArrayList<Torrent> torrents;

    public Registry() {
        this.torrents = new ArrayList<>();
        this.torrents.add(new Torrent("gta6.torrent", "8:gta6.exe10:assets.dll"));
        this.torrents.add(new Torrent("minecraft.torrent", "12:launcher.exe9:main.java"));
    }

    public void addTorrent(String path) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            String content = new String(bytes, StandardCharsets.UTF_8);

            Torrent torrent = new Torrent(path, content);
            this.torrents.add(torrent);
        } catch (IOException e) {
            System.out.println("Couldn't read torrent file!");
            return;
        }

    }

    public ArrayList<Torrent> getTorrents() {
        return this.torrents;
    }
}
