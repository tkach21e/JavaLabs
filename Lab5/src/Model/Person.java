package Model;

import java.time.LocalDate;

/**
 * Класс Person описывает владельца продукта.
 *
 * Содержит персональные данные:
 * - имя
 * - дату рождения
 * - рост
 * - вес
 * - национальность
 */

public class Person{
    private String name; //Поле не может быть null, Строка не может быть пустой
    private java.time.LocalDate birthday; //Поле не может быть null
    private long height; //Значение поля должно быть больше 0
    private Long weight; //Поле может быть null, Значение поля должно быть больше 0
    private Country nationality; //Поле может быть null

    public Person(String name, LocalDate birthday, long height, Long weight, Country nationality) {
        this.name = name;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.nationality = nationality;

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя не может отсутствовать");
        }
        if (birthday == null) {
            throw new IllegalArgumentException("Дата рождения не может отсутствовать");
        }
        if (height <= 0){
            throw new IllegalArgumentException("Рост должен быть больше 0, если он указан");
        }
        if (weight != null && weight <= 0){
            throw new IllegalArgumentException("Вес должен быть больше 0");
        }
    }

    public String getName(){
        return name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public long getHeight() {
        return height;
    }

    public Long getWeight() {
        return weight;
    }

    public Country getNationality() {
        return nationality;
    }

    @Override
    public String toString() {
        return name + ", birthday=" + birthday +
                ", height=" + height + ", weight=" + weight +
                ", nationality=" + nationality + '}';
    }
}
