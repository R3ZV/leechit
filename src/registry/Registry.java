package registry;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.nio.file.*;

import torrent.Torrent;
import torrent.File;
import user.User;

public class Registry {
    private Set<Post> posts;

    public Registry() {
        this.posts = new TreeSet<>();

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

        for (Post post : this.posts) {
            post.display();
        }
    }

    public void displayTorrent(String name) {
        for (Post post : this.posts) {
            if (post.getName().equals(name)) {
                post.torrentContents();
                return;
            }
        }
        System.out.println("Couldn't find: " + name);
    }

    public void removeTorrent(User user, String torrentName) {
        for (Post post : posts) {
            if (post.getName().equals(torrentName)) {
                if (!post.getAuthor().equals(user.getUsername())) {
                    System.out.println("You don't own that torrent!");
                } else {
                    posts.remove(post);
                    System.out.println("Successfully removed torrent!");
                }
                return;
            }
        }
        System.out.println("Couldn't find: " + torrentName);
    }

    public void downloadTorrent(String name) {
        for (Post post : this.posts) {
            if (post.getName().equals(name)) {
                this.download(post);
                return;
            }
        }
        System.out.println("Couldn't find: " + name);
    }

    private void download(Post post) {
        ArrayList<File> files = post.getTorrent().getFiles();
        Path downloadDir = Paths.get("downloads");

        try {
            if (!Files.exists(downloadDir)) {
                Files.createDirectory(downloadDir);
            }

            // Loop through the file names and create empty files
            for (File file : files) {
                Path filePath = downloadDir.resolve(file.getName());
                Files.createFile(filePath);
                System.out.println("Downloaded: " + filePath);
            }

        } catch (FileAlreadyExistsException e) {
            System.out.println("File already exists: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
