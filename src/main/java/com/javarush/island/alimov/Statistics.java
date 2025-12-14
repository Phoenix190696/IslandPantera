package com.javarush.island.alimov;

import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.animals.herbivors.*;
import com.javarush.island.alimov.entity.animals.predators.*;
import com.javarush.island.alimov.entity.plants.*;

import java.util.List;
import java.util.Map;

public class Statistics {
    private final Island island;
    public static final Map<Class<?>, String> ICONS = Map.ofEntries(
            Map.entry(Grass.class, "\uD83C\uDF3F"),
            Map.entry(Wolf.class, "\uD83D\uDC3A"),
            Map.entry(Rabbit.class, "\uD83D\uDC07"),
            Map.entry(Sheep.class, "\uD83D\uDC11"),
            Map.entry(Boar.class, "\uD83D\uDC17"),
            Map.entry(Buffalo.class, "\uD83D\uDC03"),
            Map.entry(Caterpillar.class, "\uD83D\uDC1B"),
            Map.entry(Deer.class, "\uD83E\uDD8C"),
            Map.entry(Duck.class, "\uD83E\uDD86"),
            Map.entry(Goat.class, "\uD83D\uDC10"),
            Map.entry(Horse.class, "\uD83D\uDC0E"),
            Map.entry(Mouse.class, "\uD83D\uDC01"),
            Map.entry(Bear.class, "\uD83D\uDC3B"),
            Map.entry(Boa.class, "\uD83D\uDC0D"),
            Map.entry(Eagle.class, "\uD83E\uDD85"),
            Map.entry(Fox.class, "\uD83E\uDD8A")
    );

    public Statistics(Island island) {
        this.island = island;
    }

    public int countEntities(Class<?> clazz) {
        int num = 0;
        Cell[][] cells = island.getLocation();

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Cell cell = island.getCell(i, j);
                if (cell == null) continue;

                if (Plant.class.isAssignableFrom(clazz)) {
                    Map<Class<? extends Plant>, List<Plant>> plants = cell.getPlants();
                    List<Plant> list = plants.get(clazz);
                    if (list != null) {
                        num += list.size();
                    }
                } else if (Animal.class.isAssignableFrom(clazz)) {
                    Map<Class<? extends Animal>, List<Animal>> animals = cell.getAnimals();
                    List<Animal> list = animals.get(clazz);
                    if (list != null) {
                        num += list.size();
                    }
                }
            }
        }
        return num;
    }

    public void printStatistics(Class<?> clazz) {
        int count = countEntities(clazz);
        String icon = ICONS.getOrDefault(clazz, "?");
        System.out.println(icon + " - " + count);
    }
}

