package repl;

import java.io.Console;
import java.util.Arrays;
import java.util.ArrayList;
import registry.Registry;
import torrent.Torrent;
import user.User;
import auth.Auth;

public class Repl implements ReplService {
    private boolean running;
    private Console console;
    private String command;
    private String[] commandArgs;

    private User user;
    private Auth auth;
    private Registry registry;

    public Repl() {
        this.running = true;
        this.console = System.console();
        if (console == null) {
            System.out.println("No console available");
            System.exit(1);
        }
        this.command = new String();
        this.user = null;
        this.auth = new Auth();
        this.registry = new Registry();
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void welcome() {
        System.out.println("Welcome to");
        System.out.println("  _                   _       ___ _   ");
        System.out.println(" | |    ___  ___  ___| |__   |_ _| |_ ");
        System.out.println(" | |   / _ \\/ _ \\/ __| '_ \\   | || __|");
        System.out.println(" | |__|  __/  __/ (__| | | |  | || |_ ");
        System.out.println(" |_____\\___|\\___|\\___|_| |_| |___|\\__|");
        System.out.println("                                      ");

        System.out.println("Type 'help' for available commands!");
    }


    @Override
    public void readCommand() {
        String format = "> ";
        if (this.user != null) {
            format = String.format("(%s)> ", this.user.getUsername());
        }
        String line = this.console.readLine(format);
        String[] args = line.split("\\s+");
        this.command = args[0];
        this.commandArgs = Arrays.copyOfRange(args, 1, args.length);
    }

    @Override
    public void execCommand() {
        /// USER commands
        if (this.user != null) {
            switch (this.command) {
                case "download":
                    download();
                    return;
                case "upload":
                    upload();
                    return;
                case "registry":
                    registry();
                    return;
                case "logout":
                    logout();
                    return;
                default:
            }
        }

        /// REPL commands
        switch (this.command) {
            case "help":
                help();
                break;
            case "login":
                login();
                break;
            case "register":
                register();
                break;
            case "inspect":
                inspect();
                break;
            case "remove":
                remove();
                break;
            case "exit":
                exit();
                break;
            default:
                System.out.println("Unsupported command!");
                System.out.println("Type 'help' for available commands!");
        }
    }

    private void exit() {
        this.running = false;
        System.out.println("Thank you for using Leech it!");
    }

    private void download() {
        if (this.commandArgs.length < 1) {
            System.out.println("Invalid number of arguments!");
            System.out.println("Type 'help' to learn more!");
            return;
        }
        String torrentName = this.commandArgs[0];
        this.registry.downloadTorrent(torrentName);
    }

    private void upload() {
        if (this.commandArgs.length < 1) {
            System.out.println("Invalid number of arguments!");
            System.out.println("Type 'help' to learn more!");
            return;
        }

        String filePath = this.commandArgs[0];
        System.out.println(String.format("Uploading '%s'...", filePath));
        this.registry.addPost(this.user, filePath);
    }

    private void registry() {
        this.registry.showPosts();
    }

    private void inspect() {
        if (this.commandArgs.length < 1) {
            System.out.println("Invalid number of arguments!");
            System.out.println("Type 'help' to learn more!");
            return;
        }

        String torrentName = this.commandArgs[0];
        this.registry.displayTorrent(torrentName);
    }

    private void remove() {
        if (this.commandArgs.length < 1) {
            System.out.println("Invalid number of arguments!");
            System.out.println("Type 'help' to learn more!");
            return;
        }

        String torrentName = this.commandArgs[0];
        this.registry.removeTorrent(this.user, torrentName);
    }

    private void login() {
        if (this.user != null) {
            System.out.println("You are already logged in!");
            return;
        }

        String username = this.console.readLine("Username: ");
        char[] passChars = this.console.readPassword("Password: ");
        String password = new String(passChars);

        if (this.auth.isUser(username, password)) {
            this.user = this.auth.getUser(username);
            System.out.println("Logged in successfully!");
        } else {
            System.out.println("Incorrect username / password!");
        }
    }

    private void register() {
        if (this.user != null) {
            System.out.println("You are already logged in!");
            return;
        }

        String username = this.console.readLine("Username: ");
        char[] passChars = this.console.readPassword("Password: ");
        char[] confirmPassChars = this.console.readPassword("Confirm password: ");
        if (!Arrays.equals(passChars, confirmPassChars)) {
            System.out.println("Password don't match, try again!");
            return;
        }
        this.auth.addUser(username, new String(passChars));
        System.out.println("Account created successfully!");
    }

    private void logout() {
        this.user = null;
    }

    private void help() {
        System.out.println("REPL commands:");
        System.out.println("  help                  - prints this message");
        System.out.println("  login                 - login into your account");
        System.out.println("  register              - create an account");
        System.out.println("  exit                  - terminate the session");

        if (this.user != null) {
            System.out.println("");
            System.out.println("USER commands:");
            System.out.println("  download <torrent>    - download a torrent from registry");
            System.out.println("  upload <torrent>      - upload a torrent to the registry");
            System.out.println("  inspect <torrent>     - inspect what a torrent contains");
            System.out.println("  remove <torrent>      - removes torrent form registry");
            System.out.println("  registry              - print available files in the registry");
            System.out.println("  logout                - logs out the user if logged in");
        }
    }
}
