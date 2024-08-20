package com.offerista.consumer.service.writer;

import java.io.IOException;
import java.nio.file.Path;

public interface DiskWriterService {
    /**
     * Method that appends data to a file on disk.
     *
     * @param filePath to save the data to
     * @param content  data to save
     * @throws IOException
     */
    void write(Path filePath, String content) throws IOException;
}
