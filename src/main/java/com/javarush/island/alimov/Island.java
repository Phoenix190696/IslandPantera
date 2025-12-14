package com.javarush.island.alimov;

import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.plants.Plant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Island {
    private final Cell[][] location;

    public Island(int sizeX, int sizeY) {
        location = new Cell[sizeX][sizeY];
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                location[x][y] = new Cell(x, y);
            }
        }
    }

    public Cell[][] getLocation() {
        return location;
    }

    public Cell getCell(int x, int y) {
        if (x >= 0 && x < Configuration.SIZE_ISLAND_FOR_X &&
                y >= 0 && y < Configuration.SIZE_ISLAND_FOR_Y) {
            return location[x][y];
        }
        return null;
    }
    public List<Animal> getAllAnimals() {
        return Arrays.stream(location)
                .flatMap(Arrays::stream)
                .flatMap(cell -> cell.getAnimals().values().stream().flatMap(List::stream))
                .collect(Collectors.toList());
    }

    public List<Plant> getAllPlants() {
        return Arrays.stream(location)
                .flatMap(Arrays::stream)
                .flatMap(cell -> cell.getPlants().values().stream().flatMap(List::stream))
                .collect(Collectors.toList());
    }


    public void run(Object o) {
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[i].length; j++) {
                location[i][j].run(o);
            }
        }
    }
    public void runPlants() {
        for (int y = 0; y < location.length; y++) {
            for (int x = 0; x < location[y].length; x++) {
                location[y][x].runAllPlants();
            }
        }
    }
    public void runAnimals() {
        for (int y = 0; y < location.length; y++) {
            for (int x = 0; x < location[y].length; x++) {
                location[y][x].runAllAnimals();
            }
        }
    }

    public List<Cell> getNeighbors(int x, int y) {
        List<Cell> neighbors = new ArrayList<>();

        if (y > 0) {
            neighbors.add(location[x][y - 1]);
        }
        if (y < Configuration.SIZE_ISLAND_FOR_Y - 1) {
            neighbors.add(location[x][y + 1]);
        }
        if (x > 0) {
            neighbors.add(location[x - 1][y]);
        }
        if (x < Configuration.SIZE_ISLAND_FOR_X - 1) {
            neighbors.add(location[x + 1][y]);
        }

        return neighbors;
    }

    public Cell getRandomCell() {
        int x = ThreadLocalRandom.current().nextInt(Configuration.SIZE_ISLAND_FOR_X);
        int y = ThreadLocalRandom.current().nextInt(Configuration.SIZE_ISLAND_FOR_Y);
        return location[x][y];
    }
}
