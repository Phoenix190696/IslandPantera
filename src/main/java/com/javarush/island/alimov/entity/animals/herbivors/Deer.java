package com.javarush.island.alimov.entity.animals.herbivors;

import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.plants.Grass;

import java.util.Map;

public class Deer extends Herbivore{
    public Deer(Cell currentCell, Island island) {
        super("Deer", "\uD83E\uDD8C",300, 300,20, 4, 50, currentCell, island, true,false);
    }
    @Override
    protected Map<Class<?>, Double> getFoodSources() {
        return Map.ofEntries(
                Map.entry(Grass.class, 1.0)
        );
    }
    @Override
    protected Animal createNewInstance(Cell cell, Island island) {
        return new Deer(cell,island);
    }
}
