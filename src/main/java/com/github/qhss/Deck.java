package com.github.qhss;

import static com.github.qhss.CardSymbol.*;

import java.util.*;

public class Deck {

    private Card[] deck;
    private int counter = -1;

    public Deck() {
        createDeck();
        shuffle();
    }

    public void createDeck() {
        deck =
                new Card[] {
                    new Card(A, 's'), new Card(TWO, 's'), new Card(THREE, 's'), new Card(FOUR, 's'), new Card(FIVE, 's'), new Card(SIX, 's'), new Card(SEVEN, 's'), new Card(EIGHT, 's'), new Card(NINE, 's'), new Card(TEN, 's'), new Card(J, 's'), new Card(Q, 's'), new Card(K, 's'),
                    new Card(A, 'h'), new Card(TWO, 'h'), new Card(THREE, 'h'), new Card(FOUR, 'h'), new Card(FIVE, 'h'), new Card(SIX, 'h'), new Card(SEVEN, 'h'), new Card(EIGHT, 'h'), new Card(NINE, 'h'), new Card(TEN, 'h'), new Card(J, 'h'), new Card(Q, 'h'), new Card(K, 'h'),
                    new Card(A, 'c'), new Card(TWO, 'c'), new Card(THREE, 'c'), new Card(FOUR, 'c'), new Card(FIVE, 'c'), new Card(SIX, 'c'), new Card(SEVEN, 'c'), new Card(EIGHT, 'c'), new Card(NINE, 'c'), new Card(TEN, 'c'), new Card(J, 'c'), new Card(Q, 'c'), new Card(K, 'c'),
                    new Card(A, 'd'), new Card(TWO, 'd'), new Card(THREE, 'd'), new Card(FOUR, 'd'), new Card(FIVE, 'd'), new Card(SIX, 'd'), new Card(SEVEN, 'd'), new Card(EIGHT, 'd'), new Card(NINE, 'd'), new Card(TEN, 'd'), new Card(J, 'd'), new Card(Q, 'd'), new Card(K, 'd'),
                };
    }

    public void shuffle() {
        List<Card> deckList = Arrays.asList(deck);
        Collections.shuffle(deckList);
        deckList.toArray(deck);
    }

    public Card getTop() {
        counter = (counter + 1 >= deck.length) ? 0 : counter + 1;
        return deck[counter];
    }

}
