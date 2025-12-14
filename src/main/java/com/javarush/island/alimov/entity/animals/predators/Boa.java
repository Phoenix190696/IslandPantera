package com.javarush.island.alimov.entity.animals.predators;
import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.animals.herbivors.*;
import java.util.Map;

public class Boa extends Predator {
    public Boa(Cell currentCell, Island island) {
        super("Boa", "\uD83D\uDC0D",15, 15,30, 1, 3, currentCell, island, true,false);
    }

    @Override
    protected Animal createNewInstance(Cell cell, Island island) {
        return new Boa(cell,island);
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
