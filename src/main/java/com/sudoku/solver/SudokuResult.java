package com.sudoku.solver;

/**
 * Klasa pomocnicza do zwracania informacji o przebiegu procesu rozwiązywania sudoku.
 */
class SudokuResult {
    final boolean isSuccess;
    final int outerIterations;
    final int innerIterations;
    final int allIterations;
    final Sudoku sudoku;

    SudokuResult(boolean isSuccess, int outerIterations, int innerIterations, int allIterations,
                 Sudoku sudoku) {
        this.innerIterations = innerIterations;
        this.outerIterations = outerIterations;
        this.isSuccess = isSuccess;
        this.allIterations = allIterations;
        this.sudoku = sudoku;
    }
}
