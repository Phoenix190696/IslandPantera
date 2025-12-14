package com.javarush.island.alimov.entity.animals.predators;
import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.animals.herbivors.*;
import java.util.Map;


public class Fox extends Predator{


    public Fox(Cell currentCell, Island island) {
        super("Fox", " \uD83E\uDD8A",8, 8,30, 2, 2, currentCell, island, true,false);
    }
    @Override
    protected Map<Class<?>, Double> getFoodSources() {
        return Map.ofEntries(
                Map.entry(Rabbit.class, 0.7),
                Map.entry(Mouse.class, 0.9),
                Map.entry(Duck.class, 0.6),
                Map.entry(Caterpillar.class, 0.4)
        );
    }
    @Override
    protected Animal createNewInstance(Cell cell, Island island) {
        return new Fox(cell,island);
    }
}
