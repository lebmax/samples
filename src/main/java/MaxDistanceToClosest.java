/**
 * Места в кинотеатре расположены в один ряд. Только что пришедший зритель выбирает место,
 * чтобы сидеть максимально далеко от остальных зрителей в ряду.
 * То есть расстояние от того места, куда сядет зритель до ближайшего к нему зрителя
 * должно быть максимально.
 * <p>
 * Гарантируется, что в ряду всегда есть свободные места и уже сидит хотя бы один зритель.
 * Напишите функцию, которая по заданному ряду мест (массиву из нулей и единиц)
 * вернёт расстояние (ЧИСЛО ПРОМЕЖУТКОВ МЕЖДУ КРЕСЛАМИ) от выбранного места до ближайшего зрителя.
 * Пример:
 * maxDistToClosest([1, 0, 0, 0, 1]) -> 2
 * maxDistToClosest([1, 0, 1, 0, 0, 1, 0, 0, 0, 1]) -> 2
 * maxDistToClosest([1, 0, 1, 0]) -> 1
 * maxDistToClosest([1, 0, 0, 0, 1, 0, 1]) -> 2
 * maxDistToClosest([1, 0, 0, 0]) -> 3
 */
public class MaxDistanceToClosest {

    public static void main(String[] args) {
        int res = maxDistToClosest(new int[]{1, 0, 0, 0, 1});
        System.out.println(res);

        res = maxDistToClosest(new int[]{1, 0, 1, 0, 0, 1, 0, 0, 0, 1});
        System.out.println(res);

        res = maxDistToClosest(new int[]{1, 0, 1, 0});
        System.out.println(res);

        res = maxDistToClosest(new int[]{1, 0, 0, 0, 1, 0, 1});
        System.out.println(res);

        res = maxDistToClosest(new int[]{1, 0, 0, 0});
        System.out.println(res);
    }

    private static int maxDistToClosest(int[] seats) {
        int res = 0, n = seats.length, last = -1;
        for (int i = 0; i < n; i++) {
            if (seats[i] == 1) {
                res = last < 0 ? i : Math.max(res, (i - last) / 2);
                last = i;
            }
        }
        return Math.max(res, n - last - 1);
    }
}
