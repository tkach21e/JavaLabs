package Commands;

import Managers.CollectionManager;
import Model.Product;


/**
 * Команда вывода всех элементов коллекции.
 */
public class Show implements Command{

    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "show";
    }
    @Override
    public String getDescription() {
        return "вывести все элементы коллекции";
    }
    @Override
    public void execute(String[] args) {
        if (collectionManager.getAll().isEmpty()) {
            System.out.println("Коллекция пуста.");
        } else {
            for (Product product : collectionManager.getAll()) {
                System.out.println(product);
            }
        }
    }
}

