package org.example.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Monster;

import java.io.File;
import java.util.ArrayList;
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
            JsonNode root = mapper.readTree(file);
            JsonNode monstersNode = root.path("monsters");
            List<Monster> monsters = new ArrayList<>();
            for (JsonNode node : monstersNode) {
                Monster m = mapper.treeToValue(node, Monster.class);
                monsters.add(m);
            }
            return monsters;
        } else if (next != null) {
            return next.handle(file);
        }
        throw new IllegalArgumentException("Unsupported format");
    }
}
