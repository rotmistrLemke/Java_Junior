package Sem02;

import java.lang.reflect.Method;

public class StringMethodsPrinter {

    public static void main(String[] args) {
        printStringMethods();
    }

    public static void printStringMethods() {
        Class<?> stringClass = String.class;

        // Получаем все методы класса String
        Method[] methods = stringClass.getDeclaredMethods();

        System.out.println("Все методы класса String:");
        for (Method method : methods) {
            System.out.println(method.toString());
        }

        // Разделитель для лучшей читаемости
        System.out.println("\n\n------------------------\n");

        // Выводим только публичные методы
        System.out.println("Публичные методы класса String:");
        Method[] publicMethods = stringClass.getMethods();
        for (Method method : publicMethods) {
            System.out.println(method.toString());
        }
    }
}
