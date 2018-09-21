import cycles.CycleSearchResult;
import cycles.DistributionCycles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class DistributionCyclesTests {

    @Test
    void nullArrayTest() {
        CycleSearchResult expected = new CycleSearchResult(0, 0);
        CycleSearchResult actual = DistributionCycles.evaluateRedistributionOfMaxValueCycle(null);
        assertEquals(expected, actual);
    }

    @Test
    void emptyArrayTest() {
        CycleSearchResult expected = new CycleSearchResult(0, 0);
        CycleSearchResult actual = DistributionCycles.evaluateRedistributionOfMaxValueCycle(new int[0]);
        assertEquals(expected, actual);
    }

    @Test
    void oneElementArrayTest() {
        int[] arr = {1};
        CycleSearchResult expected = new CycleSearchResult(1, 1);
        CycleSearchResult actual = DistributionCycles.evaluateRedistributionOfMaxValueCycle(arr);
        assertEquals(expected, actual);
    }

    @Test
    void zeroSumArrayTest() {
        int[] arr = {0, 0, 0, 0, 0, 0};
        CycleSearchResult expected = new CycleSearchResult(1, 1);
        CycleSearchResult actual = DistributionCycles.evaluateRedistributionOfMaxValueCycle(arr);
        assertEquals(expected, actual);
    }

    @Test
    void negativeElementsArrayTest() {
        int[] arr = {-1, -1, -1};
        CycleSearchResult expected = new CycleSearchResult(1, 2);
        CycleSearchResult actual = DistributionCycles.evaluateRedistributionOfMaxValueCycle(arr);
        assertEquals(expected, actual);
    }

    @Test
    void twoElementsArrayTest() {
        int[] arr = {100, 100};
        CycleSearchResult expected = new CycleSearchResult(2, 8);
        CycleSearchResult actual = DistributionCycles.evaluateRedistributionOfMaxValueCycle(arr);
        assertEquals(expected, actual);
    }

    @Test
    void smallArrayTest() {
        int[] arr = {0, 2, 7, 0};
        CycleSearchResult expected = new CycleSearchResult(4, 5);
        CycleSearchResult actual = DistributionCycles.evaluateRedistributionOfMaxValueCycle(arr);
        assertEquals(expected, actual);
    }

    @Test
    void mediumArrayTest() {
        int[] arr = {0, 5, 10, 0, 11, 14, 13, 4, 11, 8, 8, 7, 1, 4, 12, 11};
        CycleSearchResult expected = new CycleSearchResult(1695, 7864);
        CycleSearchResult actual = DistributionCycles.evaluateRedistributionOfMaxValueCycle(arr);
        assertEquals(expected, actual);
    }
}
