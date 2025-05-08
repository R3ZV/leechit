package registry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import user.User;
import torrent.Torrent;
import torrent.File;

public class Post implements Comparable<Post> {
    private User author;
    private Torrent torrent;
    private String timestamp;

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

    public Torrent getTorrent() {
        return this.torrent;
    }

    public String getName() {
        return this.torrent.getName();
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    @Override
    public int compareTo(Post other) {
        return this.torrent.getName().compareTo(other.torrent.getName());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Post)) return false;
        Post post = (Post) other;
        return this.torrent.getName().equals(post.torrent.getName());
    }

    @Override
    public int hashCode() {
        return author.hashCode();
    }
}
