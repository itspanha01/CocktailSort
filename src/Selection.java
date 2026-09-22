public class Selection {
    public static long comparisons;
    public static long movements;

    public static void SelectionSort(int[] arr) {
        comparisons = 0;
        movements = 0;

        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < arr.length; j++) {
                comparisons++;
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            // One swap per outer-loop pass, even if minIndex == i already
            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
                movements++;
            }
        }
    }
}