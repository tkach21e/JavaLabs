package Model;

/**
 * Класс Coordinates описывает координаты продукта.
 *
 * Используется как составная часть объекта Product.
 */

public class Coordinates {
    private double x;
    private float y;

    public Coordinates(double x, float y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    @Override
    public String toString() {
        return "(x = " + x + ", y = " + y + ")";
    }
}

