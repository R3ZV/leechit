package repl;

import java.io.Console;
import java.util.Arrays;
import java.util.ArrayList;
import registry.Registry;
import torrent.Torrent;
import user.User;
import auth.Auth;

public class Repl {
    boolean running;
    Console console;
    String command;
    String[] commandArgs;

    User user;
    Auth auth;
    Registry registry;

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

    public boolean isRunning() {
        return this.running;
    }

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
            case "exit":
                exit();
                break;
            default:
                System.out.println("Unsupported command!");
                System.out.println("Type 'help' for available commands!");
        }
    }

    void exit() {
        this.running = false;
        System.out.println("Thank you for using Leech it!");
    }

    void download() {
        System.out.println("TODO\n");
    }

    void upload() {
        if (this.commandArgs.length < 1) {
            System.out.println("Invalid number of arguments!");
            System.out.println("Type 'help' to learn more!");
            return;
        }

        String filePath = this.commandArgs[0];
        System.out.println(String.format("Uploading '%s'...", filePath));
        this.registry.addTorrent(filePath);
    }

    void registry() {
        System.out.println("Torrents:");

        ArrayList<Torrent> torrents = registry.getTorrents();
        for (int i = 0; i < torrents.size(); i++) {
            System.out.println(String.format("[%d]: %s", i, torrents.get(i).getName()));
        }
    }

    void login() {
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

    void register() {
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

    void logout() {
        this.user = null;
    }

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

    void help() {
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
            System.out.println("  registry              - print available files in the registry");
            System.out.println("  logout                - logs out the user if logged in");
        }
    }
}
