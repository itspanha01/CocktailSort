import java.util.Arrays;

public class Cocktail {
    public static void CocktailSort(int array[]) {
        int start = 0;
        int end = array.length;
        boolean swapped = true;

        while (swapped) {
            swapped = false;

            for (int i = start; i < end - 1; i++) {
                if (array[i] > array[i+1]) {
                    int temp = array[i];
                    array[i] = array[i+1];
                    array[i+1] = temp;
                    swapped = true;
                }
            }

            if (swapped == false) {
                break;
            }

            swapped = false;

            end--;

            for (int i = end - 1; i >= start; i--) {
                if (array[i] > array[i+1]) {
                    int temp = array[i];
                    array[i] = array[i+1];
                    array[i+1] = temp;
                    swapped = true;
                }
            }

            start++;
        }
    }
}
