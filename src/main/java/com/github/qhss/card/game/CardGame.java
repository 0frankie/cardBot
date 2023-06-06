package com.github.qhss.card.game;

import com.github.qhss.card.Deck;

public interface CardGame {
    Deck deck = new Deck();

    void play();

    String turn();

    String checkWinner();
}
