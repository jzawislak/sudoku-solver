package com.sudoku.solver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Reprezentuje obiekt sudoku.
 */
public class Sudoku {
    public static final int SUDOKU_SIZE = 9;
    public static final int SUDOKU_SQUARE_SIZE = 3;
    public static final int SUDOKU_EMPTY_FIELD = 0;
    /**
     * 2d tablica reprezentująca pola sudoku.
     */
    int[][] fieldsArray;

    public int[][] getFieldsArray() {
        return fieldsArray;
    }

    public void setFieldsArray(int[][] fieldsArray) {
        this.fieldsArray = fieldsArray;
    }

    /**
     * Funkcja kosztu. Sumuje powtórzenia w wierszach, kolumnach i kratkach.
     *
     * @return wartśc funkcji kosztu dla obecengo układu
     */
    public int cost() {
        int result = 0;
        Set<Integer> duplicatesRows = new HashSet<>();
        Set<Integer> duplicatesCols = new HashSet<>();
        Map<String, Set<Integer>> duplicatesSquares = new HashMap<>();

        for (int i = 0; i < SUDOKU_SIZE; i++) {
            duplicatesRows.clear();
            duplicatesCols.clear();
            for (int j = 0; j < SUDOKU_SIZE; j++) {
                duplicatesRows.add(fieldsArray[i][j]);
                duplicatesCols.add(fieldsArray[j][i]);

                String squareKey = String.valueOf(i / SUDOKU_SQUARE_SIZE) + String.valueOf(j / SUDOKU_SQUARE_SIZE);
                Set<Integer> set = duplicatesSquares.get(squareKey);
                if (set == null) {
                    set = new HashSet<>();
                    duplicatesSquares.put(squareKey, set);
                }
                set.add(fieldsArray[i][j]);
            }
            result += SUDOKU_SIZE - duplicatesRows.size();
            result += SUDOKU_SIZE - duplicatesCols.size();
        }

        for (Set<Integer> duplicates : duplicatesSquares.values()) {
            result += SUDOKU_SIZE - duplicates.size();
        }

        return result;
    }

    /**
     * Inicjalizuje niewypełnione pola reprezentowane przez {@link Sudoku#SUDOKU_EMPTY_FIELD}
     * tak aby w całym sudoku każda wartość występowała poprawną liczbę razy.
     */
    public void init() {
        //TODO dokończyć
    }

    /**
     * Zwraca sąsiada, który jest wynikiem zamiany dwóch losowych pól.
     *
     * @return losow sąsiad
     */
    public Sudoku getNeighbour() {
        //TODO dokończyć
        return new Sudoku();
    }

    /***
     * Zwraca sudoku jako string.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < fieldsArray[0].length; i++) {
            for (int j = 0; j < fieldsArray.length; j++) {
                builder.append(fieldsArray[i][j] + " ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
