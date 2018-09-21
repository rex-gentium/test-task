package cycles;

import java.util.Objects;

/***
 * Структура данных, описывающая результат поиска цикла перераспределения максимума в массиве
 * */

public class CycleSearchResult {

    private int cycleLength; // длина цикла
    private int iterationsRequired; // количество шагов, необходимых для поиска цикла

    public CycleSearchResult(int cycleLength, int iterationsRequired) {
        this.cycleLength = cycleLength;
        this.iterationsRequired = iterationsRequired;
    }

    public int getCycleLength() {
        return cycleLength;
    }

    public int getIterationsRequired() {
        return iterationsRequired;
    }

    @Override
    public String toString() {
        return "Cycle period = " + cycleLength +
                "; cycle search iterations = " + iterationsRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CycleSearchResult that = (CycleSearchResult) o;
        return cycleLength == that.cycleLength &&
                iterationsRequired == that.iterationsRequired;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cycleLength, iterationsRequired);
    }
}
