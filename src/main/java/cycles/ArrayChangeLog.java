package cycles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/***
 * История перераспределения максимума в массиве.
 * Хранит не массивы, а только данные о максимальном элементе между итерациями перераспределения.
 * Записи в хранятся как по номерам итераций, так и по хеш-кодам массивов-результатов этих итераций.
 * Нулевая итерация относится к изначальному состоянию массива.
 */
public class ArrayChangeLog {
    // хеш массива -> итерации, на которых массив с таким хешем встречался
    private HashMap<Integer, List<Integer>> arrayHashToLogIndices;
    // ключевые данные о массиве (максимумы и их индексы) по итерациям
    private List<LogEntry> entries;

    /***
     * данные о левом максимуме в массиве
     */
    public static class LogEntry {
        private final int maxValue, maxValueIndex;

        public LogEntry(int maxValue, int maxValueIndex) {
            this.maxValue = maxValue;
            this.maxValueIndex = maxValueIndex;
        }

        public int getMaxValue() {
            return maxValue;
        }

        public int getMaxValueIndex() {
            return maxValueIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LogEntry logEntry = (LogEntry) o;
            return maxValue == logEntry.maxValue &&
                    maxValueIndex == logEntry.maxValueIndex;
        }

        @Override
        public int hashCode() {
            return Objects.hash(maxValue, maxValueIndex);
        }
    }

    public ArrayChangeLog() {
        arrayHashToLogIndices = new HashMap<>();
        entries = new ArrayList<>();
    }

    /***
     * Возвращает данные о максимуме по номеру итерации
     * @param index номер итерации
     * @return данные о максимуме, null если итерации с таким номером нет
     */
    public LogEntry getEntry(int index) {
        if (index >= entries.size())
            return null;
        return entries.get(index);
    }

    /***
     * Возвращает список номеров итераций, на которых встречался массив с данным хеш-кодом
     * @param hashCode хеш-код массива
     * @return список номеров итераций, упорядоченный по возрастанию; пустой список, если хеш-код не встречался ни разу
     */
    public List<Integer> getIterationsMatchingHashCode(int hashCode) {
        return arrayHashToLogIndices.getOrDefault(hashCode, new ArrayList<>(0));
    }

    /***
     * добавляет запись о состоянии массива в лог
     * @param hashCode хеш-код массива
     * @param entry данные о массиве
     */
    void addEntry(int hashCode, LogEntry entry) {
        entries.add(entry);
        int entryIndex = entries.size() - 1;
        arrayHashToLogIndices.putIfAbsent(hashCode, new ArrayList<>(1));
        arrayHashToLogIndices.get(hashCode).add(entryIndex);
    }

    /***
     * Удаляет последнюю запись из лога
     */
    void removeLastEntry() {
        int entryIndex = entries.size() - 1;
        entries.remove(entryIndex);
        for (int hashCode : arrayHashToLogIndices.keySet()) {
            arrayHashToLogIndices.get(hashCode).remove(new Integer(entryIndex));
        }
    }

    /***
     * Удаляет последнюю запись из лога
     * @param hashCode хеш-код массива, к которому относится последняя запись;
     *                 если массива с таким кодом не было, не производит удаления
     */
    void removeLastEntry(int hashCode) {
        if (!arrayHashToLogIndices.containsKey(hashCode)) return;
        int entryIndex = entries.size() - 1;
        entries.remove(entryIndex);
        arrayHashToLogIndices.get(hashCode).remove(new Integer(entryIndex));
    }

    /***
     * @return количество записей в логе
     */
    public int size() {
        return entries.size();
    }

    /***
     * @return последнюю запись в логе, null если лог пустой
     */
    public LogEntry getLastEntry() {
        if (entries.isEmpty()) return null;
        return entries.get(entries.size() - 1);
    }
}
