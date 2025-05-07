import repl.Repl;

public  class Main {
    public static void main(String[] args) throws Exception {
        Repl repl = new Repl();
        repl.welcome();
        while (repl.isRunning()) {
            repl.readCommand();
            repl.execCommand();
        }
    }
}
