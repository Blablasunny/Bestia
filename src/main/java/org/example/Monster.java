package org.example;

import java.util.List;
import java.util.Map;

public class Monster {
    public String name;
    public String description;
    public int dangerLevel;
    public List<String> habitats;
    public String firstMention;
    public Characteristics characteristics;
    public ProtectionRecipe protectionRecipe;
    public String sourceInfo;

    @Override
    public String toString() {
        return name;
    }

    public static class Characteristics {
        public Double height;
        public Object weight;
        public List<String> immunities;
        public String activeTime;
    }

    public static class ProtectionRecipe {
        public Map<String, Integer> ingredients;
        public int preparationTime;
        public String efficiency;
    }
}

