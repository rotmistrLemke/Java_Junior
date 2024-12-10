import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

public class EvenNumberAverageCalculator {

    public static void main(String[] args) {
        // Создаем список чисел
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Используем Stream API для вычисления среднего значения четных чисел
        OptionalDouble average = numbers.stream()
                .filter(n -> n % 2 == 0)
                .mapToDouble(Integer::doubleValue)
                .average();

        // Выводим результат
        if (average.isPresent()) {
            System.out.printf("Среднее значение четных чисел: %.2f%n", average.getAsDouble());
        } else {
            System.out.println("В списке нет четных чисел.");
        }
    }
}
