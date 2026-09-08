package Server.Commands;

import Common.Model.Product;
import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;
import java.util.stream.Collectors;

/**
 * Обработчик команды FILTER_GREATER_THAN_PART_NUMBER.
 * Фильтрует элементы, чей номер партии больше заданного.
 */
public class FilterGreaterThanPartNumber implements Command {

    private final CollectionManager collectionManager;

    public FilterGreaterThanPartNumber(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "filter_greater_than_part_number";
    }

    @Override
    public String getDescription() {
        return "показать элементы с номером партии больше заданного";
    }

    /**
     * Выполняет команду filter_greater_than_part_number - фильтрует элементы,
     * чей номер партии больше заданного.
     *
     * @param request запрос с номером партии
     * @return ответ с отфильтрованными элементами
     */
    @Override
    public Response execute(Request request) {

        String partNumber = request.getStringArgument();

        if (partNumber == null || partNumber.isBlank()) {
            return new Response("Номер партии не передан.");
        }

        String result = collectionManager
                        .getCollection()
                        .stream()
                        .filter(product -> product.getPartNumber() != null && 
                                product.getPartNumber().compareTo(partNumber) > 0)
                        .sorted(Product::compareTo)
                        .map(Product::toString)
                        .collect(Collectors.joining("\n"));

        if (result.isBlank()) {
            return new Response("Совпадений не найдено.");
        }

        return new Response(result);
    }
}
