package Common.Model;

import java.io.Serializable;

/**
 * Класс Coordinates описывает координаты продукта.
 * Используется как составная часть объекта Product.
 */

public class Coordinates implements Serializable {

    private final double x;
    private final float y;

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

