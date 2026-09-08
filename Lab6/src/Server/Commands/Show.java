package Server.Commands;

import Common.Model.Product;
import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;
import java.util.stream.Collectors;

/**
 * Обработчик команды SHOW.
 * Выводит все элементы коллекции в отсортированном виде.
 */
public class Show implements Command {

    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {
        return "вывести все элементы коллекции";
    }

    /**
     * Выполняет команду show - выводит все элементы коллекции,
     * отсортированные по умолчанию (по цене).
     *
     * @param request запрос
     * @return ответ с выводом элементов
     */
    @Override
    public Response execute(Request request) {

        if (collectionManager.getCollection().isEmpty()) {
            return new Response("Коллекция пуста.");
        }

        String result = collectionManager
                .getCollection()
                .stream()
                .sorted(Product::compareTo)
                .map(Product::toString)
                .collect(Collectors.joining("\n"));

        return new Response(result);
    }
}
