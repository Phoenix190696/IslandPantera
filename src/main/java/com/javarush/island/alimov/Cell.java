package com.javarush.island.alimov;
// Пакет, в котором находится класс Cell.

import com.javarush.island.alimov.entity.animals.Animal;
// Импорт базового класса Animal.

import com.javarush.island.alimov.entity.plants.Plant;
// Импорт базового класса Plant.

import java.util.List;
// Импорт интерфейса List для работы со списками.

import java.util.Map;
// Импорт интерфейса Map для хранения пар ключ-значение.

import java.util.concurrent.ConcurrentHashMap;
// Импорт потокобезопасной реализации Map.

import java.util.concurrent.CopyOnWriteArrayList;
// Импорт потокобезопасного списка.

/**
 * Класс {@code Cell} представляет клетку острова.
 * В клетке могут находиться животные и растения.
 * Класс обеспечивает потокобезопасное хранение и управление объектами.
 *
 * <p>Основные возможности:
 * <ul>
 *     <li>Хранение животных и растений по их классам.</li>
 *     <li>Добавление и удаление объектов.</li>
 *     <li>Запуск жизненного цикла животных и растений.</li>
 *     <li>Проверка наличия свободного места для новых объектов.</li>
 * </ul>
 * <p>
 * Пример использования:
 * <pre>{@code
 * Cell cell = new Cell(0, 0);
 * cell.addPlant(new Grass("Grass", "🌿", 1.0, 200, cell, island, true));
 * cell.addAnimal(new Wolf("Wolf", "🐺", 30.0, 50.0, 30,
 *                         3, 8.0, cell, island, true, false));
 * cell.runAllAnimals(); // запускает всех животных в клетке
 * }</pre>
 */
public class Cell {
    // Класс Cell представляет клетку острова, содержащую животных и растения.

    private final int X;
    // Координата X клетки.

    private final int Y;
    // Координата Y клетки.

    private final Map<Class<? extends Animal>, List<Animal>> animals = new ConcurrentHashMap<>();
    // Потокобезопасная карта: класс животного → список животных в клетке.

    private final Map<Class<? extends Plant>, List<Plant>> plants = new ConcurrentHashMap<>();
    // Потокобезопасная карта: класс растения → список растений в клетке.

    /**
     * Конструктор клетки.
     *
     * @param x координата X
     * @param y координата Y
     */
    public Cell(int x, int y) {
        // Конструктор клетки. Инициализирует координаты.
        this.X = x;
        this.Y = y;
    }

    public int getX() {
        return X;
    }
    // Возвращает координату X клетки.

    public int getY() {
        return Y;
    }
    // Возвращает координату Y клетки.

    /**
     * Проверяет, есть ли место для растения данного вида в клетке.
     *
     * @param plant растение
     * @return {@code true}, если есть место; иначе {@code false}
     */
    public boolean hasSpaceForPlant(Plant plant) {
        // Проверяет, есть ли место для растения данного вида в клетке.
        List<Plant> list = plants.getOrDefault(plant.getClass(), new CopyOnWriteArrayList<>());
        return list.size() < plant.getMaxPerCell();
    }

    /**
     * Проверяет, есть ли место для животного данного вида в клетке.
     *
     * @param animal животное
     * @return {@code true}, если есть место; иначе {@code false}
     */
    public boolean hasSpaceForAnimal(Animal animal) {
        // Проверяет, есть ли место для животного данного вида в клетке.
        List<Animal> list = animals.getOrDefault(animal.getClass(), new CopyOnWriteArrayList<>());
        return list.size() < animal.getMaxPerCell();
    }

    /**
     * Запускает жизненный цикл конкретного объекта (животного или растения).
     *
     * @param o объект (животное или растение)
     */
    public void run(Object o) {
        // Запускает жизненный цикл конкретного объекта (животного или растения).
        if (o instanceof Animal) {
            // Если объект — животное:
            List<Animal> list = animals.get(o.getClass());
            if (list != null) {
                for (Animal animal : new CopyOnWriteArrayList<>(list)) {
                    animal.run();
                    // Запускаем метод run() для каждого животного.
                }
            }
        } else if (o instanceof Plant) {
            // Если объект — растение:
            List<Plant> list = plants.get(o.getClass());
            if (list != null) {
                for (Plant plant : new CopyOnWriteArrayList<>(list)) {
                    plant.run();
                    // Запускаем метод run() для каждого растения.
                }
            }
        }
    }

    /**
     * Запускает жизненный цикл всех растений в клетке.
     */
    public void runAllPlants() {
        // Запускает жизненный цикл всех растений в клетке.
        for (List<Plant> list : plants.values()) {
            for (Plant plant : new CopyOnWriteArrayList<>(list)) {
                plant.run();
            }
        }
    }

    /**
     * Запускает жизненный цикл всех животных в клетке.
     */
    public void runAllAnimals() {
        // Запускает жизненный цикл всех животных в клетке.
        for (List<Animal> list : animals.values()) {
            for (Animal animal : new CopyOnWriteArrayList<>(list)) {
                animal.run();
            }
        }
    }

    /**
     * Добавляет растение в клетку.
     *
     * @param plant растение
     */
    public void addPlant(Plant plant) {
        // Добавляет растение в клетку.
        plants.computeIfAbsent(plant.getClass(), k -> new CopyOnWriteArrayList<>()).add(plant);
    }

    /**
     * Удаляет растение из клетки.
     *
     * @param plant растение
     */
    public void removePlant(Plant plant) {
        // Удаляет растение из клетки.
        List<Plant> list = plants.get(plant.getClass());
        if (list != null) {
            list.remove(plant);
            if (list.isEmpty()) {
                plants.remove(plant.getClass());
                // Если список пуст — удаляем ключ из карты.
            }
        }
    }

    /**
     * Добавляет животное в клетку.
     *
     * @param animal животное
     */
    public void addAnimal(Animal animal) {
        // Добавляет животное в клетку.
        animals.computeIfAbsent(animal.getClass(), k -> new CopyOnWriteArrayList<>()).add(animal);
    }

    /**
     * Удаляет животное из клетки.
     *
     * @param animal животное
     */
    public void removeAnimal(Animal animal) {
        // Удаляет животное из клетки.
        List<Animal> list = animals.get(animal.getClass());
        if (list != null) {
            list.remove(animal);
            if (list.isEmpty()) {
                animals.remove(animal.getClass());
                // Если список пуст — удаляем ключ из карты.
            }
        }
    }

    /**
     * @return карта животных в клетке
     */
    public Map<Class<? extends Animal>, List<Animal>> getAnimals() {
        return animals;
    }
    // Возвращает карту животных в клетке.

    /**
     * @return карта растений в клетке
     */
    public Map<Class<? extends Plant>, List<Plant>> getPlants() {
        return plants;
    }
    // Возвращает карту растений в клетке.
}


