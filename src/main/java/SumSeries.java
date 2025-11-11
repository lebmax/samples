import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Есть два ступенчатых графика некоторых наблюдаемых величин,
 * заданных отсортированными списками координат начала ступенек (<время измерения>, <значение>).
 * Подразумевается, что величина сохраняет своё значение между измерениями. До первого измерения величина равна нулю.
 * Требуется построить новый график, который будет являться суммой двух представленных. Т.е. построить график,
 * на котором для каждой точки (измерения) из первого и второго графика есть соответствующая точка.
 * Напишите функцию, которая по двум заданным массивам измерений вернёт сумму двух представленных массивов.
 * Пример:
 * a={{1, 2}, {5, 1}}, b={{2, 4}, {3, 6}, {9, 7}} -> {{1, 2}, {2, 6}, {3, 8}, {5, 7}, {9, 8}}
 */
public class SumSeries {

    public static void main(String[] args) {
        List<Point> res = sumSeries(
                Arrays.asList(new Point(2, 4), new Point(3, 6), new Point(9, 7)),
                Arrays.asList(new Point(1, 2), new Point(5, 1))
        );
        System.out.println(res);
    }

    private static List<Point> sumSeries(List<Point> arr1, List<Point> arr2) {
        int i = 0;
        int j = 0;
        int y1 = 0;
        int y2 = 0;

        List<Point> result = new ArrayList<>();

        while (i != arr1.size() || j != arr2.size()) {
            if (i + 1 <= arr1.size() && (j == arr2.size() || arr1.get(i).time < arr2.get(j).time)) {
                y1 = arr1.get(i).value;
                result.add(new Point(arr1.get(i).time, y1 + y2));
                i++;
            } else if (i == arr1.size() || arr2.get(j).time < arr1.get(i).time) {
                y2 = arr2.get(j).value;
                result.add(new Point(arr2.get(j).time, y1 + y2));
                j++;
            } else {
                y1 = arr1.get(i).value;
                y2 = arr2.get(j).value;
                result.add(new Point(arr1.get(i).time, y1 + y2));
                i++;
                j++;
            }
        }
        return result;
    }

    private static class Point {
        int time;
        int value;

        public Point(int time, int value) {
            this.time = time;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "time=" + time +
                    ", value=" + value +
                    '}';
        }
    }
}
