import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Benchmarks Bubble Sort, Cocktail Sort, Insertion Sort, and Selection Sort
 * (each in its own class: Bubble, Cocktail, Insertion, Selection) across
 * three dataset sizes (5, 1,000, 1,000,000). For each run it records
 * runtime, comparisons, and swaps/movements, writes the sorted output to
 * its own .txt file, and writes a combined performance table to
 * performance_results.txt.
 *
 * Requires Bubble.java, Cocktail.java, Insertion.java, and Selection.java
 * to be compiled in the same folder -- each of those classes exposes
 * public static long comparisons / movements fields that this benchmark
 * reads immediately after calling the sort.
 */
public class SortingBenchmark {

    static int[] generateRandomArray(int size, long seed) {
        Random rand = new Random(seed); // fixed seed: every algorithm sorts the identical input
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(size * 10 + 1);
        }
        return arr;
    }

    static void writeArrayToFile(int[] arr, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                sb.append(arr[i]);
                if (i < arr.length - 1) sb.append(", ");
                if (sb.length() > 1_000_000) { // flush periodically for the 1,000,000-element case
                    writer.write(sb.toString());
                    sb.setLength(0);
                }
            }
            writer.write(sb.toString());
        }
    }

    public static void main(String[] args) throws IOException {
        int[] sizes = {5, 1_000, 1_000_000};
        String[] algorithmNames = {"Bubble Sort", "Cocktail Sort", "Insertion Sort", "Selection Sort"};

        String separator = "--------------------------------------------------------------------------------";
        StringBuilder report = new StringBuilder();
        report.append(String.format("%-15s %-15s %-15s %-15s %-15s%n",
                "Dataset Size", "Algorithm", "Time (ms)", "Comparisons", "Swaps/Moves"));
        report.append(separator).append(System.lineSeparator());

        for (int size : sizes) {
            int[] originalArray = generateRandomArray(size, 42L);
            System.out.println("Running dataset size: " + size);

            for (String algoName : algorithmNames) {
                int[] arr = originalArray.clone(); // each algorithm gets its own copy of the same input

                long comparisons;
                long movements;
                long startTime = System.nanoTime();

                if (algoName.equals("Bubble Sort")) {
                    Bubble.BubbleSort(arr);
                    comparisons = Bubble.comparisons;
                    movements = Bubble.movements;
                } else if (algoName.equals("Cocktail Sort")) {
                    Cocktail.CocktailSort(arr);
                    comparisons = Cocktail.comparisons;
                    movements = Cocktail.movements;
                } else if (algoName.equals("Insertion Sort")) {
                    Insertion.InsertionSort(arr);
                    comparisons = Insertion.comparisons;
                    movements = Insertion.movements;
                } else {
                    Selection.SelectionSort(arr);
                    comparisons = Selection.comparisons;
                    movements = Selection.movements;
                }

                long elapsedNanos = System.nanoTime() - startTime;
                double elapsedMs = elapsedNanos / 1_000_000.0;

                report.append(String.format("%-15d %-15s %-15.3f %-15d %-15d%n",
                        size, algoName, elapsedMs, comparisons, movements));

                String outFile = "sorted_" + algoName.replace(" ", "_").toLowerCase() + "_n" + size + ".txt";
                writeArrayToFile(arr, outFile);

                System.out.printf("  %-15s done in %.3f ms (%d comparisons, %d moves) -> %s%n",
                        algoName, elapsedMs, comparisons, movements, outFile);
            }
        }

        System.out.println();
        System.out.println(report);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("performance_results.txt"))) {
            writer.write(report.toString());
        }
        System.out.println("Performance summary written to performance_results.txt");
    }
}