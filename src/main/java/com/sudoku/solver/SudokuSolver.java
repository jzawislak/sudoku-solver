package com.sudoku.solver;

import com.sudoku.core.config.ConfigEnum;
import com.sudoku.core.config.ConfigUtil;
import org.apache.log4j.Logger;

import java.util.Random;

/**
 * Główna klasa rozwiązująca sudoku.
 */
public class SudokuSolver {
    private static Logger LOGGER = org.apache.log4j.Logger.getLogger(SudokuSolver.class);
    private Random random = new Random(System.currentTimeMillis());

    /**
     * Rozwiązauje jedno sudoku.
     */
    public void solveSudoku() {
        ConfigUtil.init("sudoku-solver");
        Sudoku sudoku = SudokuParser.parseSudokuFromFile(ConfigUtil.getString(ConfigEnum.SUDOKU_FILE.getValue()));
        LOGGER.info("Koszt początkowy: " + sudoku.cost());
        sudoku.init();
        LOGGER.info("Zainicjalizowane:\n" + sudoku.toString());


        int outerIterations = ConfigUtil.getInt(ConfigEnum.ITERACJE_ZWEN.getValue());
        int innerIterations = ConfigUtil.getInt(ConfigEnum.ITERACJE_WEWN.getValue());
        double tau = ConfigUtil.getDouble(ConfigEnum.TAU.getValue());
        double alfa = ConfigUtil.getDouble(ConfigEnum.ALFA.getValue());
        int allIterations = 0;

        for (int i = 0; i < outerIterations; i++) {
            LOGGER.info("Iteracja zewnętrzna " + i + ". Obecny koszt:" + sudoku.cost());
            for (int j = 0; j < innerIterations; j++) {
                Sudoku neighbour = sudoku.getNeighbour();
                int newCost = neighbour.cost();
                double sigma = newCost - sudoku.cost();
                if (newCost == 0) {
                    sudoku = neighbour;
                    LOGGER.info("Rozwiazano, wynik:\n" + sudoku.toString());
                    LOGGER.info("Ilosc iteracji: " + allIterations);
                    return;
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
    }
}
