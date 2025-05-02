package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Exporter {
    public static void exportJson(List<Monster> monsters, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, new MonsterListWrapper(monsters));
    }

    public static void exportXml(List<Monster> monsters, File file) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.writerWithDefaultPrettyPrinter().writeValue(file, new MonsterListWrapper(monsters));
    }

    public static void exportYaml(List<Monster> monsters, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, new MonsterListWrapper(monsters));
    }
}
