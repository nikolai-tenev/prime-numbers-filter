package com.offerista.producer.service.writer;

import java.io.Writer;
import java.util.Collection;

public interface DiskWriterService {
    /**
     * Write a collection of strings to disk.
     *
     * @param file    path to the file to write to
     * @param content what to write.
     */
    void write(Writer file, Collection<String> content);
}
