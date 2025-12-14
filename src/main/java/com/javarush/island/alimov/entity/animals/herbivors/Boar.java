package com.javarush.island.alimov.entity.animals.herbivors;

import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.plants.Grass;

import java.util.Map;

public class Boar extends Herbivore{
    public Boar(Cell currentCell, Island island) {
        super("Boar", "\uD83D\uDC17", 400, 400, 50, 2, 50, currentCell, island, true,false);
    }
    @Override
    protected Map<Class<?>, Double> getFoodSources() {
        return Map.ofEntries(
                Map.entry(Grass.class, 1.0),
                Map.entry(Mouse.class, 0.5),
                Map.entry(Caterpillar.class, 0.9)
        );
    }

    @Override
    protected Animal createNewInstance(Cell cell, Island island) {
        return new Boar(cell,island);
    }
}
