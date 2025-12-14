package com.javarush.island.alimov.entity.plants;

import com.javarush.island.alimov.Cell;
import com.javarush.island.alimov.Configuration;
import com.javarush.island.alimov.Island;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Plant implements Runnable {
    protected final String name;
    protected final String icon;
    protected volatile double currentWeight;
    protected final int maxPerCell;
    protected volatile Cell currentCell;
    protected final Island island;
    protected volatile boolean isAlive;
    protected int age=0;

    protected final ReentrantLock lock = new ReentrantLock();

    public Plant(String name, String icon, double currentWeight, int maxPerCell,
                 Cell currentCell, Island island, boolean isAlive) {
        this.name = name;
        this.icon = icon;
        this.currentWeight = currentWeight;
        this.maxPerCell = maxPerCell;
        this.currentCell = currentCell;
        this.island = island;
        this.isAlive = isAlive;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public String getName() { return name; }
    public String getIcon() { return icon; }
    public double getCurrentWeight() { return currentWeight; }
    public int getMaxPerCell() { return maxPerCell; }
    public Cell getCurrentCell() { return currentCell; }
    public Island getIsland() { return island; }
    public boolean isAlive() { return isAlive; }

    protected abstract Plant createNewInstance(Cell cell, Island island);

    public void reproduce(Cell cell, Island island) {
        lock.lock();
        try {
            int rnd = ThreadLocalRandom.current()
                    .nextInt(Configuration.MAX_PLANTS_REPRODUCE_IN_1_TICK);

            if (!cell.hasSpaceForPlant(this)) return;

            for (int i = 0; i <= rnd; i++) {
                if (this.age >= 2) return;
                if (ThreadLocalRandom.current().nextDouble() < Configuration.NEIGHBOR_CELL_REPRODUCE_CHANCE) {
                    Cell neighbor = getRandomNeighborCell(cell, island);
                    if (neighbor != null && neighbor.hasSpaceForPlant(this)) {
                        neighbor.addPlant(createNewInstance(neighbor, island));
                    }
                } else {
                    cell.addPlant(createNewInstance(cell, island));
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        lock.lock();
        this.age++;
        try {
            if (!isAlive) return;

            reproduce(currentCell, island);

            if (this.currentWeight <= 0||this.age>=Configuration.MAX_AGE_PLANTS) {
                isAlive = false;
                currentCell.removePlant(this);
            }
        } finally {
            lock.unlock();
        }
    }

    private Cell getRandomNeighborCell(Cell cell, Island island) {
        List<Cell> neighbors = island.getNeighbors(cell.getX(), cell.getY());
        if (neighbors == null || neighbors.isEmpty()) {
            return null;
        }
        return neighbors.get(ThreadLocalRandom.current().nextInt(neighbors.size()));
    }
}

