/**
 * Условие:
 * Дана строка, состоящая из букв 'X', 'Y' и 'O'.
 * Необходимо найти кратчайшее расстояние между буквами 'X' и 'Y',
 * либо вывести 0, если 'X' либо 'Y' отсутствуют.
 * <p>
 * Примечания:
 * -- дистанция между двумя рядом стоящими буквами считается как 1 (одно межбуквенное расстояние)
 * -- дистанция может считаться в обе стороны (взаимное расположение ближайших друг к другу X и Y не имеет значения)
 * <p>
 * Примеры:
 * "YY" -> 0
 * "XX" -> 0
 * "XY" -> 1
 * "YOX" -> 2
 * "OOOXOOYOXO" -> 2
 * "OOOXXOY"-> 2
 * "OOOXXOX"-> 0
*/
public class MinDistanceArrElement {

    public static void main(String[] args) {
        int res = distance("YY");
        System.out.println(res);

        res = distance("XX");
        System.out.println(res);

        res = distance("XY");
        System.out.println(res);

        res = distance("YOX");
        System.out.println(res);

        res = distance("OOOXOOYOXO");
        System.out.println(res);

        res = distance("OOOXXOY");
        System.out.println(res);

        res = distance("OOOXXOX");
        System.out.println(res);
    }

    private static int distance(String input) {
        int ind = -1;
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == 'X' || input.charAt(i) == 'Y') {
                if (ind == -1) {
                    ind = i;
                } else if (input.charAt(ind) == input.charAt(i)) {
                    ind = i;
                } else if (input.charAt(ind) != input.charAt(i)) {
                    min = Math.min(min, i - ind);
                    ind = i;
                }
            }
        }

        if (min != Integer.MAX_VALUE) {
            return min;
        } else {
            return 0;
        }
    }
}
