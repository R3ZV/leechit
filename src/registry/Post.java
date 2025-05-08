package registry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import user.User;
import torrent.Torrent;
import torrent.File;

public class Post {
    User author;
    Torrent torrent;
    String timestamp;

    public Post(User author, Torrent torrent) {
        this.author = author;
        this.torrent = torrent;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss-dd.MM.yyyy");
        this.timestamp = now.format(formatter);
    }

    public void display() {
        String fmt = String.format("%-25s %-15s %s", torrent.getName(),  author.getUsername(), this.timestamp);
        System.out.println(fmt);
    }

    public String getName() {
        return this.torrent.getName();
    }

    public void torrentContents() {
        System.out.println(String.format("%s contains:", this.torrent.getName()));
        System.out.println(String.format("%-25s %-15s %s", "Size",  "Name", "Path"));
        for (File file : this.torrent.getFiles()) {
            file.display();
        }
    }

    public String getAuthor() {
        return this.author.getUsername();
    }
}
