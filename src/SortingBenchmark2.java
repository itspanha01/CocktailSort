import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Benchmarks Bubble Sort, Cocktail Sort, Insertion Sort, and Selection Sort
 * (each in its own class: Bubble, Cocktail, Insertion, Selection) across
 * three dataset sizes (5, 1,000, 1,000,000) AND three input types:
 *   - Best Case:    already sorted ascending
 *   - Worst Case:   reverse sorted (strictly descending)
 *   - Average Case: random order (fixed seed, so every algorithm sees the
 *                    same random input at a given size)
 *
 * For every (size, case, algorithm) combination it records runtime,
 * comparisons, and swaps/movements, writes the sorted output to its own
 * .txt file, and writes one combined performance table to
 * performance_results.txt.
 *
 * WARNING: the 1,000,000-element Worst Case and Average Case runs are
 * genuinely slow for the O(n^2) algorithms -- expect this to take a long
 * time (potentially 30-90+ minutes total) to finish. Best Case at
 * n=1,000,000 finishes almost instantly for Bubble/Cocktail/Insertion
 * (they detect the sorted input and exit early), but Selection Sort has
 * no early-exit and will take just as long in Best Case as in the others.
 *
 * Requires Bubble.java, Cocktail.java, Insertion.java, and Selection.java
 * to be compiled in the same folder -- each of those classes exposes
 * public static long comparisons / movements fields that this benchmark
 * reads immediately after calling the sort.
 */
public class SortingBenchmark2 {

    static int[] generateRandomArray(int size, long seed) {
        Random rand = new Random(seed); // fixed seed: every algorithm sorts the identical input
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(size * 10 + 1);
        }
        return arr;
    }

    static int[] generateSortedArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i; // ascending, 0..size-1 -- best case input
        }
        return arr;
    }

    static int[] generateReverseSortedArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = size - i; // descending, size..1 -- worst case input
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
        String[] caseNames = {"Best Case", "Worst Case", "Average Case"};

        String separator = "--------------------------------------------------------------------------------------------";
        StringBuilder report = new StringBuilder();
        report.append(String.format("%-10s %-13s %-15s %-12s %-14s %-14s%n",
                "Size", "Case", "Algorithm", "Time (ms)", "Comparisons", "Swaps/Moves"));
        report.append(separator).append(System.lineSeparator());

        for (int size : sizes) {
            for (String caseName : caseNames) {
                int[] originalArray;
                if (caseName.equals("Best Case")) {
                    originalArray = generateSortedArray(size);
                } else if (caseName.equals("Worst Case")) {
                    originalArray = generateReverseSortedArray(size);
                } else {
                    originalArray = generateRandomArray(size, 42L);
                }

                System.out.println("Running size=" + size + ", case=" + caseName);

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

                    report.append(String.format("%-10d %-13s %-15s %-12.3f %-14d %-14d%n",
                            size, caseName, algoName, elapsedMs, comparisons, movements));

                    String caseKey = caseName.equals("Best Case") ? "best"
                            : caseName.equals("Worst Case") ? "worst" : "average";
                    String outFile = "sorted_" + algoName.replace(" ", "_").toLowerCase()
                            + "_" + caseKey + "_n" + size + ".txt";
                    writeArrayToFile(arr, outFile);

                    System.out.printf("  %-15s done in %.3f ms (%d comparisons, %d moves) -> %s%n",
                            algoName, elapsedMs, comparisons, movements, outFile);
                }
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