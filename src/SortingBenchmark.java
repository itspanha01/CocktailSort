import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Benchmarks Cocktail Sort, Insertion Sort, and Selection Sort across three
 * dataset sizes (5, 1,000, 1,000,000), recording runtime, comparisons, and
 * swaps/movements for each run. Writes the sorted output for every run to
 * its own .txt file, and writes a combined performance table to
 * performance_results.txt.
 */
public class SortingBenchmark {

    /** Holds the outcome of one sort run. */
    static class SortResult {
        long comparisons;
        long movements;   // "swaps" for cocktail/selection, "shifts" for insertion
        long elapsedNanos;
    }

    // ---------- Instrumented sorting algorithms ----------

    static SortResult cocktailSort(int[] arr) {
        SortResult result = new SortResult();
        long startTime = System.nanoTime();

        int start = 0;
        int end = arr.length - 1;
        boolean swapped = true;

        while (swapped) {
            swapped = false;

            for (int i = start; i < end; i++) {
                result.comparisons++;
                if (arr[i] > arr[i + 1]) {
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    result.movements++;
                    swapped = true;
                }
            }
            end--;

            if (!swapped) break;

            swapped = false;

            for (int i = end; i > start; i--) {
                result.comparisons++;
                if (arr[i - 1] > arr[i]) {
                    int temp = arr[i - 1];
                    arr[i - 1] = arr[i];
                    arr[i] = temp;
                    result.movements++;
                    swapped = true;
                }
            }
            start++;
        }

        result.elapsedNanos = System.nanoTime() - startTime;
        return result;
    }

    static SortResult insertionSort(int[] arr) {
        SortResult result = new SortResult();
        long startTime = System.nanoTime();

        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0) {
                result.comparisons++;
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    result.movements++;
                    j--;
                } else {
                    break;
                }
            }
            arr[j + 1] = key;
        }

        result.elapsedNanos = System.nanoTime() - startTime;
        return result;
    }

    static SortResult selectionSort(int[] arr) {
        SortResult result = new SortResult();
        long startTime = System.nanoTime();

        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                result.comparisons++;
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
                result.movements++;
            }
        }

        result.elapsedNanos = System.nanoTime() - startTime;
        return result;
    }

    // ---------- Helpers ----------

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

    // ---------- Benchmark driver ----------

    public static void main(String[] args) throws IOException {
        int[] sizes = {5, 1_000, 1_000_000};
        String[] algorithmNames = {"Cocktail Sort", "Insertion Sort", "Selection Sort"};

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

                SortResult result;
                if (algoName.equals("Cocktail Sort")) {
                    result = cocktailSort(arr);
                } else if (algoName.equals("Insertion Sort")) {
                    result = insertionSort(arr);
                } else {
                    result = selectionSort(arr);
                }

                double elapsedMs = result.elapsedNanos / 1_000_000.0;

                report.append(String.format("%-15d %-15s %-15.3f %-15d %-15d%n",
                        size, algoName, elapsedMs, result.comparisons, result.movements));

                String outFile = "sorted_" + algoName.replace(" ", "_").toLowerCase() + "_n" + size + ".txt";
                writeArrayToFile(arr, outFile);

                System.out.printf("  %-15s done in %.3f ms (%d comparisons, %d moves) -> %s%n",
                        algoName, elapsedMs, result.comparisons, result.movements, outFile);
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