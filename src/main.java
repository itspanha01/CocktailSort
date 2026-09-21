import java.util.Arrays;

public class main {
    public static void main(String[] args) {
        int array[] = {6,5,4,3,2,1}; //worst case (reversed sorted array)
        Cocktail Cocktail = new Cocktail();
        Bubble Bubble = new Bubble();
        Insertion Insertion = new Insertion();
        Selection Selection = new Selection();

        Cocktail.CocktailSort(array);
        System.out.println(Arrays.toString(array));

        int array2[] = {6,5,4,3,2,1};
        Bubble.BubbleSort(array2);
        System.out.println(Arrays.toString(array2));

        int array3[] = {6,5,4,3,2,1};
        Insertion.InsertionSort(array3);
        System.out.println(Arrays.toString(array3));

        int array4[] = {6,5,4,3,2,1};
        Selection.SelectionSort(array4);
        System.out.println(Arrays.toString(array4));


    }
}
