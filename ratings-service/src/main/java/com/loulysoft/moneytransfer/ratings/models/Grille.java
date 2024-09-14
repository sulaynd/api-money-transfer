package com.loulysoft.moneytransfer.ratings.models;

public record Grille(Long code, String description, Type type) {

    public enum Type {
        SIMPLE,
        GRID,
        PERCENTAGE;
    }
}
