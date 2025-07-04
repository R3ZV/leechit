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
import database.Database;

public class Registry {
    private Set<Post> posts;

    public Registry() {
        Database db = Database.getInstance();
        this.posts = db.getAllPosts();
    }

    public void addPost(User user, String path) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            String content = new String(bytes, StandardCharsets.UTF_8);

            Database db = Database.getInstance();
            Torrent torrent = new Torrent(path, content);

            int torrentId = db.insertTorrent(torrent);
            if (torrentId == -1) {
                throw new RuntimeException("Couldn't insert given torrent file!");
            }

            ArrayList<File> files = torrent.getFiles();
            db.insertFiles(files);
            // after insert files should be changed to have their generated ids

            db.insertTorrentFiles(torrentId, files);

            db.insertPost(user.getId(), torrentId);
            this.posts = db.getAllPosts();
        } catch (IOException e) {
            System.out.println("Couldn't read torrent file!");
            return;
        }
    }

    public void showPosts() {
        System.out.println(String.format("%-10s %-25s %-15s %s", "ID", "Torrent name", "Author", "Timestamp"));
        System.out.println("");

        for (Post post : this.posts) {
            post.display();
        }
    }

    public void displayTorrent(int torrentId) {
        for (Post post : this.posts) {
            if (post.getTorrent().getId() == torrentId) {
                post.torrentContents();
                return;
            }
        }
        System.out.println("Couldn't find torrnet file with id: " + torrentId);
    }

    public void removeTorrentPost(User user, int torrentId) {
        for (Post post : posts) {
            if (post.getTorrent().getId() == torrentId) {
                if (!post.getAuthor().equals(user.getUsername())) {
                    continue;
                }

                Database db = Database.getInstance();
                db.removePost(post.getId());
                this.posts = db.getAllPosts();
                System.out.println("Successfully removed torrent!");
                return;
            }
        }
        System.out.println("You don't own any torrent with that id!");
    }

    public void downloadTorrent(int torrentId) {
        for (Post post : this.posts) {
            if (post.getTorrent().getId() == torrentId) {
                this.download(post);
                return;
            }
        }
        System.out.println("Couldn't find torrent file with id: " + torrentId);
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
