package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "MonsterListWrapper")
public class MonsterListWrapper {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("monster")
    public List<Monster> monsters;

    public MonsterListWrapper() {}

    public MonsterListWrapper(List<Monster> monsters) {
        this.monsters = monsters;
    }
}
