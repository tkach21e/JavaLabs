package Commands;

import Managers.CollectionManager;

/**
 * Команда удаления элемента по id.
 */
public class RemoveById implements Command {

    private final CollectionManager collectionManager;

    public RemoveById(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }
    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по id";
    }
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: нужно указать id.");
            return;
        }

        try {
            long id = Long.parseLong(args[1]);

            boolean removed = collectionManager.removeById(id);
            if (removed) {
                System.out.println("Элемент удалён.");
            } else {
                System.out.println("Элемент с таким id не найден.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: id должен быть числом.");
        }
    }
}