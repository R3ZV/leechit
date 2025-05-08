package torrent;

public class Decoder {
    protected int pos;
    protected String content;

    public Decoder(String content) {
        this.pos = 0;
        this.content = content;
    }

    protected String next() {
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

    private String parseString() {
        int colIdx = this.pos;
        while (this.content.charAt(colIdx) != ':') {
            colIdx++;
        }

        int len = Integer.parseInt(content.substring(this.pos, colIdx));
        this.pos += colIdx + len + 1;
        return this.content.substring(colIdx + 1, colIdx + 1 + len);
    }

    private String parseNumber() {
        int colIdx = this.pos;
        while (this.content.charAt(colIdx) != 'e') {
            colIdx++;
        }

        this.pos += colIdx + 1;
        return this.content.substring(this.pos + 1, colIdx);
    }
}
