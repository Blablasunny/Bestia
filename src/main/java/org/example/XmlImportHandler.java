package org.example;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.util.List;

public class XmlImportHandler implements ImportHandler {
    private ImportHandler next;

    @Override
    public void setNext(ImportHandler handler) {
        this.next = handler;
    }

    @Override
    public List<Monster> handle(File file) throws Exception {
        if (file.getName().endsWith(".xml")) {
            XmlMapper xmlMapper = new XmlMapper();
            MonsterListWrapper wrapper = xmlMapper.readValue(file, MonsterListWrapper.class);
            return wrapper.monsters;
        } else if (next != null) {
            return next.handle(file);
        }
        throw new IllegalArgumentException("Unsupported format");
    }
}
