public class Cocktail {
    public static long comparisons;
    public static long movements;

    public static void CocktailSort(int array[]) {
        comparisons = 0;
        movements = 0;

        int start = 0;
        int end = array.length;
        boolean swapped = true;

        while (swapped) {
            swapped = false;

            for (int i = start; i < end - 1; i++) {
                comparisons++;
                if (array[i] > array[i+1]) {
                    int temp = array[i];
                    array[i] = array[i+1];
                    array[i+1] = temp;
                    movements++;
                    swapped = true;
                }
            }

            if (swapped == false) {
                break;
            }

            swapped = false;

            end--;

            for (int i = end - 1; i >= start; i--) {
                comparisons++;
                if (array[i] > array[i+1]) {
                    int temp = array[i];
                    array[i] = array[i+1];
                    array[i+1] = temp;
                    movements++;
                    swapped = true;
                }
            }

            start++;
        }
    }
}