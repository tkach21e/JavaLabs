package Managers;


/**
 * Генерирует уникальные идентификаторы для объектов Product.
 *
 * Гарантирует, что id:
 * - больше 0
 * - уникален
 */
public class IdGenerator {
    private static long currentId = 1;

    public static long generateId() {
        return currentId++;
    }

    public static void update(long id) {
        if (id >= currentId) {
            currentId = id + 1;
        }
    }
}
