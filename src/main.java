import java.util.Arrays;

public class main {
    public static void main(String[] args) {
        int array[] = {6,5,4,3,2,1}; //worst case (reversed sorted array)
        Cocktail Cocktail = new Cocktail();
        Bubble Bubble = new Bubble();

        Cocktail.CocktailSort(array);
        System.out.println(Arrays.toString(array));

        Bubble.BubbleSort(array);
        System.out.println(Arrays.toString(array));
    }
}
