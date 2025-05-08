package registry;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import torrent.Torrent;
import user.User;

public class Registry {
    ArrayList<Post> posts;

    public Registry() {
        this.posts = new ArrayList<>();

        // TODO: delete after we use a db
        User jon = new User("Jon", "1234");
        this.posts.add(new Post(jon, new Torrent("gta6.torrent", "8:gta6.exe10:assets.dll")));
        this.posts.add(new Post(jon, new Torrent("minecraft.torrent", "12:launcher.exe9:main.java")));
    }

    public void addPost(User user, String path) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            String content = new String(bytes, StandardCharsets.UTF_8);

            Torrent torrent = new Torrent(path, content);
            this.posts.add(new Post(user, torrent));
        } catch (IOException e) {
            System.out.println("Couldn't read torrent file!");
            return;
        }

    }

    public void showPosts() {
        System.out.println(String.format("%-25s %-15s %s", "Torrent name", "Author", "Timestamp"));
        System.out.println("");

        for (int i = 0; i < posts.size(); i++) {
            posts.get(i).display();
        }
    }

    public void displayTorrent(String name) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getName().equals(name)) {
                posts.get(i).torrentContents();
                return;
            }
        }
        System.out.println("Couldn't find: " + name);
    }

    public void removeTorrent(User user, String torrentName) {
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getName().equals(torrentName)) {
                if (!posts.get(i).getAuthor().equals(user.getUsername())) {
                    System.out.println("You don't own that torrent!");
                } else {
                    posts.remove(i);
                    System.out.println("Successfully removed torrent!");
                }
                return;
            }
        }
        System.out.println("Couldn't find: " + torrentName);
    }
}
