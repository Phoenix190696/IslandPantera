package com.javarush.island.alimov.entity.animals.herbivors;

import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.plants.Grass;

import java.util.Map;

public class Goat extends Herbivore{
    public Goat(Cell currentCell, Island island) {
        super("Goat", "\uD83D\uDC10",60, 60,140, 3, 10, currentCell, island, true,false);
    }
    @Override
    protected Map<Class<?>, Double> getFoodSources() {
        return Map.ofEntries(
                Map.entry(Grass.class, 1.0)
        );
    }
    @Override
    protected Animal createNewInstance(Cell cell, Island island) {
        return new Goat(cell,island);
    }
}
