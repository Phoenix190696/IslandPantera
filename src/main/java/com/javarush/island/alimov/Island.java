package com.javarush.island.alimov;
// Пакет, в котором находится класс Island.

import com.javarush.island.alimov.entity.animals.Animal;
// Импорт базового класса Animal.

import com.javarush.island.alimov.entity.plants.Plant;
// Импорт базового класса Plant.

import java.util.ArrayList;
// Импорт класса ArrayList для работы со списками.

import java.util.Arrays;
// Импорт утилит для работы с массивами.

import java.util.List;
// Импорт интерфейса List.

import java.util.concurrent.ThreadLocalRandom;
// Импорт генератора случайных чисел для многопоточной среды.

import java.util.stream.Collectors;
// Импорт утилиты для преобразования потоков в коллекции.
/**
 * Класс {@code Island} представляет остров, состоящий из клеток ({@link Cell}).
 * Он управляет расположением животных и растений, а также их жизненными процессами.
 *
 * <p>Основные возможности:
 * <ul>
 *     <li>Создание острова с заданными размерами.</li>
 *     <li>Получение клетки по координатам или случайной клетки.</li>
 *     <li>Получение всех животных и растений на острове.</li>
 *     <li>Запуск жизненных процессов животных и растений.</li>
 *     <li>Определение соседних клеток для заданной клетки.</li>
 * </ul>
 *
 * Пример использования:
 * <pre>{@code
 * Island island = new Island(10, 10);
 * Cell randomCell = island.getRandomCell();
 * List<Animal> animals = island.getAllAnimals();
 * List<Plant> plants = island.getAllPlants();
 * }</pre>
 */
public class Island {
    // Класс Island представляет остров, состоящий из клеток (Cell).

    private final Cell[][] location;
    // Двумерный массив клеток, описывающий карту острова.
    /**
     * Конструктор для создания острова.
     *
     * @param sizeX ширина острова (количество клеток по оси X)
     * @param sizeY высота острова (количество клеток по оси Y)
     */
    public Island(int sizeX, int sizeY) {
        // Конструктор острова, создаёт сетку клеток заданного размера.
        location = new Cell[sizeX][sizeY];
        // Инициализация массива клеток.
        for (int x = 0; x < sizeX; x++) {
            // Цикл по координате X.
            for (int y = 0; y < sizeY; y++) {
                // Цикл по координате Y.
                location[x][y] = new Cell(x, y);
                // Создаём новую клетку с координатами (x, y).
            }
        }
    }
    /**
     * Возвращает всю карту острова.
     *
     * @return двумерный массив клеток
     */
    public Cell[][] getLocation() {
        // Возвращает всю карту острова (двумерный массив клеток).
        return location;
    }
    /**
     * Возвращает клетку по координатам.
     *
     * @param x координата X
     * @param y координата Y
     * @return клетка по указанным координатам или {@code null}, если координаты некорректны
     */
    public Cell getCell(int x, int y) {
        // Возвращает клетку по координатам (x, y), если они допустимы.
        if (x >= 0 && x < Configuration.SIZE_ISLAND_FOR_X &&
                y >= 0 && y < Configuration.SIZE_ISLAND_FOR_Y) {
            return location[x][y];
            // Если координаты в пределах острова — возвращаем клетку.
        }
        return null;
        // Если координаты некорректные — возвращаем null.
    }
    /**
     * Возвращает список всех животных на острове.
     *
     * @return список животных
     */
    public List<Animal> getAllAnimals() {
        // Возвращает список всех животных на острове.
        return Arrays.stream(location)
                // Преобразуем массив клеток в поток.
                .flatMap(Arrays::stream)
                // Разворачиваем двумерный массив в одномерный поток клеток.
                .flatMap(cell -> cell.getAnimals().values().stream().flatMap(List::stream))
                // Из каждой клетки берём карту животных, превращаем её в поток списков, затем объединяем.
                .collect(Collectors.toList());
        // Собираем всех животных в один список.
    }
    /**
     * Возвращает список всех растений на острове.
     *
     * @return список растений
     */
    public List<Plant> getAllPlants() {
        // Возвращает список всех растений на острове.
        return Arrays.stream(location)
                .flatMap(Arrays::stream)
                // Разворачиваем массив клеток.
                .flatMap(cell -> cell.getPlants().values().stream().flatMap(List::stream))
                // Из каждой клетки берём карту растений, объединяем все списки.
                .collect(Collectors.toList());
        // Собираем все растения в один список.
    }
    /**
     * Запускает выполнение действия для всех клеток острова.
     *
     * @param o объект, передаваемый в метод run каждой клетки
     */
    public void run(Object o) {
        // Запускает выполнение действия для всех клеток острова.
        for (int i = 0; i < location.length; i++) {
            for (int j = 0; j < location[i].length; j++) {
                location[i][j].run(o);
                // Передаём объект o в метод run клетки.
            }
        }
    }
    /**
     * Запускает жизненные процессы всех растений на острове.
     */
    public void runPlants() {
        // Запускает жизненные процессы всех растений на острове.
        for (int y = 0; y < location.length; y++) {
            for (int x = 0; x < location[y].length; x++) {
                location[y][x].runAllPlants();
                // Запускаем метод runAllPlants для каждой клетки.
            }
        }
    }
    /**
     * Запускает жизненные процессы всех животных на острове.
     */
    public void runAnimals() {
        // Запускает жизненные процессы всех животных на острове.
        for (int y = 0; y < location.length; y++) {
            for (int x = 0; x < location[y].length; x++) {
                location[y][x].runAllAnimals();
                // Запускаем метод runAllAnimals для каждой клетки.
            }
        }
    }
    /**
     * Возвращает список соседних клеток для клетки с координатами (x, y).
     *
     * @param x координата X
     * @param y координата Y
     * @return список соседних клеток
     */
    public List<Cell> getNeighbors(int x, int y) {
        // Возвращает список соседних клеток для клетки с координатами (x, y).
        List<Cell> neighbors = new ArrayList<>();
        // Список соседей.

        if (y > 0) {
            neighbors.add(location[x][y - 1]);
            // Добавляем клетку сверху.
        }
        if (y < Configuration.SIZE_ISLAND_FOR_Y - 1) {
            neighbors.add(location[x][y + 1]);
            // Добавляем клетку снизу.
        }
        if (x > 0) {
            neighbors.add(location[x - 1][y]);
            // Добавляем клетку слева.
        }
        if (x < Configuration.SIZE_ISLAND_FOR_X - 1) {
            neighbors.add(location[x + 1][y]);
            // Добавляем клетку справа.
        }

        return neighbors;
        // Возвращаем список соседних клеток.
    }
    /**
     * Возвращает случайную клетку острова.
     *
     * @return случайная клетка
     */
    public Cell getRandomCell() {
        // Возвращает случайную клетку острова.
        int x = ThreadLocalRandom.current().nextInt(Configuration.SIZE_ISLAND_FOR_X);
        // Случайное значение X.
        int y = ThreadLocalRandom.current().nextInt(Configuration.SIZE_ISLAND_FOR_Y);
        // Случайное значение Y.
        return location[x][y];
        // Возвращаем клетку по случайным координатам.
    }
}

