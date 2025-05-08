import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Name: Yash Parmar
 * Pledge: I pledge my honor that I have abided by the Stevens Honor System.
 *
 * This class performs a set of experiments to test the performance of different types of hash tables,
 * including linear and quadratic probing hash tables. It evaluates the average number of accesses needed
 * for contains operations under various scenarios such as insertion, deletion, and re-insertion.
 *
 * @author: Yash Parmar
 */

public class HashTableTester {

    /** The number of trials to run for each test scenario. */
    private static final int NUM_TRIALS = 100;

    /** The number of lookups to perform in each trial for performance measurement. */
    private static final int NUM_LOOKUPS = 1000;

    /** A Random instance initialized with a seed value of 42 for generating random numbers. */
    private static final Random rand = new Random(42);

    /**
     * Main entry point for the hash table testing scenarios.
     *
     * Executes three different scenarios:
     * 1. Linear HashTable (insertion only).
     * 2. Linear HashTable (insertion with deletions).
     * 3. Quadratic HashTable (insertion only).
     *
     * Displays results for various table sizes and numbers of elements.
     *
     * @param args Command-line arguments (not used).
     */

    public static void main(String[] args) {
        System.out.println("=== Scenario 1: LinearHashTable ===");
        System.out.println("m\t|n\t|Average accesses/contains");
        System.out.println("----+---+--------------------------");

        testScenario1(100, 0);
        testScenario1(4, 2);
        testScenario1(6, 3);
        testScenario1(8, 4);
        testScenario1(20, 10);
        testScenario1(50, 25);
        testScenario1(100, 25);
        testScenario1(1000, 25);
        testScenario1(1000, 50);
        testScenario1(1000, 100);
        testScenario1(1000, 200);
        testScenario1(1000, 500);
        testScenario1(1000, 750);
        testScenario1(1000, 900);
        testScenario1(1000, 950);
        testScenario1(1000, 975);
        testScenario1(2000, 1000);

        System.out.println("\n=== Scenario 2: LinearHashTable with Deletions ===");
        System.out.println("m\t|n\t|Average accesses/contains");
        System.out.println("----+---+--------------------------");

        testScenario2(4, 2);
        testScenario2(100, 50);
        testScenario2(1000, 100);
        testScenario2(1000, 200);
        testScenario2(1000, 500);
        testScenario2(1000, 750);
        testScenario2(1000, 900);
        testScenario2(1000, 950);
        testScenario2(1000, 975);

        System.out.println("\n=== Scenario 3: QuadraticHashTable ===");
        System.out.println("m\t|n\t|Average accesses/contains");
        System.out.println("----+---+--------------------------");

        testScenario3(100, 0);
        testScenario3(4, 2);
        testScenario3(6, 3);
        testScenario3(8, 4);
        testScenario3(20, 10);
        testScenario3(50, 25);
        testScenario3(100, 50);
        testScenario3(100, 20);
        testScenario3(100, 10);
        testScenario3(1000, 50);
        testScenario3(2000, 1000);
    }

    /**
     * Tests the performance of a linear hash table by inserting a given number of elements
     * and performing lookup operations to calculate the average number of accesses required.
     *
     * @param capacity The size of the hash table.
     * @param numElements The number of elements to insert into the table.
     */

    private static void testScenario1(int capacity, int numElements) {
        double totalAccesses = 0;
        int successfulTrials = 0;

        for (int trial = 0; trial < NUM_TRIALS; trial++) {
            try {
                LinearHashTable<Integer> table = new LinearHashTable<>(capacity);
                List<Integer> insertedElements = generateRandomElements(numElements);

                for (Integer element : insertedElements) {
                    try {
                        table.insert(element);
                    } catch (OutOfSpaceException e) {
                        break;
                    }
                }

                table.resetAccesses();

                double trialAccesses = performLookups(table, insertedElements);
                totalAccesses += trialAccesses;
                successfulTrials++;
            } catch (Exception e) {
                continue;
            }
        }

        if (successfulTrials > 0) {
            double avgAccesses = totalAccesses / successfulTrials;
            System.out.printf("%d\t|%d\t|%.2f\n", capacity, numElements, avgAccesses);
        } else {
            System.out.printf("%d\t|%d\t|N/A (all trials failed)\n", capacity, numElements);
        }
    }

