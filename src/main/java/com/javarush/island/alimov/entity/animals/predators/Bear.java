package com.javarush.island.alimov.entity.animals.predators;
import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.animals.herbivors.*;
import java.util.Map;


public class Bear extends Predator{
    public Bear(Cell currentCell, Island island) {
        super("Bear", " \uD83D\uDC3B",500, 500,5, 2, 80, currentCell, island, true,false);
    }

    @Override
    protected Animal createNewInstance(Cell cell, Island island) {
        return new Bear(cell,island);
    }
    @Override
    protected Map<Class<?>, Double> getFoodSources() {
        return Map.ofEntries(
                Map.entry(Boa.class, 0.8),
                Map.entry(Horse.class, 0.4),
                Map.entry(Deer.class, 0.8),
                Map.entry(Rabbit.class, 0.8),
                Map.entry(Mouse.class, 0.9),
                Map.entry(Goat.class, 0.7),
                Map.entry(Sheep.class, 0.7),
                Map.entry(Boar.class, 0.5),
                Map.entry(Buffalo.class, 0.2),
                Map.entry(Duck.class, 0.1)
        );
    }
}
