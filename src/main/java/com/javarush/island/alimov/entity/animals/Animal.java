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

/**
 * Абстрактный класс {@code Animal} представляет животное на острове.
 * Животные обладают весом, возрастом, могут перемещаться, питаться,
 * размножаться и умирать. Класс реализует интерфейс {@link Runnable},
 * что позволяет запускать жизненный цикл животного в многопоточном режиме.
 *
 * <p>Основные возможности:
 * <ul>
 *     <li>Хранение информации о животном (имя, иконка, вес, клетка).</li>
 *     <li>Поиск и поедание пищи (растений или других животных).</li>
 *     <li>Перемещение в пределах скорости.</li>
 *     <li>Размножение при выполнении условий.</li>
 *     <li>Учёт возраста и состояния (живое/мертвое, голодное/сытое).</li>
 * </ul>
 * <p>
 * Пример использования:
 * <pre>{@code
 * Animal wolf = new Wolf("Wolf", "🐺", 30.0, 50.0, 30,
 *                        3, 8.0, cell, island, true, false);
 * wolf.run(); // запускает жизненный цикл волка
 * }</pre>
 */
public abstract class Animal implements Runnable {
    protected final String name; // Имя животного
    protected final String icon; // Иконка (символ) животного
    protected volatile double currentWeight; // Текущий вес животного (volatile — для многопоточности)
    protected final double maxWeight; // Максимальный вес животного
    protected final int maxPerCell; // Максимальное количество животных данного вида в одной клетке
    protected final int speed; // Скорость передвижения (максимальное количество клеток за шаг)
    protected final double foodForFullSatiety; // Количество пищи для полного насыщения
    protected volatile Cell currentCell; // Текущая клетка, где находится животное
    protected final Island island; // Ссылка на остров
    protected volatile boolean isAlive; // Флаг — живо ли животное
    protected int age = 0; // Возраст животного
    protected boolean isHungry = false; // Флаг — голодно ли животное

    public void setAlive(boolean alive) { // Сеттер для состояния жизни
        isAlive = alive;
    }

    public void setCurrentWeight(double currentWeight) { // Сеттер для текущего веса
        this.currentWeight = currentWeight;
    }

    protected final ReentrantLock lock = new ReentrantLock(); // Блокировка для потокобезопасности

