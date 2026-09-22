public class Bubble {
    public static long comparisons;
    public static long movements;

    public static void BubbleSort(int array[]) {
        comparisons = 0;
        movements = 0;

        boolean swapped;
        int i, j, temp;
        for (i = 0; i < array.length - 1; i++) {
            swapped = false;
            for (j = 0; j < array.length - i - 1; j++) {
                comparisons++;
                if (array[j] > array[j + 1]) {
                    temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    movements++;
                    swapped = true;
                }
            }
            if (swapped == false) {
                break;
            }
        }
    }
}