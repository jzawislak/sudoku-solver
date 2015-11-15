package com.sudoku;

import com.sudoku.solver.SudokuSolver;

public class Main {
    public static void main(String[] args) {
        SudokuSolver sudokuSolver = new SudokuSolver();
        sudokuSolver.solveSudoku();
    }
}