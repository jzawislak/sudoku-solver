package com.sudoku.solver;

import com.sudoku.core.config.enums.ConfigEnum;
import com.sudoku.core.config.ConfigUtil;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Random;

/**
 * Główna klasa rozwiązująca sudoku.
 */
public class SudokuSolver {
    private static Logger LOGGER = org.apache.log4j.Logger.getLogger(SudokuSolver.class);
    private Random random = new Random(System.currentTimeMillis());

    /**
     * Tryb rozwiązywania jednego sudoku wczytanego z pliku.
     */
    public SudokuResult solveSingleSudoku() {
        Sudoku sudoku = SudokuParser.parseSudokuFromFile(ConfigUtil.getString(ConfigEnum.SUDOKU_FILE.getValue()));
        return this.solveSudoku(sudoku, Level.DEBUG);
    }

    /**
     * Rozwiązauje jedno sudoku.
     *
     * @return informacje pomocnicze o przebiegu rozwiązania
     */
    private SudokuResult solveSudoku(Sudoku sudoku, Level level) {
        LOGGER.setLevel(level);
        LOGGER.debug("Sudoku początkowe:\n" + sudoku);
        LOGGER.debug("Koszt początkowy: " + sudoku.cost());
        //kopia aby nie zmieniać sudoku podanego jako argument
        sudoku = sudoku.makeCopy();
        sudoku.initEmptyFields();
        LOGGER.trace("Zainicjalizowane:\n" + sudoku.toString());
        LOGGER.debug("Rozpoczęcie rozwiązywania, proszę czekać.");

        int outerIterations = ConfigUtil.getInt(ConfigEnum.ITERACJE_ZWEN.getValue());
        int innerIterations = ConfigUtil.getInt(ConfigEnum.ITERACJE_WEWN.getValue());
        double tau = ConfigUtil.getDouble(ConfigEnum.TAU.getValue());
        double alfa = ConfigUtil.getDouble(ConfigEnum.ALFA.getValue());
        int allIterations = 0;

        for (int i = 0; i < outerIterations; i++) {
            LOGGER.trace("Iteracja zewnętrzna " + i + ". Obecny koszt:" + sudoku.cost());
            for (int j = 0; j < innerIterations; j++) {
                Sudoku neighbour = sudoku.getNeighbour();
                int newCost = neighbour.cost();
                double sigma = newCost - sudoku.cost();
                if (newCost == 0) {
                    sudoku = neighbour;
                    LOGGER.debug("Rozwiazano, wynik:\n" + sudoku.toString());
                    LOGGER.info("Rozwiązanie znaleziono po liczbie iteracji: " + allIterations);
                    //System.out.print(allIterations);
                    //System.out.print(" \n");
                    return new SudokuResult(true, i, j, allIterations, sudoku);
                } else if (sigma < 0) {
                    sudoku = neighbour;
                } else if (random.nextDouble() < Math.exp(-sigma / tau)) {
                    sudoku = neighbour;
                }
                allIterations++;
            }
            tau = tau * alfa;
        }
        LOGGER.info("Brak rozwiązania.");
        //System.out.print("brak \n");
        return new SudokuResult(false, outerIterations, innerIterations, allIterations, sudoku);
    }

    /**
     * Przeprowadza pełny test na wczytanym sudoku. Zakłada się, że wczytane sudoku jest całkowicie wypełnione.
     */
    public void performFullTest() {
        Sudoku sudoku = SudokuParser.parseSudokuFromFile(ConfigUtil.getString(ConfigEnum.SUDOKU_FILE.getValue()));
        LOGGER.info("Pełny test.");
        if (!sudoku.wasSolved()) {
            LOGGER.error("Błąd! Wczytane sudoku nie jest uzupełnione." +
                    " Do wykonania pełnego testu należy wczytać rozwiązane sudoku.");
            return;
        }

        List<Integer> difficultyList = ConfigUtil.getIntList(ConfigEnum.POZIOMY_TRUDNOSCI.getValue());
        if (difficultyList == null || difficultyList.isEmpty()) {
            LOGGER.error("Błąd! Podaj chociaż jeden poziom trudności.");
            return;
        } else {
            LOGGER.info("Wczytane poziomy trudności: " + difficultyList);
        }

        for (Integer difficulty : difficultyList) {
            LOGGER.info("Test dla poziomu trudności " + difficulty);
            Sudoku difficultSudoku = sudoku.getNewWithFieldsRemoved(difficulty);
            int numberOfTests = ConfigUtil.getInt(ConfigEnum.ILOSC_TESTOW.getValue(), 1);
            long summaryNumberOfIterations = 0;
            int numberOfFailures = 0;
            for (int i = 0; i < numberOfTests; i++) {
                Level level = i == 0 ? Level.DEBUG : Level.INFO;
                SudokuResult sudokuResult = this.solveSudoku(difficultSudoku, level);
                if(sudokuResult.isSuccess) {
                    summaryNumberOfIterations += sudokuResult.allIterations;
                } else {
                    numberOfFailures++;
                }
            }
            LOGGER.info("Srednia liczba iteracji: " + (float) summaryNumberOfIterations/(numberOfTests - numberOfFailures));
            LOGGER.info("Procent porażek (procent): " + (float) numberOfFailures/numberOfTests * 100);
        }
    }
}
