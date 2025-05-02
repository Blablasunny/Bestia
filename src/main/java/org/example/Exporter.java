package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Exporter {
    public static void exportJson(List<Monster> monsters, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.putPOJO("monsters", monsters);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
    }

    public static void exportXml(List<Monster> monsters, File file) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        ObjectNode root = xmlMapper.createObjectNode();
        root.putPOJO("monsters", monsters);
        xmlMapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
    }

    public static void exportYaml(List<Monster> monsters, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ObjectNode root = mapper.createObjectNode();
        root.putPOJO("monsters", monsters);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
    }
}

