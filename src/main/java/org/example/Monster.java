package org.example;

import java.util.List;

public class Monster {
    public Monster() {}

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
        public Characteristics() {}
        public Double height;
        public Object weight;
        public List<String> immunities;
        public String activeTime;
    }

    public static class ProtectionRecipe {
        public ProtectionRecipe() {}
        public List<Ingredient> ingredients;
        public int preparationTime;
        public String efficiency;
    }

    public static class Ingredient {
        public Ingredient() {}
        public String name;
        public int quantity;
    }
}
