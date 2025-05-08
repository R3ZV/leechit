package repl;

public interface ReplService {
    void welcome();
    boolean isRunning();
    void readCommand();
    void execCommand();
}
