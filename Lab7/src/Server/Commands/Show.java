package Server.Commands;

import Common.Model.Product;
import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;
import java.util.stream.Collectors;

/**
 * Обработчик команды SHOW.
 * Выводит все элементы в текущем порядке коллекции в памяти.
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
     * Выполняет команду show - выводит все элементы в текущем порядке.
     *
     * @param request запрос
     * @return ответ с выводом элементов
     */
    @Override
    public Response execute(Request request) {

        String result = collectionManager
                .getCollection()
                .stream()
                .map(Product::toString)
                .collect(Collectors.joining("\n"));

        return new Response(result.isEmpty() ? "Коллекция пуста." : result);
    }
}
