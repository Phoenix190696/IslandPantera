package com.javarush.island.alimov.entity.animals.herbivors;

import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.plants.Grass;

import java.util.Map;

public class Buffalo extends Herbivore{
    public Buffalo(Cell currentCell, Island island) {
        super("Buffalo", "\uD83D\uDC03", 700, 700, 10, 3, 100, currentCell, island, true,false);
    }
    @Override
    protected Map<Class<?>, Double> getFoodSources() {
        return Map.ofEntries(
                Map.entry(Grass.class, 1.0)
        );
    }
    @Override
    protected Animal createNewInstance(Cell cell, Island island) {
        return new Buffalo(cell,island);
    }
}
