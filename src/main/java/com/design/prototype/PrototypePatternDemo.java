package com.design.prototype;

public class PrototypePatternDemo {

    public static void main(String[] args) {
        ShapeCache.loadCache();

        Shape clonedShape = (Shape) ShapeCache.getShape("1");
        System.out.println("Shape : " + clonedShape.getType());

        Shape clonedShape2 = (Shape) ShapeCache.getShape("2");
        System.out.println("Shape : " + clonedShape2.getType());

        Shape clonedShape3 = (Shape) ShapeCache.getShape("3");
        System.out.println("Shape : " + clonedShape3.getType());

        Shape clonedShape4 = (Shape) ShapeCache.getShape("3");
        System.out.println("Shape : " + clonedShape3.getType());


        System.out.println(clonedShape);
        System.out.println(clonedShape2);
        System.out.println(clonedShape3);
        System.out.println(clonedShape4);

    }


}
