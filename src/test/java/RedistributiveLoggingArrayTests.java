import cycles.RedistributiveArray;
import cycles.RedistributiveLoggingArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RedistributiveLoggingArrayTests {

    @Test
    public void nullArrayTest() {
        int[] arr = null;
        RedistributiveLoggingArray redistributiveLoggingArray = new RedistributiveLoggingArray(arr);
        assertArrayEquals(new int[0], redistributiveLoggingArray.getData());
        redistributiveLoggingArray.redistributeMaxValue();
        assert(redistributiveLoggingArray.isCycleFound());
        assertEquals(0, redistributiveLoggingArray.getCyclePeriod());
        assertArrayEquals(new int[0], redistributiveLoggingArray.getData());
        assertEquals(1, redistributiveLoggingArray.getIterationCount());
        redistributiveLoggingArray.rollback();
        assertArrayEquals(new int[0], redistributiveLoggingArray.getData());
        assertEquals(0, redistributiveLoggingArray.getIterationCount());
    }

    @Test
    public void emptyArrayTest() {
        int[] arr = new int[0];
        RedistributiveLoggingArray redistributiveLoggingArray = new RedistributiveLoggingArray(arr);
        assertArrayEquals(arr, redistributiveLoggingArray.getData());
        redistributiveLoggingArray.redistributeMaxValue();
        assert(redistributiveLoggingArray.isCycleFound());
        assertEquals(0, redistributiveLoggingArray.getCyclePeriod());
        assertArrayEquals(arr, redistributiveLoggingArray.getData());
        assertEquals(1, redistributiveLoggingArray.getIterationCount());
        redistributiveLoggingArray.rollback();
        assertArrayEquals(arr, redistributiveLoggingArray.getData());
        assertEquals(0, redistributiveLoggingArray.getIterationCount());
    }

    @Test
    public void zeroArrayTest() {
        int[] arr = {0, 0, 0};
        RedistributiveLoggingArray redistributiveLoggingArray = new RedistributiveLoggingArray(arr);
        assertArrayEquals(arr, redistributiveLoggingArray.getData());
        redistributiveLoggingArray.redistributeMaxValue();
        assert(redistributiveLoggingArray.isCycleFound());
        assertEquals(-1, redistributiveLoggingArray.getCyclePeriod());
        assertArrayEquals(arr, redistributiveLoggingArray.getData());
        assertEquals(1, redistributiveLoggingArray.getIterationCount());
        redistributiveLoggingArray.rollback();
        assertArrayEquals(arr, redistributiveLoggingArray.getData());
        assertEquals(0, redistributiveLoggingArray.getIterationCount());
    }

    @Test
    public void negativeArrayTest() {
        int[] arr = {-1, -2, -1};
        int[] expected = {0, -3, -1};
        RedistributiveLoggingArray redistributiveLoggingArray = new RedistributiveLoggingArray(arr);
        assertArrayEquals(arr, redistributiveLoggingArray.getData());
        redistributiveLoggingArray.redistributeMaxValue();
        assert(!redistributiveLoggingArray.isCycleFound());
        assertEquals(-1, redistributiveLoggingArray.getCyclePeriod());
        assertArrayEquals(expected, redistributiveLoggingArray.getData());
        assertEquals(1, redistributiveLoggingArray.getIterationCount());
        redistributiveLoggingArray.rollback();
        assertArrayEquals(arr, redistributiveLoggingArray.getData());
        assertEquals(0, redistributiveLoggingArray.getIterationCount());
    }

    @Test
    public void redistributionTest() {
        int[] arr = {0, 2, 7, 0};
        int[] expected = {2, 4, 1, 2};
        RedistributiveLoggingArray redistributiveLoggingArray = new RedistributiveLoggingArray(arr);
        redistributiveLoggingArray.redistributeMaxValue();
        assertArrayEquals(expected, redistributiveLoggingArray.getData());
        assertEquals(1, redistributiveLoggingArray.getIterationCount());
    }

    @Test
    public void simpleRollbackTest() {
        int[] arr = {0, 2, 7, 0};
        RedistributiveLoggingArray redistributiveLoggingArray = new RedistributiveLoggingArray(arr);
        redistributiveLoggingArray.redistributeMaxValue();
        redistributiveLoggingArray.rollback();
        assertArrayEquals(arr, redistributiveLoggingArray.getData());
        assertEquals(0, redistributiveLoggingArray.getIterationCount());
    }

    @Test
    public void doubleRollbackTest() {
        int[] arr0 = {0, 2, 7, 0};
        int[] arr1 = {2, 4, 1, 2};
        RedistributiveLoggingArray redistributiveLoggingArray = new RedistributiveLoggingArray(arr0);
        redistributiveLoggingArray.redistributeMaxValue();
        redistributiveLoggingArray.redistributeMaxValue();
        redistributiveLoggingArray.rollback();
        assertArrayEquals(arr1, redistributiveLoggingArray.getData());
        assertEquals(1, redistributiveLoggingArray.getIterationCount());
        redistributiveLoggingArray.rollback();
        assertArrayEquals(arr0, redistributiveLoggingArray.getData());
        assertEquals(0, redistributiveLoggingArray.getIterationCount());
    }

    @Test
    public void parameterRollbackTest() {
        int[] arr0 = {0, 2, 7, 0};
        int[] arr1 = {2, 4, 1, 2};
        RedistributiveLoggingArray redistributiveLoggingArray = new RedistributiveLoggingArray(arr0);
        redistributiveLoggingArray.redistributeMaxValue();
        redistributiveLoggingArray.redistributeMaxValue();
        redistributiveLoggingArray.redistributeMaxValue();
        redistributiveLoggingArray.rollback(1);
        assertArrayEquals(arr1, redistributiveLoggingArray.getData());
        assertEquals(1, redistributiveLoggingArray.getIterationCount());
    }

    @Test
    public void recoveryTest() {
        int[] arr0 = {0, 2, 7, 0};
        int[] arr1 = {2, 4, 1, 2};
        //int[] arr2 = {3, 1, 2, 3};
        int[] arr3 = {0, 2, 3, 4};
        RedistributiveLoggingArray redistributiveLoggingArray = new RedistributiveLoggingArray(arr0);
        redistributiveLoggingArray.redistributeMaxValue();
        redistributiveLoggingArray.redistributeMaxValue();
        redistributiveLoggingArray.redistributeMaxValue();
        int[] recoveredArr = redistributiveLoggingArray.recover(1);
        assertArrayEquals(arr1, recoveredArr);
        assertArrayEquals(arr3, redistributiveLoggingArray.getData());
        assertEquals(3, redistributiveLoggingArray.getIterationCount());
    }

    @Test
    public void invalidRecoveryTest() {
        int[] arr0 = {0, 2, 7, 0};
        int[] arr1 = {2, 4, 1, 2};
        //int[] arr2 = {3, 1, 2, 3};
        int[] arr3 = {0, 2, 3, 4};
        RedistributiveLoggingArray redistributiveLoggingArray = new RedistributiveLoggingArray(arr0);
        redistributiveLoggingArray.redistributeMaxValue();
        redistributiveLoggingArray.redistributeMaxValue();
        redistributiveLoggingArray.redistributeMaxValue();
        int[] recoveredArr = redistributiveLoggingArray.recover(-1);
        assertArrayEquals(arr0, recoveredArr);
        assertArrayEquals(arr3, redistributiveLoggingArray.getData());
        assertEquals(3, redistributiveLoggingArray.getIterationCount());
        recoveredArr = redistributiveLoggingArray.recover(10);
        assertArrayEquals(arr3, recoveredArr);
        assertArrayEquals(arr3, redistributiveLoggingArray.getData());
        assertEquals(3, redistributiveLoggingArray.getIterationCount());
    }

}
