package Server.Commands;

import Common.Model.Product;
import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;
import Server.Managers.IdGenerator;
import java.util.Date;

/**
 * Обработчик команды ADD_IF_MAX.
 * Добавляет элемент, если он больше максимального.
 */
public class AddIfMax implements Command {

    private final CollectionManager collectionManager;

    public AddIfMax(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "add_if_max";
    }

    @Override
    public String getDescription() {
        return "добавить элемент, если он больше максимального";
    }

    /**
     * Выполняет команду add_if_max - добавляет элемент,
     * если его значение больше максимального в коллекции.
     *
     * @param request запрос с данными продукта
     * @return ответ с результатом
     */
    @Override
    public Response execute(Request request) {

        Product product = request.getProduct();

        if (product == null) {
            return new Response("Продукт не передан.");
        }

        Product maxProduct = collectionManager.getMax();

        boolean shouldAdd = maxProduct == null || product.compareTo(maxProduct) > 0;

        if (!shouldAdd) {
            return new Response("Элемент не является максимальным.");
        }

        product.setId(IdGenerator.generateId());
        product.setCreationDate(new Date());
        collectionManager.add(product);

        return new Response("Элемент успешно добавлен.");
    }
}
