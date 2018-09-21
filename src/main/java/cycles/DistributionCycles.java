package cycles;

import java.util.*;

/***
 * Интерфейс со статическими методами поиска циклов распределения в массивах
 */
public abstract class DistributionCycles {

    /***
     * Возвращает количество итераций, необходимых для обнаружения цикла перераспределния максимального значения
     * по массиву
     * @param arr массив целых чисел, для null и пустого массива вернет (0, 0)
     * @return результат поиска (длина цикла, количество итераций поиска)
     */
    public static CycleSearchResult evaluateRedistributionOfMaxValueCycle(int[] arr) {
        return isAcceptableMemoryDemand(arr)
                ? evaluateRedistributionOfMaxValueCycleTimeEfficient(arr)
                : evaluateRedistributionOfMaxValueCycleMemoryEfficient(arr) ;
    }

    /***
     * Оценка расхода памяти на быстрый поиск цикла
     * @param arr массив для поиска цикла
     * @return true, если расход памяти приемлемый
     */
    private static boolean isAcceptableMemoryDemand(int[] arr) {
        // условие можно изменить на более приемлемое для данной машины,
        // напр. Runtime.getRuntime().freeMemory() > N;
        return arr.length < 1000;
    }

    /***
     * Возвращает количество итераций, необходимых для обнаружения цикла перераспределния максимального значения
     * по массиву. Работает быстрее, но расходует много памяти.
     * @param arr массив целых чисел, для null и пустого массива вернет (0, 0)
     * @return результат поиска (длина цикла, количество итераций поиска)
     */
    private static CycleSearchResult evaluateRedistributionOfMaxValueCycleTimeEfficient(int[] arr) {
        if (arr == null || arr.length == 0)
            return new CycleSearchResult(0, 0);
        RedistributiveArray redistributiveArray = new RedistributiveArray(arr);
        int iterationCount = 0;
        int cycleLength = 0;
        // карта: массив -> номер итерации
        HashMap<RedistributiveArray, Integer> redistributionCash = new HashMap<>();
        redistributionCash.put(redistributiveArray, iterationCount);
        while (true) {
            ++iterationCount;
            redistributiveArray.redistributeMaxValue();
            Integer previousEntry = redistributionCash.put(redistributiveArray, iterationCount);
            if (previousEntry != null) {
                cycleLength = iterationCount - previousEntry;
                break;
            }
        }
        return new CycleSearchResult(cycleLength, iterationCount);
    }

    /***
     * Возвращает количество итераций, необходимых для обнаружения цикла перераспределния максимального значения
     * по массиву
     * @param arr массив целых чисел, для null и пустого массива вернет (0, 0)
     * @return результат поиска (длина цикла, количество итераций поиска)
     */
    private static CycleSearchResult evaluateRedistributionOfMaxValueCycleMemoryEfficient(int[] arr) {
        if (arr == null || arr.length == 0)
            return new CycleSearchResult(0, 0);
        RedistributiveArray redistributiveArray = new RedistributiveArray(arr);
        // карта: хеш массива -> максимальное значение, его индекс и номер итерации
        RedistributionLog log = new RedistributionLog();
        log.addEntry(redistributiveArray);
        while (!log.isCycleFound()) {
            redistributiveArray.redistributeMaxValue();
            log.addEntry(redistributiveArray);
        }
        return new CycleSearchResult(log.getCyclePeriod(), log.getIterationCount());
    }

}
