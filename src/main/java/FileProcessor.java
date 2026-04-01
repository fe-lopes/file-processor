import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {
    private static final Logger log = LoggerFactory.getLogger(FileProcessor.class);

    public List<String> process(Reader reader) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.toUpperCase());
            }

            log.info("File processed successfully. Total lines: {}", lines.size());

        } catch (IOException e) {
            log.error("Error processing file.");
            throw new FileProcessingException("Error processing file", e);
        }

        return lines;
    }
}
