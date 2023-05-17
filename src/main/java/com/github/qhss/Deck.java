package com.github.qhss;
import java.util.*;
import static com.github.qhss.Card.*;

public class Deck {

    private Card[] deck;
    private int counter = -1;

    public Deck() {
        createDeck();
        modifySuit();
        shuffle();
    }

    public void createDeck() {
        this.deck = new Card[] { A, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, J, Q, K, 
                                 A, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, J, Q, K, 
                                 A, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, J, Q, K, 
                                 A, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, J, Q, K };
    }

    public void shuffle() {
        List<Card> deckList = Arrays.asList(deck);
		Collections.shuffle(deckList);
		deckList.toArray(deck);
    }

    public Card getTop() {
        counter++;
        return deck[counter];
    }

    public void reset() {
        createDeck();
        shuffle();
        counter = -1;
    }

    public void modifySuit() {
        char[] suits = {'d', 'c', 'h', 's'};
        //int count = -1;
        for (int i = 0; i < deck.length; i++) {
            //if (i % 13 == 0) count++;
            //this.deck[i].setSuit(suits[count]);
            int suitIndex = i / 13;
            this.deck[i].setSuit(suits[suitIndex]);
            System.out.println(this.deck[i].getSuit());
        }
    }

    public Card[] getDeck() {
        return deck;
    }

    public static void main (String args[]) {
        Deck d = new Deck();

    }
}