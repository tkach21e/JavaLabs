package Commands;

import Managers.CollectionManager;
import Managers.CommandManager;
import Model.Product;
import Managers.InputManager;

/**
 * Команда обновления элемента по id.
 *
 * Заменяет данные существующего элемента новыми.
 */
public class Update implements Command {

    private final CollectionManager collectionManager;
    private final CommandManager commandManager;

    public Update(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    @Override
    public String getName() {
        return "update";
    }
    @Override
    public String getDescription() {
        return "обновить элемент по id";
    }
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Ошибка: укажите id.");
            return;
        }

        try {
            long id = Long.parseLong(args[1]);

            if (collectionManager.getById(id).isEmpty()) {
                System.out.println("Элемент с таким id не найден.");
                return;
            }

            InputManager inputManager = commandManager.getInputManager();
            System.out.println("Введите новые данные элемента.");
            Product newProduct = inputManager.readProduct();
            Product updated = new Product(
                    id,
                    newProduct.getName(),
                    newProduct.getCoordinates(),
                    newProduct.getPrice(),
                    newProduct.getPartNumber(),
                    newProduct.getUnitOfMeasure(),
                    newProduct.getOwner());

            collectionManager.removeById(id);
            collectionManager.add(updated);
            System.out.println("Элемент обновлён.");

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: id должен быть числом.");
        } catch (Exception e) {
            System.out.println("Ошибка обновления: " + e.getMessage());
        }
    }
}