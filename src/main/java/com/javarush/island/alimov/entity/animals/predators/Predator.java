package com.javarush.island.alimov.entity.animals.predators;

import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.animals.Animal;

public abstract class Predator extends Animal {

    public Predator(String name, String icon, double currentWeight, double maxWeight, int maxPerCell, int speed, double foodForFullSatiety, Cell currentCell, Island island, boolean isAlive, boolean isHungry) {
        super(name, icon, currentWeight, maxWeight, maxPerCell, speed, foodForFullSatiety, currentCell, island, isAlive, isHungry);
    }
}
