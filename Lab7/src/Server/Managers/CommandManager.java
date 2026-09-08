package Server.Managers;

import Common.Network.*;
import Server.Commands.*;
import Server.Database.UserRepository;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Управляет командами и их обработчиками.
 * Отвечает за:
 * - регистрацию обработчиков команд
 * - маршрутизацию команд к соответствующим обработчикам
 * - выполнение команд
 */
public class CommandManager {

    private static final Logger logger = LoggerFactory.getLogger(CommandManager.class);
    private final Map<String, Command> commands = new HashMap<>();
    private final UserRepository users;

    public CommandManager(UserRepository users) {
        this.users = users;
    }

    /**
     * Выполняет команду на основе запроса.
     *
     * @param request запрос с типом и параметрами команды
     * @return ответ с результатом выполнения
     */
    public Response execute(Request request) {
        try {
            logger.debug("Обработка запроса, команда: {}", request.getCommandName());
            String login = request.getLogin();
            String password = request.getPassword();
            if (login == null || login.isBlank() || login.length() > 100
                    || password == null || password.isBlank()) {
                return new Response("Нужны логин (1–100 символов) и непустой пароль.", false);
            }

            String name = request.getCommandName();
            if ("register".equals(name)) {
                return users.register(login, password)
                        ? new Response("Регистрация успешно завершена.")
                        : new Response("Этот логин уже занят.", false);
            }
            if (!users.authenticate(login, password)) {
                return new Response("Неверный логин или пароль.", false);
            }
            if ("login".equals(name)) return new Response("Авторизация успешна.");

            Command command = commands.get(name);
            return command == null
                    ? new Response("На сервере нет этой команды.", false)
                    : command.execute(request);
        } catch (SQLException exception) {
            logger.error("Ошибка PostgreSQL", exception);
            return new Response("Ошибка базы данных. Попробуйте позже.", false);
        } catch (RuntimeException exception) {
            logger.error("Ошибка обработки запроса", exception);
            return new Response("Некорректный запрос.", false);
        }
    }

    /**
     * Регистрирует команду
     */
    public void register(Command command) {
        commands.put(command.getName(), command);
    }

    /**
     * Возвращает все команды
     */
    public Map<String, Command> getCommands() {
        return commands;
    }
}
