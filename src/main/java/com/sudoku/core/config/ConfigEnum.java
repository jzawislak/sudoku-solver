package com.sudoku.core.config;

/**
 * Reprezentuje możliwe pola w pliku konfiguracyjnym.
 */
public enum ConfigEnum {
    SUDOKU_FILE("plik_sudoku"),
    ITERACJE_ZWEN("iteracje_zwen"),
    ITERACJE_WEWN("iteracje_wewn"),
    ALFA("alfa"),
    TAU("tau");

    private final String value;

    ConfigEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
