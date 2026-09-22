public class Insertion {
    public static long comparisons;
    public static long movements;

    public static void InsertionSort(int[] arr) {
        comparisons = 0;
        movements = 0;

        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0) {
                comparisons++;
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    movements++;
                    j--;
                } else {
                    break;
                }
            }

            // j+1 is where key finally belongs
            arr[j + 1] = key;
        }
    }
}