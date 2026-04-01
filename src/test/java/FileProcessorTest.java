import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessorTest {

    @Test
    void shouldProcessLinesSuccessfully() {
        String input = "hello\nworld";

        FileProcessor processor = new FileProcessor();
        List<String> result = processor.process(new StringReader(input));

        assertEquals(2, result.size());
        assertEquals("HELLO", result.get(0));
        assertEquals("WORLD", result.get(1));
    }
}
