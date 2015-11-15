package com.sudoku.solver;

import com.sudoku.core.config.ConfigEnum;
import com.sudoku.core.config.ConfigUtil;
import org.apache.log4j.Logger;


/**
 * Główna kalsa rozwiązująca sudoku.
 */
public class SudokuSolver {
    private static Logger LOGGER = org.apache.log4j.Logger.getLogger(SudokuSolver.class);

    /**
     * Rozwiązauje jedno sudoku.
     */
    public void solveSudoku() {
        ConfigUtil.init("sudoku-solver");
        Sudoku sudoku = SudokuParser.parseSudokuFromFile(ConfigUtil.getString(ConfigEnum.SUDOKU_FILE.getValue()));
        LOGGER.info(sudoku.cost());
    }
}
