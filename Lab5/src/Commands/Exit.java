package Commands;

/**
 * Завершает работу программы без сохранения.
 */
public class Exit implements Command {

    private boolean shouldExit = false;

    @Override
    public String getName() {
        return "exit";
    }
    @Override
    public String getDescription() {
        return "завершить программу (без сохранения)";
    }
    @Override
    public void execute(String[] args) {
        shouldExit = true;
        System.out.println("Завершение программы...");
    }

    public boolean shouldExit() {
        return shouldExit;
    }
}