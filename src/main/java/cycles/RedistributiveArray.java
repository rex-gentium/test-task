package cycles;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/***
 * Массив с функцией перераспределения максимума по остальным ячейкам
 */
public class RedistributiveArray {
    private int[] data;
    private int maxValue, maxValueIndex;

    /***
     * @param data null воспринимается как пустой массив
     */
    public RedistributiveArray(int[] data) {
        if (data == null || data.length == 0) {
            this.data = new int[0];
            return;
        }
        this.data = new int[data.length];
        this.maxValue = data[0];
        this.maxValueIndex = 0;
        for (int i = 0; i < data.length; i++) {
            this.data[i] = data[i];
            if (maxValue < data[i]) {
                maxValue = data[i];
                maxValueIndex = i;
            }
        }
    }

    public RedistributiveArray(RedistributiveArray toCopy) {
        if (toCopy == null) {
            this.data = new int[0];
            return;
        }
        this.data = Arrays.copyOf(toCopy.data, toCopy.data.length);
        this.maxValue = toCopy.maxValue;
        this.maxValueIndex = toCopy.maxValueIndex;
    }

    /***
     * Обнуляет максимальный элемент и распределяет его значение между следующими ячейками (циклически, если достигнут
     * конец массива), обновляет максимум. Если длина массива меньше 2, операций не производится.
     */
    public void redistributeMaxValue() {
        if (data.length <= 1 || maxValue == 0) return; // нечего распределять
        int distributionValue = maxValue / data.length; // на сколько увеличить все ячейки в массиве
        int surplusCount = maxValue % data.length; // сколько следующих от максимума ячеек увеличить ещё на 1 (распределяем остаток от деления)
        int surplus = 1;
        if (maxValue < 0) { // поправка на минус
            surplusCount *= -1;
            surplus *= -1;
        }
        // обнуляем максимум и обходим со следующей ячейки
        data[maxValueIndex] = 0;
        int startIndex = (maxValueIndex + 1) % data.length;
        maxValue = data[startIndex];
        maxValueIndex = startIndex;
        int i = startIndex;
        do {
            if (surplusCount-- > 0) {
                data[i] += distributionValue + surplus;
            }
            else {
                data[i] += distributionValue;
            }
            if (data[i] > maxValue
                    || data[i] == maxValue && i < maxValueIndex) { // самый левый максимум
                maxValue = data[i];
                maxValueIndex = i;
            }
            if (++i >= data.length)
                i = 0;
        } while (i != startIndex);
    }

    /***
     * Откатывает массив на одно распределение назад
     * @param previousMaxValue предыдущий максимум
     * @param previousMaxIndex индекс предыдущего максимума
     */
    public void redistributeBack(int previousMaxValue, int previousMaxIndex) {
        if (data.length <= 1 || previousMaxValue == 0) return; // ничего не распределяли
        int distributionValue = previousMaxValue / data.length; // на сколько увеличили все ячейки в массиве
        int surplusCount = previousMaxValue % data.length; // сколько следующих от максимума ячеек увеличили ещё на 1
        int surplus = 1;
        if (previousMaxValue < 0) { // поправка на минус
            surplusCount *= -1;
            surplus *= -1;
        }
        data[previousMaxIndex] += previousMaxValue;
        int startIndex = (previousMaxIndex + 1) % data.length;
        int i = startIndex;
        do {
            if (surplusCount-- > 0) {
                data[i] -= (distributionValue + surplus);
            }
            else {
                data[i] -= distributionValue;
            }
            if (++i >= data.length)
                i = 0;
        } while (i != startIndex);
        maxValue = previousMaxValue;
        maxValueIndex = previousMaxIndex;
    }

    /***
     * Возвращает копию массива
     */
    public int[] getData() {
        return this.data.clone();
    }

    public List<Integer> asList() {
        return Arrays.stream(data).boxed().collect(Collectors.toList());
    }

    public Integer[] getDataBoxed() {
        return IntStream.of(data).boxed().toArray(Integer[]::new);
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
        RedistributiveArray that = (RedistributiveArray) o;
        return Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

}
