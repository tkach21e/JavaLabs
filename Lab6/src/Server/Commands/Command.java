package Server.Commands;

import Common.Network.Request;
import Common.Network.Response;

/**
 * Интерфейс обработчика команды.
 * Определяет структуру всех обработчиков команд:
 * - имя команды
 * - описание команды
 * - ответ команды
 */

public interface Command {
    /**
     * Получить имя команды.
     *
     * @return имя команды
     */
    String getName();

    /**
     * Получить описание команды.
     *
     * @return описание команды
     */
    String getDescription();

    /**
     * Выполнить команду.
     *
     * @param request запрос с параметрами команды
     * @return ответ сервера
     */
    Response execute(Request request);
}
