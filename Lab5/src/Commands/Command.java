package Commands;

/**
 * Интерфейс команды.
 *
 * Определяет структуру всех команд:
 * - имя
 * - описание
 * - выполнение
 */
public interface Command {
    String getName();            // имя команды
    String getDescription();     // описание (для help)
    void execute(String[] args); // выполнение
}