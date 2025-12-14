package com.javarush.island.alimov.entity.animals.predators;

import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.animals.herbivors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Eagle extends Predator{
    public Eagle(Cell currentCell, Island island) {
        super("Eagle", " \uD83E\uDD85",6, 6,20, 3, 1, currentCell, island, true,false);
    }

    @Override
    protected Animal createNewInstance(Cell cell, Island island) {
        return new Eagle(cell,island);
    }
    @Override
    protected Map<Class<?>, Double> getFoodSources() {
        return Map.ofEntries(
                Map.entry(Rabbit.class, 0.2),
                Map.entry(Fox.class, 0.15),
                Map.entry(Mouse.class, 0.4),
                Map.entry(Duck.class, 0.1)
        );
    }
}
