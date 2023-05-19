package com.github.qhss;

public interface CardGame {
    Deck deck = new Deck();

    void play();

    String turn();

    boolean checkWinner();
}