    /**
     * Конструктор для создания животного.
     *
     * @param name               имя животного
     * @param icon               иконка животного
     * @param currentWeight      текущий вес
     * @param maxWeight          максимальный вес
     * @param maxPerCell         максимальное количество животных в клетке
     * @param speed              скорость передвижения
     * @param foodForFullSatiety количество пищи для насыщения
     * @param currentCell        клетка, где находится животное
     * @param island             остров
     * @param isAlive            состояние животного (живое/мертвое)
     * @param isHungry           состояние голода (true — голодно, false — сыто)
     */
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
        this.isHungry = false; // Животное создаётся не голодным
    }

    // Геттеры для полей
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

    /**
     * Источники пищи для животного.
     * <p>Карта: класс пищи → вероятность успешного поедания.</p>
     *
     * @return карта источников пищи
     */
    protected abstract Map<Class<?>, Double> getFoodSources(); // Источники пищи (класс → вероятность поедания)

    /**
     * Создание нового экземпляра животного (для размножения).
     *
     * @param cell   клетка, где будет создано животное
     * @param island остров
     * @return новый экземпляр животного
     */
    protected abstract Animal createNewInstance(Cell cell, Island island); // Создание нового экземпляра животного (для размножения)

    /**
     * Поиск и поедание пищи в клетке.
     *
     * @param cell клетка, где животное ищет пищу
     */
    public void eat(Cell cell) {
        lock.lock(); // Блокируем доступ для потокобезопасности
        try {
            if (!isAlive) return; // Если животное мертво — ничего не делаем

            Map<Class<?>, Double> foodSources = getFoodSources(); // Получаем список источников пищи

            for (Map.Entry<Class<?>, Double> entry : foodSources.entrySet()) { // Перебираем все источники пищи
                Class<?> foodType = entry.getKey(); // Тип пищи (животное или растение)
                double chance = entry.getValue(); // Вероятность успешного поедания

                if (Animal.class.isAssignableFrom(foodType)) { // Если пища — другое животное
                    List<Animal> preyList = cell.getAnimals().getOrDefault(foodType, List.of()); // Получаем список животных данного типа
                    for (Animal prey : new ArrayList<>(preyList)) { // Перебираем всех жертв
                        if (prey.isAlive() && ThreadLocalRandom.current().nextDouble() < chance) { // Если жертва жива и сработала вероятность
                            consume(prey, cell); // Съедаем жертву
                            return;
                        }
                    }
                } else if (Plant.class.isAssignableFrom(foodType)) { // Если пища — растение
                    List<Plant> plantList = cell.getPlants().getOrDefault(foodType, List.of()); // Получаем список растений
                    for (Plant plant : new ArrayList<>(plantList)) {
                        if (plant.isAlive() && ThreadLocalRandom.current().nextDouble() < chance) { // Если растение живо и вероятность сработала
                            consume(plant, cell); // Съедаем растение
                            return;
                        }
                    }
                }
            }
        } finally {
            lock.unlock(); // Освобождаем блокировку
        }
    }

    /**
     * Метод поедания животного-жертвы.
     * <p>Уменьшает вес жертвы, увеличивает вес хищника,
     * проверяет условие смерти жертвы и удаляет её из клетки при необходимости.</p>
     *
     * @param prey животное-жертва
     * @param cell клетка, где происходит поедание
     */
    private void consume(Animal prey, Cell cell) {
        double weightPrey = prey.getCurrentWeight(); // Вес жертвы
        prey.setCurrentWeight(weightPrey - this.foodForFullSatiety); // Уменьшаем вес жертвы
        this.currentWeight = Math.min(this.currentWeight + this.foodForFullSatiety, this.maxWeight); // Увеличиваем вес хищника

        if (prey.getCurrentWeight() <= 0) { // Если жертва полностью съедена
            prey.setAlive(false); // Жертва умирает
            cell.removeAnimal(prey); // Удаляем её из клетки
        }
        this.isHungry = false; // Хищник больше не голоден
    }

    /**
     * Метод поедания растения.
     * <p>Уменьшает вес растения, увеличивает вес животного,
     * проверяет условие смерти растения и удаляет его из клетки при необходимости.</p>
     *
     * @param plant растение-жертва
     * @param cell  клетка, где происходит поедание
     */
    private void consume(Plant plant, Cell cell) {
        double weightPlant = plant.getCurrentWeight(); // Вес растения
        plant.setCurrentWeight(weightPlant - this.foodForFullSatiety); // Уменьшаем вес растения
        this.currentWeight = Math.min(this.currentWeight + this.foodForFullSatiety, this.maxWeight); // Увеличиваем вес животного

        if (plant.getCurrentWeight() <= 0) { // Если растение полностью съедено
            plant.setAlive(false); // Растение умирает
            cell.removePlant(plant); // Удаляем его из клетки
        }
        this.isHungry = false; // Животное больше не голодно
    }

    /**
     * Размножение животного при выполнении условий.
     *
     * @param cell клетка, где находится животное
     */
    public void move(Cell source, Cell destination) {
        lock.lock();
        try {
            if (!isAlive) return; // Если мёртвое — не двигается
            if (destination == null) return; // Если нет клетки назначения — не двигается
            if (destination.hasSpaceForAnimal(this)) { // Если в клетке есть место для животного
                source.removeAnimal(this); // Убираем из текущей клетки
                destination.addAnimal(this); // Добавляем в новую клетку
                this.currentCell = destination; // Обновляем текущую клетку
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Размножение животного при выполнении условий.
     *
     * @param cell клетка, где находится животное
     */
    public void reproduce(Cell cell) {
        lock.lock();
        try {
            if (!isAlive) return; // Мёртвое животное не размножается
            if (this.isHungry == true) return; // Голодное животное не размножается

            List<Animal> sameSpecies = cell.getAnimals().getOrDefault(this.getClass(), List.of()); // Получаем список животных того же вида
            if (sameSpecies.size() > 1) { // Если есть хотя бы два животных одного вида
                if (this.currentWeight >= this.maxWeight * 0.5) { // Если вес достаточный (>= 50% от максимума)
                    if (ThreadLocalRandom.current().nextDouble() < Configuration.ANIMAL_CELL_SEX_CHANCE) { // Вероятность спаривания
                        if (ThreadLocalRandom.current().nextDouble() < Configuration.ANIMAL_CELL_REPRODUCE_CHANCE) { // Вероятность успешного размножения
                            int count = ThreadLocalRandom.current()
                                    .nextInt(Configuration.MAX_ANIMALS_REPRODUCE_IN_1_TICK) + 1; // Количество потомков
                            for (int i = 0; i < count; i++) {
                                if (cell.hasSpaceForAnimal(this)) { // Если есть место для потомка
                                    Animal child = createNewInstance(cell, island); // Создаём нового ребёнка
                                    cell.addAnimal(child); // Добавляем его в клетку
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

    /**
     * Получение случайной клетки в пределах скорости.
     *
     * @param currentCell текущая клетка
     * @param speed       скорость передвижения
     * @param location    карта острова
     * @return случайная клетка в пределах скорости
     */
    public Cell getRandomCellWithinSpeed(Cell currentCell, int speed, Cell[][] location) {
        int currentX = currentCell.getX(); // Получаем текущую координату X клетки
        int currentY = currentCell.getY(); // Получаем текущую координату Y клетки

        // Генерируем случайное смещение по X в пределах [-speed, speed]
        int deltaX = ThreadLocalRandom.current().nextInt(-speed, speed + 1);
        // Генерируем случайное смещение по Y в пределах [-speed, speed]
        int deltaY = ThreadLocalRandom.current().nextInt(-speed, speed + 1);

        // Вычисляем новую координату X, ограничивая её границами острова
        int newX = Math.max(0, Math.min(Configuration.SIZE_ISLAND_FOR_X - 1, currentX + deltaX));
        // Вычисляем новую координату Y, ограничивая её границами острова
        int newY = Math.max(0, Math.min(Configuration.SIZE_ISLAND_FOR_Y - 1, currentY + deltaY));

        return location[newX][newY]; // Возвращаем клетку по новым координатам
    }

    /**
     * Жизненный цикл животного.
     * <p>Животное становится голодным, теряет вес, стареет,
     * пытается поесть, перемещается, размножается и проверяет условия смерти.</p>
     */
    @Override
    public void run() { // Метод run() — логика поведения животного в одном "тике" симуляции
        lock.lock(); // Блокируем доступ для потокобезопасности
        this.isHungry = true; // Животное становится голодным
        this.currentWeight -= (this.maxWeight * 0.2); // Животное теряет часть веса (20% от максимального)
        this.age += 1; // Увеличиваем возраст животного на 1

        try {
            if (!isAlive) return; // Если животное мертво — прекращаем выполнение

            eat(currentCell); // Пытаемся поесть в текущей клетке

            // Выбираем случайную клетку в пределах скорости
            Cell destination = getRandomCellWithinSpeed(currentCell, this.speed, island.getLocation());

            move(currentCell, destination); // Перемещаемся в выбранную клетку
            reproduce(currentCell); // Пытаемся размножиться в текущей клетке

            // Проверяем условия смерти: вес <= 0 или возраст >= максимального
            if (currentWeight <= 0 || age >= Configuration.MAX_AGE_ANIMALS) {
                isAlive = false; // Животное умирает
                currentCell.removeAnimal(this); // Удаляем животное из клетки
            }
        } finally {
            lock.unlock(); // Освобождаем блокировку
        }
    }
}

