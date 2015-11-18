package com.sudoku.solver;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Klasa pomocnicza do obsługi obiektów typu {@link Sudoku}
 */
public class SudokuParser {
    private static Logger LOGGER = Logger.getLogger(SudokuParser.class);

    /**
     * Zwraca tablicę int reprezentujacą sudoku.
     *
     * @param filename plik w którym znajduje sie sudoku
     * @return twczytane sudoku
     */
    public static Sudoku parseSudokuFromFile(String filename) {
        Sudoku sudoku = new Sudoku();
        int[][] fieldsArray = new int[Sudoku.SUDOKU_SIZE][Sudoku.SUDOKU_SIZE];
        InputStream inputStream = readFileAsStream(filename);

        Scanner scanner = new Scanner(inputStream);
        for (int i = 0; i < Sudoku.SUDOKU_SIZE; ++i) {
            for (int j = 0; j < Sudoku.SUDOKU_SIZE; ++j) {
                if (scanner.hasNextInt()) {
                    fieldsArray[i][j] = scanner.nextInt();
                }
            }
        }
        sudoku.setFieldsArrayAndCopy(fieldsArray);
        LOGGER.info("Sudoku loaded \n" + sudoku);

        return sudoku;
    }

    /**
     * Otwiera pllik jako strumień danych. Najpierw szukany jest plik wlokalnm katalogu,
     * a następnie w katalogu "resources".
     *
     * @param fileName nazwa pliku
     */
    private static InputStream readFileAsStream(String fileName) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(fileName);
            if (inputStream.available() == 0) {
                throw new IOException();
            }
            LOGGER.info("Loading file " + fileName + "from user local directory.");
        } catch (IOException e) {
            ClassLoader classLoader = SudokuParser.class.getClassLoader();
            inputStream = classLoader.getResourceAsStream(fileName);
            LOGGER.info("Loading file " + fileName + " from resources.");
        }

        return inputStream;
    }
}
