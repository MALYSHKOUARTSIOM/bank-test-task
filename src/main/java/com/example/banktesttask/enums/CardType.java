package com.example.banktesttask.enums;

public enum CardType {
    Classic(0),
    Gold(1),
    Platinum(2);

    private final int statusValue;

    CardType(int statusValue) {
        this.statusValue = statusValue;
    }

    public int getStatusValue() {
        return statusValue;
    }
}
