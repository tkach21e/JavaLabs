package Server.Commands;

import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;
import Server.Database.ProductRepository;
import java.sql.SQLException;
import Common.Model.Product;

/**
 * Обработчик команды REMOVE_BY_ID.
 * Удаляет элемент из коллекции по ID.
 */
public class RemoveById implements Command {

    private final CollectionManager collectionManager;
    private final ProductRepository productRepository;

    public RemoveById(CollectionManager collectionManager, ProductRepository productRepository) {
        this.collectionManager = collectionManager;
        this.productRepository = productRepository;
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String getDescription() {
        return "удалить принадлежащий вам элемент по ID";
    }

    /**
     * Выполняет команду remove_by_id - удаляет элемент по ID.
     *
     * @param request запрос с ID элемента
     * @return ответ с результатом
     */
    @Override
    public Response execute(Request request) throws SQLException {

        Long id = request.getId();
        String login = request.getLogin();
        synchronized (collectionManager) {
            Product product = collectionManager.getById(id).orElse(null);
            if (product == null) {
                return new Response("Элемент с таким ID не найден.", false);
            }
            if (!login.equals(product.getCreatorLogin())) {
                return new Response("Нельзя удалить объект другого пользователя.", false);
            }
            if (!productRepository.deleteById(id, login)) {
                return new Response("Объект не найден в базе данных.", false);
            }
            collectionManager.removeById(id);
            return new Response("Элемент успешно удален.");
        }
    }
}
