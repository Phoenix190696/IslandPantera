package com.javarush.island.alimov.entity.animals.herbivors;

import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.plants.Grass;

import java.util.Map;

public class Duck extends Herbivore{
    public Duck(Cell currentCell, Island island) {
        super("Duck", "\uD83E\uDD86", 1, 1, 200, 4, 0.15, currentCell, island, true,false);
    }
    @Override
    protected Map<Class<?>, Double> getFoodSources() {
        return Map.ofEntries(
                Map.entry(Grass.class, 1.0),
                Map.entry(Caterpillar.class, 0.9)
        );
    }

    @Override
    protected Animal createNewInstance(Cell cell, Island island) {
        return new Duck(cell,island);
    }
}
