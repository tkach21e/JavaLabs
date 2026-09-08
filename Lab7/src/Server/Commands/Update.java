package Server.Commands;

import Common.Model.Product;
import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;
import Server.Database.ProductRepository;
import java.sql.SQLException;

/**
 * Обработчик команды UPDATE.
 * Обновляет элемент в коллекции по ID.
 */
public class Update implements Command {

    private final CollectionManager collectionManager;
    private final ProductRepository productRepository;

    public Update(CollectionManager collectionManager, ProductRepository productRepository) {
        this.collectionManager = collectionManager;
        this.productRepository = productRepository;
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "обновить принадлежащий вам элемент по ID";
    }

    /**
     * Выполняет команду update - обновляет элемент по ID.
     *
     * @param request запрос с ID и данными нового продукта
     * @return ответ с результатом
     */
    @Override
    public Response execute(Request request) throws SQLException {

        Long id = request.getId();
        Product newProduct = request.getProduct();

        if (newProduct == null) {
            return new Response("Продукт не передан.", false);
        }

        String login = request.getLogin();
        synchronized (collectionManager) {
            Product existingProduct = collectionManager.getById(id).orElse(null);
            if (existingProduct == null) {
                return new Response("Элемент с таким ID не найден.", false);
            }
            if (!login.equals(existingProduct.getCreatorLogin())) {
                return new Response("Нельзя изменить объект другого пользователя.", false);
            }

            newProduct.setId(id);
            newProduct.setCreationDate(existingProduct.getCreationDate());
            newProduct.setCreatorLogin(login);
            if (!productRepository.update(id, newProduct, login)) {
                return new Response("Объект не найден в базе данных.", false);
            }
            collectionManager.replaceById(id, newProduct);
            return new Response("Элемент успешно обновлен.");
        }
    }
}
