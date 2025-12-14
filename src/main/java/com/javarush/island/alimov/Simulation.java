package com.javarush.island.alimov;

import com.javarush.island.alimov.entity.animals.Animal;
import com.javarush.island.alimov.entity.animals.herbivors.*;
import com.javarush.island.alimov.entity.animals.predators.*;
import com.javarush.island.alimov.entity.plants.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Simulation {
   static int counter=0;
    public static void main(String[] args) {
        Configuration config=new Configuration();
        Island island=new Island(Configuration.SIZE_ISLAND_FOR_X, Configuration.SIZE_ISLAND_FOR_Y);
        init(island);
        Statistics statistics=new Statistics(island);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(Configuration.MAX_THREADS);
        executor.scheduleAtFixedRate(() -> {
            island.runPlants();
        }, 0, config.TICK_SIMULATION, Configuration.TIME_UNIT);
        executor.scheduleAtFixedRate(() -> {
            island.runAnimals();
        }, 0, config.TICK_SIMULATION, Configuration.TIME_UNIT);
        executor.scheduleAtFixedRate(() -> {
            System.out.println("Step "+counter);
            statistics.printStatistics(Grass.class);
            statistics.printStatistics(Boar.class);
            statistics.printStatistics(Buffalo.class);
            statistics.printStatistics(Caterpillar.class);
            statistics.printStatistics(Deer.class);
            statistics.printStatistics(Duck.class);
            statistics.printStatistics(Goat.class);
            statistics.printStatistics(Horse.class);
            statistics.printStatistics(Mouse.class);
            statistics.printStatistics(Rabbit.class);
            statistics.printStatistics(Sheep.class);
            statistics.printStatistics(Bear.class);
            statistics.printStatistics(Boa.class);
            statistics.printStatistics(Fox.class);
            statistics.printStatistics(Wolf.class);
            counter++;
            checkEndCondition(island, executor);
        }, 0, config.TICK_SIMULATION, Configuration.TIME_UNIT);
    }

    public static void init(Island island){
        createPlantsRandom(island, Grass.class,100);
        createAnimalsRandom(island, Boar.class,50);
        createAnimalsRandom(island, Buffalo.class,10);
        createAnimalsRandom(island, Caterpillar.class,1000);
        createAnimalsRandom(island, Deer.class,20);
        createAnimalsRandom(island, Duck.class,200);
        createAnimalsRandom(island, Goat.class,140);
        createAnimalsRandom(island, Horse.class,20);
        createAnimalsRandom(island, Mouse.class,500);
        createAnimalsRandom(island, Rabbit.class,150);
        createAnimalsRandom(island, Sheep.class,140);
        createAnimalsRandom(island, Bear.class,5);
        createAnimalsRandom(island, Boa.class,30);
        createAnimalsRandom(island, Fox.class,30);
        createAnimalsRandom(island, Wolf.class,30);
    }
    public static <T extends Animal> void createAnimalsRandom(Island island,
                                                          Class<T> animalClass,
                                                          int count) {
        for (int i = 0; i < count; i++) {
            Cell cell = island.getRandomCell();
            try {
                T animal = animalClass
                        .getConstructor(Cell.class, Island.class)
                        .newInstance(cell, island);
                cell.addAnimal(animal);
            } catch (Exception e) {
                System.out.println("Ошибка при создании животного: " + e);
                e.printStackTrace();
            }
        }
    }
    public static <T extends Plant> void createPlantsRandom(Island island,
                                                         Class<T> plantClass,
                                                         int count) {
        for (int i = 0; i < count; i++) {
            Cell cell = island.getRandomCell();
            try {
                T plant = plantClass
                        .getConstructor(Cell.class, Island.class)
                        .newInstance(cell, island);
                cell.addPlant(plant);
            } catch (Exception e) {
                System.out.println("Ошибка при создании животного: " + e);
                e.printStackTrace();
            }
        }
    }
    public static void checkEndCondition(Island island, ScheduledExecutorService executor) {
        boolean hasAnimals = island.getAllAnimals().stream().anyMatch(Animal::isAlive);
        boolean hasPlants = island.getAllPlants().stream().anyMatch(Plant::isAlive);
        if (!hasAnimals && !hasPlants) {
            System.out.println("Симуляция завершена: все животные погибли и растений не осталось.");
            executor.shutdown();
        }
    }
}
