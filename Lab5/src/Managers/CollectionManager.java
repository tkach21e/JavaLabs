package Managers;

import Model.Product;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Stack;


/**
 * Управляет коллекцией объектов Product.
 *
 * Отвечает за:
 * - хранение элементов
 * - добавление и удаление
 * - поиск
 * - сортировку
 * - очистку коллекции
 */

public class CollectionManager {
    private Stack<Product> collection = new Stack<>();
    private LocalDateTime initializationDate = LocalDateTime.now();

    public void add(Product product) {
        collection.push(product);
    }

    public boolean removeById(long id) {
        return collection.removeIf(p -> p.getId() == id);
    }

    public Optional<Product> getById(long id) {
        return collection.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    public void clear() {
        collection.clear();
    }

    public int size() {
        return collection.size();
    }

    public Stack<Product> getAll() {
        return collection;
    }

    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }

    public Product getMax() {
        return collection.stream()
                .max(Product::compareTo)
                .orElse(null);
    }

    public void shuffle() {
        Collections.shuffle(collection);
    }
}