package org.example;

import java.io.File;
import java.util.List;

public interface ImportHandler {
    void setNext(ImportHandler handler);
    List<Monster> handle(File file) throws Exception;
}
