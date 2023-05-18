package com.github.qhss;

public class Card {
    private final CardSymbol cardSymbol;
    private final char suit;

    public Card(CardSymbol cardSymbol, char suit) {
        this.cardSymbol = cardSymbol;
        this.suit = suit;
    }

    public CardSymbol getCardSymbol() {
        return cardSymbol;
    }

    public char getSuit() {
        return suit;
    }
}
