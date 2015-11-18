package com.sudoku.solver;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Reprezentuje obiekt sudoku.
 */
public class Sudoku {
    public static final int SUDOKU_SIZE = 9;
    public static final int SUDOKU_SQUARE_SIZE = 3;
    public static final int SUDOKU_EMPTY_FIELD = 0;
    private static Logger LOGGER = Logger.getLogger(Sudoku.class);
    /**
     * 2d tablica reprezentująca pola sudoku.
     */
    int[][] fieldsArray = new int[SUDOKU_SIZE][SUDOKU_SIZE];
    /**
     * Zbiór reprezentujący puste pola sudoku w oryginalnym, nieuzupełnionym sudoku.
     */
    List<Integer> emptyfieldsSet = new ArrayList<>();
    /**
     * Pomocniczy obiekt do losowania sąsiadów.
     */
    private Random random = new Random(System.currentTimeMillis());

    public int[][] getFieldsArray() {
        return fieldsArray;
    }

    public void setFieldsArrayAndCopy(int[][] fieldsArray) {
        this.fieldsArray = fieldsArray;
        //Zapisanie listy pustych pól.
        fieldsArray = new int[Sudoku.SUDOKU_SIZE][Sudoku.SUDOKU_SIZE];
        for (int i = 0; i < Sudoku.SUDOKU_SIZE; ++i) {
            for (int j = 0; j < Sudoku.SUDOKU_SIZE; ++j) {
                if (fieldsArray[i][j] == SUDOKU_EMPTY_FIELD) {
                    emptyfieldsSet.add(i * 10 + j);
                }
            }
        }
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
        Map<Integer, Integer> numberMap = new HashMap<>();
        //policzenie liczby występujacych cyfr
        for (int i = 0; i < Sudoku.SUDOKU_SIZE; ++i) {
            for (int j = 0; j < Sudoku.SUDOKU_SIZE; ++j) {
                if (numberMap.get(fieldsArray[i][j]) == null) {
                    numberMap.put(fieldsArray[i][j], 0);
                }
                numberMap.put(fieldsArray[i][j], numberMap.get(fieldsArray[i][j]) + 1);
            }
        }
        //zastąpienie wszystkich zer
        int k = 1;
        for (int i = 0; i < Sudoku.SUDOKU_SIZE; ++i) {
            for (int j = 0; j < Sudoku.SUDOKU_SIZE; ++j) {
                if (fieldsArray[i][j] == SUDOKU_EMPTY_FIELD) {
                    for (; k <= 9; k++) {
                        if (numberMap.get(k) < SUDOKU_SIZE) {
                            fieldsArray[i][j] = k;
                            numberMap.put(k, numberMap.get(k) + 1);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Zwraca sąsiada, który jest wynikiem zamiany dwóch losowych pól.
     *
     * @return losowy sąsiad
     */
    //TODO być może da się zopytamalizować, metoda wywoływana jest dosyć często
    public Sudoku getNeighbour() {
        int first = random.nextInt(emptyfieldsSet.size());
        int second = random.nextInt(emptyfieldsSet.size());

        int row1 = Integer.valueOf(emptyfieldsSet.get(first) / 10);
        int row2 = Integer.valueOf(emptyfieldsSet.get(second) / 10);
        int col1 = Integer.valueOf(emptyfieldsSet.get(first) % 10);
        int col2 = Integer.valueOf(emptyfieldsSet.get(second) % 10);

        Sudoku result = this.makeCopy();
        int old = result.fieldsArray[row1][col1];
        result.fieldsArray[row1][col1] = result.fieldsArray[row2][col2];
        result.fieldsArray[row2][col2] = old;

        return result;
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

    public Sudoku makeCopy() {
        Sudoku sudoku = new Sudoku();
        //zbiór oryginalny nie ulega potem zmianie
        sudoku.emptyfieldsSet = emptyfieldsSet;

        //kopia pól
        for (int i = 0; i < Sudoku.SUDOKU_SIZE; ++i) {
            for (int j = 0; j < Sudoku.SUDOKU_SIZE; ++j) {
                sudoku.fieldsArray[i][j] = this.fieldsArray[i][j];
            }
        }
        return sudoku;
    }
}
