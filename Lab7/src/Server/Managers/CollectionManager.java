package Server.Managers;

import Common.Model.Product;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Stack;

/**
 * Управляет коллекцией объектов Product.
 * Отвечает за:
 * - хранение элементов
 * - добавление и удаление
 * - поиск
 * - перемешивание
 * - очистку коллекции
 */

public class CollectionManager {

    private final Stack<Product> collection;
    private final LocalDateTime initializationDate;

    public CollectionManager() {
        collection = new Stack<>();
        initializationDate = LocalDateTime.now();
    }

    public CollectionManager(Stack<Product> collection) {
        this.collection = collection;
        initializationDate = LocalDateTime.now();
    }

    public synchronized void add(Product product) {
        collection.push(product);
    }

    public synchronized boolean removeById(long id) {
        return collection.removeIf(p -> p.getId() == id);
    }

    public synchronized Optional<Product> getById(long id) {
        return collection.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    public synchronized int size() {
        return collection.size();
    }

    public synchronized Stack<Product> getCollection() {
        Stack<Product> copy = new Stack<>();
        copy.addAll(collection);
        return copy;
    }

    public LocalDateTime getInitializationDate() {
        return initializationDate;
    }

    public synchronized Product getMax() {
        return collection.stream()
                .max(Product::compareTo)
                .orElse(null);
    }

    public synchronized void removeOwned(String creatorLogin) {
        collection.removeIf(product -> creatorLogin.equals(product.getCreatorLogin()));
    }

    public synchronized void removeGreater(Product product, String creatorLogin) {
        collection.removeIf(target -> creatorLogin.equals(target.getCreatorLogin())
                && target.compareTo(product) > 0);
    }

    public synchronized void shuffle() {
        Collections.shuffle(collection);
    }

    public synchronized void replaceById(long id, Product replacement) {
        collection.replaceAll(product -> product.getId() == id ? replacement : product);
    }

}
