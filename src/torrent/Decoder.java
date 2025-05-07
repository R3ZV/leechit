package torrent;

public class Decoder {
    int pos;
    String content;

    public Decoder(String content) {
        this.pos = 0;
        this.content = content;
    }

    public String next() {
        if (this.pos >= this.content.length()) {
            return "EEOF";
        }

        // first values in strings is the len
        if (Character.isDigit(this.content.charAt(0))) {
            return this.parseString();
        } else if (this.content.charAt(0) == 'i') {
            return this.parseNumber();
        }

        throw new RuntimeException("Unsupported content found at pos: " + this.pos);
    }

    String parseString() {
        int colIdx = this.pos;
        while (this.content.charAt(colIdx) != ':') {
            colIdx++;
        }

        int len = Integer.parseInt(content.substring(this.pos, colIdx));
        this.pos += colIdx + len + 1;
        return this.content.substring(colIdx + 1, colIdx + 1 + len);
    }

    String parseNumber() {
        int colIdx = this.pos;
        while (this.content.charAt(colIdx) != 'e') {
            colIdx++;
        }

        this.pos += colIdx + 1;
        return this.content.substring(this.pos + 1, colIdx);
    }
}
