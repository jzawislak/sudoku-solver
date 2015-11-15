package com.sudoku.core.config;

/**
 * Reprezentuje możliwe pola w pliku konfiguracyjnym.
 */
public enum ConfigEnum {
    SUDOKU_FILE("plik_sudoku");

    private final String value;

    ConfigEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
