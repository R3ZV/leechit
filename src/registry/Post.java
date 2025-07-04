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
    private int id;

    public Post(User author, Torrent torrent, String timestamp, int id) {
        this.author = author;
        this.id = id;
        this.torrent = torrent;
        this.timestamp = timestamp;
    }

    public void display() {
        String fmt = String.format("%-10s %-25s %-15s %s", torrent.getId(), torrent.getName(),  author.getUsername(), this.timestamp);
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

    public int getId() {
        return this.id;
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
