package Managers;

import Model.*;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Отвечает за ввод данных от пользователя или из файла.
 *
 * Обеспечивает:
 * - чтение примитивных типов
 * - чтение сложных объектов (Product)
 * - обработку ошибок ввода
 */
public class InputManager {

    private final Scanner scanner;

    public InputManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public Product readProduct() {

        System.out.print("Введите наименование продукта: ");
        String name = readNonEmptyString();

        System.out.print("Введите координату x (double): ");
        double x = readDouble();

        System.out.print("Введите координату y (float): ");
        float y = readFloat();

        Coordinates coordinates = new Coordinates(x, y);

        System.out.print("Введите цену (больше 0): ");
        Long price = readLongObj(false);

        System.out.print("Введите номер партии (либо ничего): ");
        String pn = scanner.nextLine();
        String partNumber = pn.isEmpty() ? null : pn;

        printEnum(UnitOfMeasure.values());
        System.out.print("Введите единицу измерения из списка (либо ничего): ");
        UnitOfMeasure uom = readUnitOfMeasure();

        Person owner = readPerson();

        return new Product(name, coordinates, price, partNumber, uom, owner);
    }

    private Person readPerson() {
        System.out.print("Введите имя владельца: ");
        String name = readNonEmptyString();

        System.out.print("Введите дату рождения (в формате yyyy-mm-dd): ");
        LocalDate birthday = readDate();

        System.out.print("Введите рост (больше 0): ");
        long height = readLong();

        System.out.print("Введите вес (больше 0) (либо ничего): ");
        Long weight = readLongObj(true);

        printEnum(Country.values());
        System.out.print("Введите страну из списка (либо ничего): ");
        Country nationality = readCountry();

        return new Person(name, birthday, height, weight, nationality);
    }

    private UnitOfMeasure readUnitOfMeasure(){
        while (true){
            try {
                String str = scanner.nextLine();
                if (str.isEmpty()){
                    return null;
                }
                return UnitOfMeasure.valueOf(str);
            }catch (Exception e){
                System.out.print("Ошибка: введите единицы измерения из списка (либо ничего): ");
            }
        }
    }

    private Country readCountry(){
        while (true){
            try {
                String str = scanner.nextLine();
                if (str.isEmpty()){
                    return null;
                }
                return Country.valueOf(str);
            }catch (Exception e){
                System.out.print("Ошибка: введите страну из списка (либо ничего): ");
            }
        }
    }

    private LocalDate readDate() {
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine());
            } catch (Exception e) {
                System.out.print("Ошибка: введите дату рождения (в формате yyyy-mm-dd): ");
            }
        }
    }

    private double readDouble() {
        while (true) {

            String str = scanner.nextLine();
            if (str.contains(",")){
                str = str.replace(",",".");
            }

            try {

                return Double.parseDouble(str);

            } catch (Exception e) {
                System.out.print("Ошибка: введите ЧИСЛО (double): ");
            }
        }
    }
    private float readFloat() {
        while (true) {

            String str = scanner.nextLine();
            if (str.contains(",")){
                str = str.replace(",",".");
            }

            try {

                return Float.parseFloat(str);

            } catch (Exception e) {
                System.out.print("Ошибка: введите ЧИСЛО (float): ");
            }
        }
    }
    private long readLong() {
        while (true) {

            String str = scanner.nextLine();
            if (str.contains(",")){
                str = str.replace(",",".");
            }

            try {

                long value = Long.parseLong(str);
                if (value > 0){
                    return value;
                }
                System.out.print("Ошибка: число должно быть больше 0: ");

            } catch (Exception e) {
                System.out.print("Ошибка: введите ЧИСЛО (long): ");
            }
        }
    }
    private Long readLongObj(boolean canbenull) {
        while (true) {

            String str = scanner.nextLine();
            if (str.contains(",")){
                str = str.replace(",",".");
            }

            if (str.isEmpty() && canbenull){
                return null;
            } else {
                try {

                    long value = Long.parseLong(str);
                    if (value > 0) {
                        return value;
                    }
                    System.out.print("Ошибка: число должно быть больше 0: ");

                } catch (Exception e) {
                    System.out.print("Ошибка: введите ЧИСЛО (Long): ");
                }
            }
        }
    }
    private String readNonEmptyString() {
        while (true) {
            String input = scanner.nextLine();
            if (!input.trim().isEmpty()) { return input; }
            System.out.print("Строка не может быть пустой: ");
        }
    }

    private void printEnum(Object[] values) {
        for (Object v : values) {
            System.out.println(v);
        }
    }
}