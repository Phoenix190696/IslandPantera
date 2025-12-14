package com.javarush.island.alimov.entity.animals.predators;

import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.animals.herbivors.*;


import java.util.Map;

public class Wolf extends Predator {

    public Wolf(Cell currentCell, Island island) {
        super("Wolf", " \uD83D\uDC3A", 50, 50, 30, 3, 8, currentCell, island, true, false);
    }

    @Override
    protected Animal createNewInstance(Cell cell, Island island) {
        return new Wolf(cell, island);
    }

    @Override
    protected Map<Class<?>, Double> getFoodSources() {
        return Map.ofEntries(
                Map.entry(Rabbit.class, 0.6),
                Map.entry(Sheep.class, 0.7),
                Map.entry(Deer.class, 0.15),
                Map.entry(Horse.class, 0.1),
                Map.entry(Mouse.class, 0.8),
                Map.entry(Goat.class, 0.6),
                Map.entry(Boar.class, 0.15),
                Map.entry(Buffalo.class, 0.1),
                Map.entry(Duck.class, 0.4)
        );
    }
}

