import cycles.CycleSearchResult;
import cycles.DistributionCycles;

import java.util.Random;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        //int[] arr = {0, 5, 10, 0, 11, 14, 13, 4, 11, 8, 8, 7, 1, 4, 12, 11};
        //int[] arr = {0, 0, 0, 0, 10};
        int[] arr = generateRandomArray(30, 1000);

        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.currentTimeMillis();
        CycleSearchResult searchResult = DistributionCycles.evaluateRedistributionOfMaxValueCycle(arr);
        long elapsedTime = System.currentTimeMillis() - startTime;
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println(searchResult);
        System.out.println(elapsedTime + " ms " + (memoryAfter - memoryBefore) + " bytes");
    }

    private static int[] generateRandomArray(int bound, int count) {
        return IntStream.generate(() -> new Random().nextInt(bound)).limit(count).toArray();
    }

}
