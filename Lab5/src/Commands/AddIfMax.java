package Commands;

import Managers.CollectionManager;
import Managers.CommandManager;
import Model.Product;
import Managers.InputManager;


/**
 * Добавляет элемент, если он больше максимального в коллекции.
 */
public class AddIfMax implements Command {

    private final CollectionManager collectionManager;
    private final CommandManager commandManager;

    public AddIfMax(CollectionManager collectionManager,
                    CommandManager commandManager){
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    @Override
    public String getName(){
        return "add_if_max";
    }
    @Override
    public String getDescription(){
        return "добавить элемент, если он больше максимального";
    }
    @Override
    public void execute(String[] args) {
        try {
            InputManager inputManager = commandManager.getInputManager();
            Product newProduct = inputManager.readProduct();
            Product maxProduct = collectionManager.getMax();

            if (maxProduct == null) {
                collectionManager.add(newProduct);
                System.out.println("Коллекция была пустой. Элемент добавлен.");
                return;
            }

            if (newProduct.compareTo(maxProduct) > 0) {
                collectionManager.add(newProduct);
                System.out.println("Элемент добавлен (он больше максимального).");
            } else {
                System.out.println("Элемент не добавлен (он не больше максимального).");
            }

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
