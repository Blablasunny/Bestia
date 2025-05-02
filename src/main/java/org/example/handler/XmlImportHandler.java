package org.example.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.Monster;

import java.io.File;
import java.util.ArrayList;
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
            JsonNode root = xmlMapper.readTree(file);
            List<Monster> monsters = new ArrayList<>();

            JsonNode monstersNode = root.path("monsters");
            if (!monstersNode.isArray()) {
                monstersNode = root.path("bestiarium").path("monsters");
            }

            if (monstersNode.isMissingNode() || !monstersNode.isArray()) {
                throw new IllegalArgumentException("Неверная структура XML: не найден массив monsters.");
            }

            for (JsonNode node : monstersNode) {
                try {
                    Monster m = xmlMapper.treeToValue(node, Monster.class);
                    monsters.add(m);
                } catch (Exception e) {
                    System.err.println("Ошибка разбора существа: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            return monsters;
        } else if (next != null) {
            return next.handle(file);
        }
        throw new IllegalArgumentException("Unsupported format");
    }
}

