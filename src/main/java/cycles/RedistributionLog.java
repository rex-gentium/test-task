package cycles;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***
 * Лог состояния массива
 * по хеш-коду хранит список итераций, на которых встретился массив с таким хешем
 * по номеру итерации хранит максимальное значение и его индекс по состоянию на ту итерацию
 */
public class RedistributionLog {
    private HashMap<Integer, List<Integer>> arrayHashToIterationsList;
    private List<RedistributionLogEntry> maximumValuesLog;
    private boolean isCycleFound;
    private int cyclePeriod;

    /***
     * запись лога отражает состояние массива на какой-то итерации
     */
    private class RedistributionLogEntry {
        private final int maxValue, maxValueIndex;

        private RedistributionLogEntry(int maxValue, int maxValueIndex) {
            this.maxValue = maxValue;
            this.maxValueIndex = maxValueIndex;
        }

        private RedistributionLogEntry(RedistributiveArray array) {
            this.maxValue = array.getMaxValue();
            this.maxValueIndex = array.getMaxValueIndex();
        }
    }

    public RedistributionLog() {
        arrayHashToIterationsList = new HashMap<>();
        maximumValuesLog = new ArrayList<>();
        isCycleFound = false;
        cyclePeriod = 0;
    }

    /***
     * вносит в конец лога запись о состоянии массива
     * @param array массив, null воспринимается как пустой
     */
    public void addEntry(RedistributiveArray array) {
        if (array == null)
            array = new RedistributiveArray(new int[0]);
        int currentIteration = maximumValuesLog.size();
        int arrayHashCode = array.hashCode();
        List<Integer> presentIterations = arrayHashToIterationsList.putIfAbsent(arrayHashCode, new ArrayList<>(1));
        if (presentIterations == null)
            arrayHashToIterationsList.get(arrayHashCode).add(currentIteration);
        else {
            //
            for (Integer iteration : presentIterations) {
                if (assertCycleAfterBackTrace(iteration, array)) {
                    this.isCycleFound = true;
                    this.cyclePeriod = currentIteration - iteration;
                }
            }
            presentIterations.add(currentIteration);
        }
        maximumValuesLog.add(new RedistributionLogEntry(array));
    }

    /***
     * По информации из лога возвращает массив на несколько состояний назад и сравнивает восстановленный массив с исходным
     * @param expectedIteration номер итерации, к которой восстанавливается массив
     * @param arrayToBacktrace массив для восстановления (не меняется в ходе выполнения)
     * @return true если восстановленный массив равен исходному (образуют цикл), иначе false
     */
    private boolean assertCycleAfterBackTrace(int expectedIteration, RedistributiveArray arrayToBacktrace) {
        RedistributionLogEntry expectedState = maximumValuesLog.get(expectedIteration);
        if (expectedState.maxValue != arrayToBacktrace.getMaxValue() || expectedState.maxValueIndex != arrayToBacktrace.getMaxValueIndex())
            return false;
        RedistributiveArray expectedAfterBacktrace = new RedistributiveArray(arrayToBacktrace);
        int currentIteration = maximumValuesLog.size();
        int stepsBack = currentIteration - expectedIteration;
        for (int i = 0; i < stepsBack; i++) {
            int previousMaximumValue = maximumValuesLog.get(currentIteration - i - 1).maxValue;
            int previousMaximumIndex = maximumValuesLog.get(currentIteration - i - 1).maxValueIndex;
            arrayToBacktrace.redistributeBack(previousMaximumValue, previousMaximumIndex);
        }
        return expectedAfterBacktrace.equals(arrayToBacktrace);
    }

    public boolean isCycleFound() {
        return isCycleFound;
    }

    public int getCyclePeriod() {
        return cyclePeriod;
    }

    public int getIterationCount() {
        return maximumValuesLog.size() - 1;
    }
}
