import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.util.List;

public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        FileProcessor processor = new FileProcessor();


        try (FileReader reader = new FileReader("data.txt")) {
            List<String> lines = processor.process(reader);
            log.info("Lines processed: {}", lines.size());
        } catch (FileProcessingException e) {
            log.error("Business error while processing file: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while processing file", e);
        }
    }
}
