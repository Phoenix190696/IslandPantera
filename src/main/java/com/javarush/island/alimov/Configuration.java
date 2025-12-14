package com.javarush.island.alimov;
import java.util.concurrent.TimeUnit;

public class Configuration {
    public static final int SIZE_ISLAND_FOR_X=100;
    public static final int SIZE_ISLAND_FOR_Y=20;
    public static final long TICK_SIMULATION=1;
    public static final TimeUnit TIME_UNIT=TimeUnit.SECONDS;
    public static final int MAX_THREADS=4;
    public static final int MAX_PLANTS_REPRODUCE_IN_1_TICK=20000;
    public static final int MAX_ANIMALS_REPRODUCE_IN_1_TICK=2;
    public static final double NEIGHBOR_CELL_REPRODUCE_CHANCE=1.0;
    public static final double ANIMAL_CELL_REPRODUCE_CHANCE=0.5;
    public static final double ANIMAL_CELL_SEX_CHANCE=1.0;
    public static final int MAX_AGE_ANIMALS=20;
    public static final int MAX_AGE_PLANTS=20;

}
