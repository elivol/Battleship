abstract class Shape {

    abstract double getPerimeter();

    abstract double getArea();
}

class Triangle extends Shape {

    private double side1;
    private double side2;
    private double side3;

    public Triangle(double side1, double side2, double side3) {
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
    }

    @java.lang.Override
    double getPerimeter() {
        return side1 + side2 + side3;
    }

    @java.lang.Override
    double getArea() {
        double p = getPerimeter() / 2;
        return Math.sqrt(p * (p - side1) * (p - side2) * (p - side3));
    }
}

class Rectangle extends Shape {

    private double side1;
    private double side2;

    public Rectangle(double side1, double side2) {
        this.side1 = side1;
        this.side2 = side2;
    }

    @java.lang.Override
    double getPerimeter() {
        return 2 * (side1 + side2);
    }

    @java.lang.Override
    double getArea() {
        return side1 * side2;
    }
}

class Circle extends Shape {
    double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @java.lang.Override
    double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @java.lang.Override
    double getArea() {
        return Math.PI * radius * radius;
    }
}