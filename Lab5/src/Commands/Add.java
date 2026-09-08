package Commands;

import Managers.CollectionManager;
import Managers.CommandManager;
import Model.Product;
import Managers.InputManager;

/**
 * Команда добавления элемента в коллекцию.
 *
 * Считывает данные продукта и добавляет его в коллекцию.
 */
public class Add implements Command {

    private final CollectionManager collectionManager;
    private final CommandManager commandManager;

    public Add(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    @Override
    public String getName() {
        return "add";
    }
    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }
    @Override
    public void execute(String[] args) {
        try {
            InputManager inputManager = commandManager.getInputManager();
            Product product = inputManager.readProduct();
            collectionManager.add(product);
            System.out.println("Элемент добавлен.");
        } catch (Exception e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        }
    }
}