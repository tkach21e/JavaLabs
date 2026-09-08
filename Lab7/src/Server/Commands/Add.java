package Server.Commands;

import Common.Model.Product;
import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;
import Server.Database.ProductRepository;
import java.sql.SQLException;

/**
 * Обработчик команды ADD.
 * Добавляет новый элемент в коллекцию.
 */
public class Add implements Command {

    private final CollectionManager collectionManager;
    private final ProductRepository productRepository;

    public Add(CollectionManager collectionManager, ProductRepository productRepository) {
        this.collectionManager = collectionManager;
        this.productRepository = productRepository;
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
    public Response execute(Request request) throws SQLException {

        Product product = request.getProduct();

        if (product == null) {
            return new Response("Продукт не передан.", false);
        }

        String login = request.getLogin();
        synchronized (collectionManager) {
            productRepository.insert(product, login);
            collectionManager.add(product);
        }

        return new Response("Элемент успешно добавлен.");
    }
}
