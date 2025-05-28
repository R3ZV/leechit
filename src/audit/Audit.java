package audit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Audit {
    private String auditFile;

    public Audit(String auditFile) {
        this.auditFile = auditFile;
    }

    public void log(String actionName) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss-dd.MM.yyyy");

        String timestamp = now.format(formatter);
        String content = String.format("%s, %s", actionName, timestamp);

        try (FileWriter fw = new FileWriter(this.auditFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            out.println(content);
        } catch (IOException e) {
            System.err.println("Error appending to audit file: " + this.auditFile + " - " + e.getMessage());
        }
    }
}

