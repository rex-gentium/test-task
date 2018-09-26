import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import cycles.ArrayChangeLog;
import cycles.RedistributiveLoggingArray;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayChangeLogTests {

    @Test
    public void emptyLogTest() {
        ArrayChangeLog log = new ArrayChangeLog();
        assertEquals(0, log.size());
        assertNull(log.getEntry(5));
        assertNull(log.getLastEntry());
        assert(log.getIterationsMatchingHashCode(561).isEmpty());
    }

    @Test
    public void oneEntryLogTest() {
        int[] arr = {0, 3, 4};
        RedistributiveLoggingArray redistributiveLoggingArray = new RedistributiveLoggingArray(arr);
        ArrayChangeLog log = redistributiveLoggingArray.getChangeLog();
        assertEquals(1, log.size());
        assertNull(log.getEntry(1));
        assertNotNull(log.getEntry(0));
        assertNotNull(log.getLastEntry());
        assertEquals(log.getEntry(0), log.getLastEntry());
        int arrHash = Arrays.hashCode(arr);
        List<Integer> expectedIterations = new ArrayList<>(1);
        expectedIterations.add(0);
        assert(!log.getIterationsMatchingHashCode(arrHash).isEmpty());
        assert(expectedIterations.equals(log.getIterationsMatchingHashCode(arrHash)));
    }

    @Test
    public void sameEntriesLogTest() {
        int[] arr = {0, 0, 0};
        int arrHash = Arrays.hashCode(arr);
        RedistributiveLoggingArray redistributiveLoggingArray = new RedistributiveLoggingArray(arr);
        ArrayChangeLog log = redistributiveLoggingArray.getChangeLog();
        assertEquals(log.getEntry(0), log.getLastEntry());
        List<Integer> expectedIterations = new ArrayList<>(2);
        expectedIterations.add(0);
        expectedIterations.add(1);
        assert(log.getIterationsMatchingHashCode(arrHash).equals(expectedIterations));
    }
}
