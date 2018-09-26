package cycles;

import java.util.Arrays;
import java.util.List;

/***
 * Массив с функцией перераспределения максимального значения по остальным ячейкам.
 * Хранит историю распределений и может быть возвращен к любому предыдущему состоянию (или восстановить его копию).
 * Ищет в истории цикл перераспределения только при вызове соответствующего метода (не ищет после каждой итерации).
 * Регистрирует длину цикла единожды, независимо от откатов.
 */
public class RedistributiveLoggingArray extends RedistributiveArray {
    private int[] initialData;
    private ArrayChangeLog changeLog;
    private boolean isCycleFound;
    private int cyclePeriod;

    /***
     * @param arr null воспринимается как пустой массив
     */
    public RedistributiveLoggingArray(int[] arr) {
        super(arr);
        initialData = Arrays.copyOf(data, data.length);
        changeLog = new ArrayChangeLog();
        changeLog.addEntry(hashCode(), new ArrayChangeLog.LogEntry(maxValue, maxValueIndex));
        isCycleFound = initialData.length == 0;
        cyclePeriod = (isCycleFound) ? 0 : -1;
    }

    public boolean isCycleFound() {
        if (!isCycleFound)
            checkCycle();
        return isCycleFound;
    }

    /***
     * Ищет совпадение текущего массива с одним из его предыдущих состояний (самым недавним, если таких несколько)
     */
    private void checkCycle() {
        ArrayChangeLog.LogEntry lastEntry = changeLog.getLastEntry(); // запись о текущем состоянии
        List<Integer> sameHashOccurrences = changeLog.getIterationsMatchingHashCode(this.hashCode());
        for (int i = sameHashOccurrences.size() - 1; i >= 0; i--) { // ищем самое недавнее совпадение
            int iterationNumber = sameHashOccurrences.get(i);
            ArrayChangeLog.LogEntry pastEntry = changeLog.getEntry(iterationNumber);
            if (pastEntry == lastEntry // одна и та же итерация
                    || !pastEntry.equals(lastEntry)) // не совпадают максимумы
                continue;
            int[] recoveredArray = recover(iterationNumber);
            isCycleFound = Arrays.equals(recoveredArray, this.data);
            if (isCycleFound) {
                cyclePeriod = getIterationCount() - iterationNumber;
                break;
            }
        }
    }

    /***
     * Возвращает массив по состоянию на данную итерацию
     * @param iterationNumber желаемая итерация,
     *                        для отрицательных значений вернет начальный массив
     *                        для слишком больших значений вернет текущий массив
     * @return восстановленный массив
     */
    public int[] recover(int iterationNumber) {
        if (iterationNumber < 0) return Arrays.copyOf(initialData, initialData.length);
        if (iterationNumber >= getIterationCount()) return getData();
        return (iterationNumber < getIterationCount() / 2)
                ? recoverFromInitial(iterationNumber)
                : recoverFromCurrent(iterationNumber);
    }

    @Override
    public void redistributeMaxValue() {
        super.redistributeMaxValue();
        changeLog.addEntry(hashCode(), new ArrayChangeLog.LogEntry(maxValue, maxValueIndex));
    }

    @Override
    public void rollback(int previousMaxValue, int previousMaxIndex) {
        changeLog.removeLastEntry(hashCode());
        super.rollback(previousMaxValue, previousMaxIndex);
    }

    /***
     * Откатывает массив на одно распределение назад.
     * Если массив находится в изначальном состоянии, откат не производится
     */
    public void rollback() {
        if (changeLog.size() <= 1) return;
        changeLog.removeLastEntry(hashCode());
        ArrayChangeLog.LogEntry entry = changeLog.getLastEntry();
        super.rollback(entry.getMaxValue(), entry.getMaxValueIndex());
    }

    /***
     * Откатывает массив к указанной итерации распределения
     * @param toIteration желаемая итерация; для нуля откат к исходному массиву; для отрицательной отката не произойдет
     */
    public void rollback(int toIteration) {
        if (toIteration < 0) return;
        int stepsCount = getIterationCount() - toIteration;
        for (int i = 0; i < stepsCount; i++)
            rollback();
    }

    /***
     * Восстанавливает массив к состоянию на данную итерацию из начального массива
     * @param iterationNumber желаемая итерация
     * @return восстановленный массив
     */
    private int[] recoverFromCurrent(Integer iterationNumber) {
        RedistributiveArray base = new RedistributiveArray(this);
        int iterationCount = getIterationCount();
        for (int i = iterationCount - 1; i >= iterationNumber; i--) {
            ArrayChangeLog.LogEntry entryAboutPreviousArray = changeLog.getEntry(i);
            int previousMaximumValue = entryAboutPreviousArray.getMaxValue();
            int previousMaximumIndex = entryAboutPreviousArray.getMaxValueIndex();
            base.rollback(previousMaximumValue, previousMaximumIndex);
        }
        return base.getData();
    }

    /***
     * Восстанавливает массив к состоянию на данную итерацию из текущего массива
     * @param iterationNumber желаемая итерация
     * @return восстановленный массив
     */
    private int[] recoverFromInitial(int iterationNumber) {
        RedistributiveArray base = new RedistributiveArray(initialData);
        for (int i = 0; i < iterationNumber; i++)
            base.redistributeMaxValue();
        return base.getData();
    }

    /***
     * @return количество произведенных перераспределений
     */
    public int getIterationCount() {
        return changeLog.size() - 1;
    }

    public ArrayChangeLog getChangeLog() {
        return changeLog;
    }

    /***
     * @return длина цикла, -1 если цикла еще не обнаружено
     */
    public int getCyclePeriod() {
        return cyclePeriod;
    }

}
