package com.github.qhss;
import java.util.*;

public class Blackjack implements CardGame{
    Player player1, player2;

    public Blackjack(Player one) {
        player1 = new Player();
        player2 = one;
    }

    public Blackjack(Player one, Player two) {
        player1 = one;
        player2 = two;
    }

    @Override
    public void play() {
        deck.getTop();
        
    }

    @Override
    public String turn() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'turn'");
    }

    @Override
    public boolean checkWinner() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkWinner'");
    }
    // A + (values) > 21
    // A = 1
    private int getScore(Player p) {
        int score = 0;
        ArrayList<Card> playerHand = p.getHand();
        return score;
    }
}