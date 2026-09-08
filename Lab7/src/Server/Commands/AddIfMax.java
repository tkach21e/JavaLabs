package Server.Commands;

import Common.Model.Product;
import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;
import Server.Database.ProductRepository;
import java.sql.SQLException;

/**
 * Обработчик команды ADD_IF_MAX.
 * Добавляет элемент, если он больше максимального.
 */
public class AddIfMax implements Command {

    private final CollectionManager collectionManager;
    private final ProductRepository productRepository;

    public AddIfMax(CollectionManager collectionManager, ProductRepository productRepository) {
        this.collectionManager = collectionManager;
        this.productRepository = productRepository;
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
    public Response execute(Request request) throws SQLException {

        Product product = request.getProduct();

        if (product == null) {
            return new Response("Продукт не передан.", false);
        }

        synchronized (collectionManager) {
            Product maxProduct = collectionManager.getMax();
            boolean shouldAdd = maxProduct == null || product.compareTo(maxProduct) > 0;
            if (!shouldAdd) return new Response("Элемент не является максимальным.", false);

            productRepository.insert(product, request.getLogin());
            collectionManager.add(product);
            return new Response("Элемент успешно добавлен.");
        }
    }
}
