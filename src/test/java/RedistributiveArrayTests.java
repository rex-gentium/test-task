import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cycles.RedistributiveArray;
import org.junit.jupiter.api.Test;

class RedistributiveArrayTests {

    @Test
    void nullRedistributionTest() {
        int[] arr = null;
        int[] expected = {};
        RedistributiveArray array = new RedistributiveArray(arr);
        array.redistributeMaxValue();
        assertArrayEquals(expected, array.getData());
    }

    @Test
    void emptyRedistributionTest() {
        int[] arr = {};
        int[] expected = {};
        RedistributiveArray array = new RedistributiveArray(arr);
        array.redistributeMaxValue();
        assertArrayEquals(expected, array.getData());
    }

    @Test
    void zeroRedistributionTest() {
        int[] arr = {0, 0, 0, 0};
        int[] expected = {0, 0, 0, 0};
        RedistributiveArray array = new RedistributiveArray(arr);
        array.redistributeMaxValue();
        assertArrayEquals(expected, array.getData());
    }

    @Test
    void negativeRedistributionTest() {
        int[] arr = {-1, -2, -1};
        int[] expected = {0, -3, -1};
        RedistributiveArray array = new RedistributiveArray(arr);
        array.redistributeMaxValue();
        assertArrayEquals(expected, array.getData());
    }

    @Test
    void redistributionTest() {
        int[] arr = {0, 2, 7, 0};
        int[] expected = {2, 4, 1, 2};
        RedistributiveArray array = new RedistributiveArray(arr);
        array.redistributeMaxValue();
        assertArrayEquals(expected, array.getData());
    }

    @Test
    void backRedistributionTest() {
        int[] arr = {0, 2, 7, 0};
        int[] expectedRedistribution = {2, 4, 1, 2};
        RedistributiveArray array = new RedistributiveArray(arr);
        array.redistributeMaxValue();
        assertArrayEquals(expectedRedistribution, array.getData());
        array.redistributeBack(7, 2);
        assertArrayEquals(arr, array.getData());
    }
}
