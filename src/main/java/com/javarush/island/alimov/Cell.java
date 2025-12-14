package com.javarush.island.alimov;

import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.plants.Plant;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Cell {
    private final int X;
    private final int Y;
    private final Map<Class<? extends Animal>, List<Animal>> animals = new ConcurrentHashMap<>();
    private final Map<Class<? extends Plant>, List<Plant>> plants = new ConcurrentHashMap<>();

    public Cell(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    public int getX() { return X; }
    public int getY() { return Y; }

    public boolean hasSpaceForPlant(Plant plant) {
        List<Plant> list = plants.getOrDefault(plant.getClass(), new CopyOnWriteArrayList<>());
        return list.size() < plant.getMaxPerCell();
    }
    public boolean hasSpaceForAnimal(Animal animal) {
        List<Animal> list = animals.getOrDefault(animal.getClass(), new CopyOnWriteArrayList<>());
        return list.size() < animal.getMaxPerCell();
    }

    public void run(Object o) {
        if (o instanceof Animal) {
            List<Animal> list = animals.get(o.getClass());
            if (list != null) {
                for (Animal animal : new CopyOnWriteArrayList<>(list)) {
                    animal.run();
                }
            }
        } else if (o instanceof Plant) {
            List<Plant> list = plants.get(o.getClass());
            if (list != null) {
                for (Plant plant : new CopyOnWriteArrayList<>(list)) {
                    plant.run();
                }
            }
        }
    }

    public void runAllPlants() {
        for (List<Plant> list : plants.values()) {
            for (Plant plant : new CopyOnWriteArrayList<>(list)) {
                plant.run();
            }
        }
    }

    public void runAllAnimals() {
        for (List<Animal> list : animals.values()) {
            for (Animal animal : new CopyOnWriteArrayList<>(list)) {
                animal.run();
            }
        }
    }

    public void addPlant(Plant plant) {
        plants.computeIfAbsent(plant.getClass(), k -> new CopyOnWriteArrayList<>()).add(plant);
    }

    public void removePlant(Plant plant) {
        List<Plant> list = plants.get(plant.getClass());
        if (list != null) {
            list.remove(plant);
            if (list.isEmpty()) {
                plants.remove(plant.getClass());
            }
        }
    }

    public void addAnimal(Animal animal) {
        animals.computeIfAbsent(animal.getClass(), k -> new CopyOnWriteArrayList<>()).add(animal);
    }

    public void removeAnimal(Animal animal) {
        List<Animal> list = animals.get(animal.getClass());
        if (list != null) {
            list.remove(animal);
            if (list.isEmpty()) {
                animals.remove(animal.getClass());
            }
        }
    }

    public Map<Class<? extends Animal>, List<Animal>> getAnimals() { return animals; }
    public Map<Class<? extends Plant>, List<Plant>> getPlants() { return plants; }
}

