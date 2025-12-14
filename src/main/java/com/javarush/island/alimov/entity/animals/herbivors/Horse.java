package com.javarush.island.alimov.entity.animals.herbivors;

import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.plants.Grass;

import java.util.Map;

public class Horse extends Herbivore{
    public Horse(Cell currentCell, Island island) {
        super("Horse", " \uD83D\uDC0E",400, 400,20, 4, 60, currentCell, island, true,false);
    }
    @Override
    protected Map<Class<?>, Double> getFoodSources() {
        return Map.ofEntries(
                Map.entry(Grass.class, 1.0)
        );
    }
    @Override
    protected Animal createNewInstance(Cell cell, Island island) {
        return new Horse(cell,island);
    }
}
