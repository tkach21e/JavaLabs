package Commands;

import Managers.CollectionManager;
import Managers.CommandManager;
import Model.Product;
import Managers.InputManager;


/**
 * Удаляет элементы, превышающие заданный.
 */
public class RemoveGreater implements Command {

    private final CommandManager commandManager;
    private final CollectionManager collectionManager;

    public RemoveGreater(CollectionManager collectionManager,
                         CommandManager commandManager){
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }
    @Override
    public String getName(){
        return "remove_greater";
    }
    @Override
    public String getDescription(){
        return "удалить элементы, превышающие заданный";
    }
    @Override
    public void execute(String[] args){
        try {
            InputManager inputManager = commandManager.getInputManager();
            System.out.println("Введите элемент для сравнения.");
            Product newProduct = inputManager.readProduct();
            int before = collectionManager.size();
            collectionManager.getAll().removeIf(target -> target.compareTo(newProduct) > 0);
            int after = collectionManager.size();
            System.out.println("Удалено элементов: " + (before-after));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
