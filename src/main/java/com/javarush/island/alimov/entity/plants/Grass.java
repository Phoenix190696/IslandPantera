package com.javarush.island.alimov.entity.plants;

import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;


public class Grass extends Plant {
    public Grass(Cell currentCell, Island island) {
        super("Grass", "\uD83C\uDF3F", 1.0, 200, currentCell, island, true);
    }

    @Override
    protected Plant createNewInstance(Cell cell, Island island) {
        return new Grass(cell, island);
    }

}

