package Commands;

import Managers.CollectionManager;


/**
 * Команда вывода информации о коллекции.
 */
public class Info implements Command {

    private final CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "info";
    }
    @Override
    public String getDescription() {
        return "вывести информацию о коллекции";
    }
    @Override
    public void execute(String[] args) {
        System.out.println("Тип коллекции: " + collectionManager.getAll().getClass().getName());
        System.out.println("Дата инициализации: " + collectionManager.getInitializationDate());
        System.out.println("Количество элементов: " + collectionManager.size());
    }
}