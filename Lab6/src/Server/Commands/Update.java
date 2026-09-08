package Server.Commands;

import Common.Model.Product;
import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;

/**
 * Обработчик команды UPDATE.
 * Обновляет элемент в коллекции по ID.
 */
public class Update implements Command {

    private final CollectionManager collectionManager;

    public Update(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "обновить элемент коллекции по ID";
    }

    /**
     * Выполняет команду update - обновляет элемент по ID.
     *
     * @param request запрос с ID и данными нового продукта
     * @return ответ с результатом
     */
    @Override
    public Response execute(Request request) {

        try {

            Long id = request.getId();
            Product newProduct = request.getProduct();

            if (newProduct == null) {
                return new Response("Продукт не передан.");
            }

            Product existingProduct = collectionManager.getById(id).orElse(null);

            if (existingProduct == null) {
                return new Response("Элемент с таким ID не найден.");
            }

            newProduct.setId(id);
            newProduct.setCreationDate(existingProduct.getCreationDate());

            collectionManager.removeById(id);
            collectionManager.add(newProduct);

            return new Response("Элемент успешно обновлен.");

        } catch (Exception e) {
            return new Response("Ошибка при обновлении: " + e.getMessage());
        }
    }
}
