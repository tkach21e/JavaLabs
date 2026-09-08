package Server.Commands;

import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;

/**
 * Обработчик команды REMOVE_BY_ID.
 * Удаляет элемент из коллекции по ID.
 */
public class RemoveById implements Command {

    private final CollectionManager collectionManager;

    public RemoveById(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по ID";
    }

    /**
     * Выполняет команду remove_by_id - удаляет элемент по ID.
     *
     * @param request запрос с ID элемента
     * @return ответ с результатом
     */
    @Override
    public Response execute(Request request) {

        try {

            Long id = request.getId();
            boolean removed = collectionManager.removeById(id);

            if (removed) {
                return new Response("Элемент успешно удален.");
            } else {
                return new Response("Элемент с таким ID не найден.");
            }

        } catch (Exception e) {
            return new Response("Ошибка при удалении: " + e.getMessage());
        }
    }
}
