package com.sudoku;

import com.sudoku.core.config.ConfigUtil;
import com.sudoku.core.config.enums.ConfigEnum;
import com.sudoku.core.config.enums.ConfigMode;
import com.sudoku.solver.SudokuSolver;

public class Main {
    public static void main(String[] args) {
        ConfigUtil.init("sudoku-solver");
        SudokuSolver sudokuSolver = new SudokuSolver();
        ConfigMode configMode = ConfigMode.valueOf(ConfigUtil.getString(ConfigEnum.TRYB.getValue()));
        if (configMode == ConfigMode.SINGLE) {
            sudokuSolver.solveSingleSudoku();
        } else if (configMode == ConfigMode.FULL_TEST) {
            sudokuSolver.performFullTest();
        }
    }
}