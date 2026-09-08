package Server.Commands;

import Common.Model.Product;
import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;
import Server.Managers.IdGenerator;
import java.util.Date;

/**
 * Обработчик команды ADD.
 * Добавляет новый элемент в коллекцию.
 */
public class Add implements Command {

    private final CollectionManager collectionManager;

    public Add(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "add";
    }
    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }

    /**
     * Выполняет команду add - добавляет новый элемент в коллекцию.
     *
     * @param request запрос с данными продукта
     * @return ответ с результатом
     */
    @Override
    public Response execute(Request request) {

        try {

            Product product = request.getProduct();

            if (product == null) {
                return new Response("Продукт не передан.");
            }

            product.setId(IdGenerator.generateId());
            product.setCreationDate(new Date());
            collectionManager.add(product);

            return new Response("Элемент успешно добавлен.");

        } catch (Exception e) {
            return new Response("Ошибка при добавлении: " + e.getMessage());
        }
    }
}