    /**
     * Tests the performance of a linear hash table with insertions and deletions, calculating
     * the average number of accesses during lookups.
     *
     * @param capacity The size of the hash table.
     * @param numElements The number of elements to insert and delete in the table.
     */

    private static void testScenario2(int capacity, int numElements) {
        double totalAccesses = 0;
        int successfulTrials = 0;

        for (int trial = 0; trial < NUM_TRIALS; trial++) {
            try {
                LinearHashTable<Integer> table = new LinearHashTable<>(capacity);
                List<Integer> firstBatch = generateRandomElements(numElements);

                for (Integer element : firstBatch) {
                    try {
                        table.insert(element);
                    } catch (OutOfSpaceException e) {
                        break;
                    }
                }

                for (Integer element : firstBatch) {
                    table.delete(element);
                }

                List<Integer> secondBatch = generateRandomElements(numElements);
                for (Integer element : secondBatch) {
                    try {
                        table.insert(element);
                    } catch (OutOfSpaceException e) {
                        break;
                    }
                }

                table.resetAccesses();

                double trialAccesses = performLookups(table, secondBatch);
                totalAccesses += trialAccesses;
                successfulTrials++;
            } catch (Exception e) {
                continue;
            }
        }

        if (successfulTrials > 0) {
            double avgAccesses = totalAccesses / successfulTrials;
            System.out.printf("%d\t|%d\t|%.2f\n", capacity, numElements, avgAccesses);
        } else {
            System.out.printf("%d\t|%d\t|N/A (all trials failed)\n", capacity, numElements);
        }
    }

    /**
     * Tests the performance of a quadratic hash table by inserting a given number of elements
     * and performing lookup operations to calculate the average number of accesses required.
     *
     * @param capacity The size of the hash table.
     * @param numElements The number of elements to insert into the table.
     */

    private static void testScenario3(int capacity, int numElements) {
        double totalAccesses = 0;
        int successfulTrials = 0;

        for (int trial = 0; trial < NUM_TRIALS; trial++) {
            try {
                QuadraticHashTable<Integer> table = new QuadraticHashTable<>(capacity);
                List<Integer> insertedElements = generateRandomElements(numElements);

                for (Integer element : insertedElements) {
                    try {
                        table.insert(element);
                    } catch (OutOfSpaceException e) {
                        break;
                    }
                }

                table.resetAccesses();

                double trialAccesses = performLookups(table, insertedElements);
                totalAccesses += trialAccesses;
                successfulTrials++;
            } catch (Exception e) {
                continue;
            }
        }

        if (successfulTrials > 0) {
            double avgAccesses = totalAccesses / successfulTrials;
            System.out.printf("%d\t|%d\t|%.2f\n", capacity, numElements, avgAccesses);
        } else {
            System.out.printf("%d\t|%d\t|N/A (all trials failed)\n", capacity, numElements);
        }
    }

    /**
     * Generates a list of random integers to be inserted into the hash table.
     *
     * @param count The number of random integers to generate.
     * @return A list containing the generated random integers.
     */

    private static List<Integer> generateRandomElements(int count) {
        Random rand = new Random();
        int[] arr = rand.ints(count).toArray();
        List<Integer> elements = Arrays.stream(arr).boxed().collect(Collectors.toList());

        return elements;
    }

    /**
     * Performs lookup operations on the provided hash table and calculates the total number of accesses.
     *
     * @param table The hash table to perform lookups on.
     * @param elements The list of elements to look up.
     * @return The average number of accesses required for the lookups.
     */

    private static double performLookups(StatisticsSet<Integer> table, List<Integer> elements) {
        if (elements.isEmpty()) {
            return 0;
        }

        int totalAccesses = 0;

        for (int i = 0; i < NUM_LOOKUPS; i++) {
            int previousAccesses = table.accessCount();

            int randomIndex = rand.nextInt(elements.size());
            Integer element = elements.get(randomIndex);

            table.contains(element);

            totalAccesses += (table.accessCount() - previousAccesses);
        }

        return (double) totalAccesses / NUM_LOOKUPS;
    }
}