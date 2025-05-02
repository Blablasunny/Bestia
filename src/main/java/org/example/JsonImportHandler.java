package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

public class JsonImportHandler implements ImportHandler {
    private ImportHandler next;

    @Override
    public void setNext(ImportHandler handler) {
        this.next = handler;
    }

    @Override
    public List<Monster> handle(File file) throws Exception {
        if (file.getName().endsWith(".json")) {
            ObjectMapper mapper = new ObjectMapper();
            MonsterListWrapper wrapper = mapper.readValue(file, MonsterListWrapper.class);
            return wrapper.monsters;
        } else if (next != null) {
            return next.handle(file);
        }
        throw new IllegalArgumentException("Unsupported format");
    }
}
