package Server.Commands;

import Common.Model.Product;
import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;
import Server.Database.ProductRepository;
import java.sql.SQLException;

/**
 * Обработчик команды REMOVE_GREATER.
 * Удаляет все элементы, превышающие заданный.
 */
public class RemoveGreater implements Command {

    private final CollectionManager collectionManager;
    private final ProductRepository productRepository;

    public RemoveGreater(CollectionManager collectionManager, ProductRepository productRepository) {
        this.collectionManager = collectionManager;
        this.productRepository = productRepository;
    }

    @Override
    public String getName() {
        return "remove_greater";
    }

    @Override
    public String getDescription() {
        return "удалить принадлежащие вам элементы, превышающие заданный";
    }

    /**
     * Выполняет команду remove_greater - удаляет все элементы,
     * чье значение больше заданного.
     *
     * @param request запрос с данными продукта для сравнения
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
            int removed = productRepository.deleteGreaterOwned(product.getPrice(), login);
            collectionManager.removeGreater(product, login);
            return new Response("Удалено принадлежащих вам элементов: " + removed);
        }
    }
}
