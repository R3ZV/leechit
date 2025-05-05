import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DecoderTests {

    @Test
    void decodeStrings() {
        Decoder decoder = new Decoder("6:decode");
        String result = decoder.next();
        String expected = "decoder";

        String errMsg = String.format("Expected '%s but got '%s'", expected, result);
        assertEquals(expected, result, errMsg);
    }
}
