package Server.Commands;

import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;
import Server.Database.ProductRepository;
import java.sql.SQLException;

/**
 * Обработчик команды CLEAR.
 * Очищает коллекцию.
 */
public class Clear implements Command {

    private final CollectionManager collectionManager;
    private final ProductRepository productRepository;

    public Clear(CollectionManager collectionManager, ProductRepository productRepository) {
        this.collectionManager = collectionManager;
        this.productRepository = productRepository;
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "удалить все принадлежащие вам элементы";
    }

    /**
     * Выполняет команду clear - очищает коллекцию.
     *
     * @param request запрос
     * @return ответ с результатом
     */
    @Override
    public Response execute(Request request) throws SQLException {
        String login = request.getLogin();
        synchronized (collectionManager) {
            int removed = productRepository.deleteAllOwned(login);
            collectionManager.removeOwned(login);
            return new Response("Удалено принадлежащих вам элементов: " + removed);
        }
    }
}
