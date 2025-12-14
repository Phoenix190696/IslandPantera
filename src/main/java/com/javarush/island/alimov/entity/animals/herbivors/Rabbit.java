package com.javarush.island.alimov.entity.animals.herbivors;

import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.plants.Grass;

import java.util.Map;


public class Rabbit extends Herbivore{
    public Rabbit(Cell currentCell, Island island) {
        super("Rabbit", "\uD83D\uDC07", 2, 2, 150, 2, 0.45, currentCell, island, true,false);
    }
    @Override
    protected Map<Class<?>, Double> getFoodSources() {
        return Map.ofEntries(
                Map.entry(Grass.class, 1.0)
        );
    }

    @Override
    protected Animal createNewInstance(Cell cell, Island island) {
        return new Rabbit(cell,island);
    }
}
