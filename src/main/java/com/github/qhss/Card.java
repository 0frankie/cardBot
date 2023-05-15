package com.github.qhss;

public enum Card {
    A(11, "\uD83C\uDCC1"),
    TWO(2, "🃂"),
    THREE(3, "🃃"),
    FOUR(4, "🃄"),
    FIVE(5, "\uD83C\uDCC5"),
    SIX(6, "\uD83C\uDCC6"),
    SEVEN(7, "\uD83C\uDCC7"),
    EIGHT(8, "\uD83C\uDCC8"),
    NINE(9, "\uD83C\uDCC9"),
    TEN(10, "\uD83C\uDCCA"),
    J(10, "\uD83C\uDCCB"),
    Q(10, "\uD83C\uDCCD"),
    K(10, "\uD83C\uDCCE");

    private final int value;
    private final String icon;

    Card(int value, String icon) {
        this.value = value;
        this.icon = icon;
    }


    public int getValue() {
        return value;
    }

    public String getIcon() {
        return icon;
    }
}
