package Server.Commands;

import Common.Model.Product;
import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;

/**
 * Обработчик команды REMOVE_GREATER.
 * Удаляет все элементы, превышающие заданный.
 */
public class RemoveGreater implements Command {

    private final CollectionManager collectionManager;

    public RemoveGreater(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "remove_greater";
    }

    @Override
    public String getDescription() {
        return "удалить все элементы, превышающие заданный";
    }

    /**
     * Выполняет команду remove_greater - удаляет все элементы,
     * чье значение больше заданного.
     *
     * @param request запрос с данными продукта для сравнения
     * @return ответ с результатом
     */
    @Override
    public Response execute(Request request) {

        try {

            Product product = request.getProduct();

            if (product == null) {
                return new Response("Продукт не передан.");
            }

            int before = collectionManager.size();

            collectionManager.getCollection().removeIf(target -> target.compareTo(product) > 0);

            int after = collectionManager.size();

            return new Response("Удалено элементов: " + (before - after));

        } catch (Exception e) {
            return new Response("Ошибка при удалении: " + e.getMessage());
        }
    }
}
