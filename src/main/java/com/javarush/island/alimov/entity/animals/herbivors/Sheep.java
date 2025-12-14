package com.javarush.island.alimov.entity.animals.herbivors;

import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.plants.Grass;

import java.util.Map;

public class Sheep extends Herbivore{
    public Sheep(Cell currentCell, Island island) {
        super("Sheep", "\uD83D\uDC11", 70, 70, 140, 3, 15, currentCell, island, true,false);
    }
    @Override
    protected Map<Class<?>, Double> getFoodSources() {
        return Map.ofEntries(
                Map.entry(Grass.class, 1.0)
        );
    }
    @Override
    protected Animal createNewInstance(Cell cell, Island island) {
        return new Sheep(cell,island);
    }
}
