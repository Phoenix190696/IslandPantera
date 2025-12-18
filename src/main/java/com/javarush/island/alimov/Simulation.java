package com.javarush.island.alimov;
// Пакет, в котором находится класс Simulation.

import com.javarush.island.alimov.entity.animals.Animal;
// Импорт базового класса Animal.

import com.javarush.island.alimov.entity.animals.herbivors.*;
// Импорт всех травоядных животных.

import com.javarush.island.alimov.entity.animals.predators.*;
// Импорт всех хищников.

import com.javarush.island.alimov.entity.plants.*;
// Импорт всех растений.

import java.util.concurrent.Executors;
// Импорт класса Executors для создания потоков.

import java.util.concurrent.ScheduledExecutorService;
// Импорт планировщика задач с фиксированным интервалом.
/**
 * Класс {@code Simulation} отвечает за запуск и управление симуляцией острова.
 * Он инициализирует животных и растения, запускает их жизненные процессы
 * в многопоточном режиме и выводит статистику на каждом шаге.
 *
 * <p>Основные возможности:
 * <ul>
 *     <li>Инициализация острова с заданным количеством животных и растений.</li>
 *     <li>Запуск процессов роста растений и активности животных.</li>
 *     <li>Вывод статистики по видам на каждом шаге симуляции.</li>
 *     <li>Проверка условий завершения симуляции.</li>
 * </ul>
 *
 * Пример использования:
 * <pre>{@code
 * public static void main(String[] args) {
 *     Simulation.main(args);
 * }
 * }</pre>
 */
public class Simulation {
    // Главный класс, запускающий симуляцию острова.

    static int counter=0;
    // Счётчик шагов симуляции.

    public static void main(String[] args) {
        // Точка входа в программу.
        Configuration config=new Configuration();
        // Создаём объект конфигурации (параметры симуляции).
        Island island=new Island(Configuration.SIZE_ISLAND_FOR_X, Configuration.SIZE_ISLAND_FOR_Y);
        // Создаём остров с заданными размерами.
        init(island);
        // Инициализируем остров (заселяем животными и растениями).
        Statistics statistics=new Statistics(island);
        // Создаём объект для подсчёта статистики.
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(Configuration.MAX_THREADS);
        // Создаём пул потоков для выполнения задач с фиксированным интервалом.

        executor.scheduleAtFixedRate(() -> {
            // Планируем задачу для роста растений.
            island.runPlants();
        }, 0, config.TICK_SIMULATION, Configuration.TIME_UNIT);
        // Запускаем каждые TICK_SIMULATION единиц времени.

        executor.scheduleAtFixedRate(() -> {
            // Планируем задачу для активности животных.
            island.runAnimals();
        }, 0, config.TICK_SIMULATION, Configuration.TIME_UNIT);
        // Запускаем каждые TICK_SIMULATION единиц времени.

        executor.scheduleAtFixedRate(() -> {
            // Планируем задачу для вывода статистики.
            System.out.println("Step "+counter);
            // Выводим номер шага симуляции.
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
            // Выводим статистику по всем видам животных и растений.
            counter++;
            // Увеличиваем счётчик шагов.
            checkEndCondition(island, executor);
            // Проверяем условие завершения симуляции.
        }, 0, config.TICK_SIMULATION, Configuration.TIME_UNIT);
        // Запускаем каждые TICK_SIMULATION единиц времени.
    }
    /**
     * Инициализирует остров, создавая заданное количество животных и растений.
     *
     * @param island объект острова
     */
    public static void init(Island island){
        // Метод для начальной инициализации острова.
        createPlantsRandom(island, Grass.class,100);
        // Создаём 100 растений травы.
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
        // Заселяем остров животными разных видов в указанном количестве.
    }
    /**
     * Создаёт указанное количество животных случайным образом на острове.
     *
     * @param island      объект острова
     * @param animalClass класс животного
     * @param count       количество создаваемых животных
     * @param <T>         тип животного
     */
    public static <T extends Animal> void createAnimalsRandom(Island island,
                                                              Class<T> animalClass,
                                                              int count) {
        // Метод для случайного размещения животных на острове.
        for (int i = 0; i < count; i++) {
            // Цикл для создания указанного количества животных.
            Cell cell = island.getRandomCell();
            // Получаем случайную клетку острова.
            try {
                T animal = animalClass
                        .getConstructor(Cell.class, Island.class)
                        .newInstance(cell, island);
                // Создаём новый объект животного через рефлексию.
                cell.addAnimal(animal);
                // Добавляем животное в клетку.
            } catch (Exception e) {
                // Обработка ошибок при создании.
                System.out.println("Ошибка при создании животного: " + e);
                e.printStackTrace();
            }
        }
    }
    /**
     * Создаёт указанное количество растений случайным образом на острове.
     *
     * @param island     объект острова
     * @param plantClass класс растения
     * @param count      количество создаваемых растений
     * @param <T>        тип растения
     */
    public static <T extends Plant> void createPlantsRandom(Island island,
                                                            Class<T> plantClass,
                                                            int count) {
        // Метод для случайного размещения растений на острове.
        for (int i = 0; i < count; i++) {
            // Цикл для создания указанного количества растений.
            Cell cell = island.getRandomCell();
            // Получаем случайную клетку острова.
            try {
                T plant = plantClass
                        .getConstructor(Cell.class, Island.class)
                        .newInstance(cell, island);
                // Создаём новый объект растения через рефлексию.
                cell.addPlant(plant);
                // Добавляем растение в клетку.
            } catch (Exception e) {
                // Обработка ошибок при создании.
                System.out.println("Ошибка при создании животного: " + e);
                e.printStackTrace();
            }
        }
    }
    /**
     * Проверяет условие завершения симуляции.
     * <p>Если на острове не осталось живых животных и растений,
     * симуляция завершается.</p>
     *
     * @param island   объект острова
     * @param executor планировщик задач, который будет остановлен
     */
    public static void checkEndCondition(Island island, ScheduledExecutorService executor) {
        // Метод для проверки завершения симуляции.
        boolean hasAnimals = island.getAllAnimals().stream().anyMatch(Animal::isAlive);
        // Проверяем, есть ли живые животные.
        boolean hasPlants = island.getAllPlants().stream().anyMatch(Plant::isAlive);
        // Проверяем, есть ли живые растения.
        if (!hasAnimals && !hasPlants) {
            // Если животных и растений не осталось:
            System.out.println("Симуляция завершена: все животные погибли и растений не осталось.");
            // Выводим сообщение о завершении.
            executor.shutdown();
            // Останавливаем выполнение всех задач.
        }
    }
}

