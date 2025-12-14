package com.javarush.island.alimov.entity.animals;
import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Configuration;
import com.javarush.island.alimov.Island;
import com.javarush.island.alimov.entity.plants.Plant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Animal implements Runnable {
    protected final String name;
    protected final String icon;
    protected volatile double currentWeight;
    protected final double maxWeight;
    protected final int maxPerCell;
    protected final int speed;
    protected final double foodForFullSatiety;
    protected volatile Cell currentCell;
    protected final Island island;
    protected volatile boolean isAlive;
    protected int age = 0;
    protected boolean isHungry = false;

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }
    protected final ReentrantLock lock = new ReentrantLock();

    public Animal(String name, String icon, double currentWeight, double maxWeight, int maxPerCell,
                  int speed, double foodForFullSatiety, Cell currentCell, Island island, boolean isAlive, boolean isHungry) {
        this.name = name;
        this.icon = icon;
        this.currentWeight = currentWeight;
        this.maxWeight = maxWeight;
        this.maxPerCell = maxPerCell;
        this.speed = speed;
        this.foodForFullSatiety = foodForFullSatiety;
        this.currentCell = currentCell;
        this.island = island;
        this.isAlive = isAlive;
        this.isHungry = false;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public int getMaxPerCell() {
        return maxPerCell;
    }

    public int getSpeed() {
        return speed;
    }

    public double getFoodForFullSatiety() {
        return foodForFullSatiety;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public Island getIsland() {
        return island;
    }

    public boolean isAlive() {
        return isAlive;
    }

    protected abstract Map<Class<?>, Double> getFoodSources();
    protected abstract Animal createNewInstance(Cell cell, Island island);


    public void eat(Cell cell) {
        lock.lock();
        try {
            if (!isAlive) return;

            Map<Class<?>, Double> foodSources = getFoodSources();

            for (Map.Entry<Class<?>, Double> entry : foodSources.entrySet()) {
                Class<?> foodType = entry.getKey();
                double chance = entry.getValue();
                if (Animal.class.isAssignableFrom(foodType)) {
                    List<Animal> preyList = cell.getAnimals().getOrDefault(foodType, List.of());
                    for (Animal prey : new ArrayList<>(preyList)) {
                        if (prey.isAlive() && ThreadLocalRandom.current().nextDouble() < chance) {
                            consume(prey, cell);
                            return;
                        }
                    }
                } else if (Plant.class.isAssignableFrom(foodType)) {
                    List<Plant> plantList = cell.getPlants().getOrDefault(foodType, List.of());
                    for (Plant plant : new ArrayList<>(plantList)) {
                        if (plant.isAlive() && ThreadLocalRandom.current().nextDouble() < chance) {
                            consume(plant, cell);
                            return;
                        }
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }
    private void consume(Animal prey, Cell cell) {
        double weightPrey = prey.getCurrentWeight();
        prey.setCurrentWeight(weightPrey - this.foodForFullSatiety);
        this.currentWeight = Math.min(this.currentWeight + this.foodForFullSatiety, this.maxWeight);

        if (prey.getCurrentWeight() <= 0) {
            prey.setAlive(false);
            cell.removeAnimal(prey);
        }
        this.isHungry = false;
    }

    private void consume(Plant plant, Cell cell) {
        double weightPlant = plant.getCurrentWeight();
        plant.setCurrentWeight(weightPlant - this.foodForFullSatiety);
        this.currentWeight = Math.min(this.currentWeight + this.foodForFullSatiety, this.maxWeight);

        if (plant.getCurrentWeight() <= 0) {
            plant.setAlive(false);
            cell.removePlant(plant);
        }
        this.isHungry = false;
    }



    public void move(Cell source, Cell destination) {
        lock.lock();
        try {
            if (!isAlive) return;
            if (destination == null) return;
            if (destination.hasSpaceForAnimal(this)) {
                source.removeAnimal(this);
                destination.addAnimal(this);

                this.currentCell = destination;
            }
        } finally {
            lock.unlock();
        }
    }

    public void reproduce(Cell cell) {
        lock.lock();
        try {
            if (!isAlive) return;
            if (this.isHungry == true) return;

            List<Animal> sameSpecies = cell.getAnimals().getOrDefault(this.getClass(), List.of());
            if (sameSpecies.size() > 1) {
                if (this.currentWeight >= this.maxWeight * 0.5) {
                    if (ThreadLocalRandom.current().nextDouble() < Configuration.ANIMAL_CELL_SEX_CHANCE) {
                        if (ThreadLocalRandom.current().nextDouble() < Configuration.ANIMAL_CELL_REPRODUCE_CHANCE) {
                            int count = ThreadLocalRandom.current()
                                    .nextInt(Configuration.MAX_ANIMALS_REPRODUCE_IN_1_TICK) + 1;
                            for (int i = 0; i < count; i++) {
                                if (cell.hasSpaceForAnimal(this)) {
                                    Animal child = createNewInstance(cell, island);
                                    cell.addAnimal(child);
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public Cell getRandomCellWithinSpeed(Cell currentCell, int speed, Cell[][] location) {
        int currentX = currentCell.getX();
        int currentY = currentCell.getY();
        int deltaX = ThreadLocalRandom.current().nextInt(-speed, speed + 1);
        int deltaY = ThreadLocalRandom.current().nextInt(-speed, speed + 1);
        int newX = Math.max(0, Math.min(Configuration.SIZE_ISLAND_FOR_X - 1, currentX + deltaX));
        int newY = Math.max(0, Math.min(Configuration.SIZE_ISLAND_FOR_Y - 1, currentY + deltaY));
        return location[newX][newY];
    }

    @Override
    public void run() {
        lock.lock();
        this.isHungry = true;
        this.currentWeight -= (this.maxWeight * 0.2);
        this.age += 1;
        try {
            if (!isAlive) return;
            eat(currentCell);
            Cell destination = getRandomCellWithinSpeed(currentCell, this.speed, island.getLocation());
            move(currentCell, destination);
            reproduce(currentCell);
            if (currentWeight <= 0 || age >= Configuration.MAX_AGE_ANIMALS) {
                isAlive = false;
                currentCell.removeAnimal(this);
            }
        } finally {
            lock.unlock();
        }
    }
}

