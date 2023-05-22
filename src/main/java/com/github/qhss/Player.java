package com.github.qhss;
import java.util.*;

public class Player {
    private ArrayList<Card> hand = new ArrayList<>();
    private boolean turn;
    private int betAmount;
    private boolean doubleDown;
    private int money;

    public Player() {
        turn = false;
        this.money = Integer.MAX_VALUE;
    }

    public Player(int money) {
        turn = true;
        this.money = money;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public boolean isDoubleDown() {
        return doubleDown;
    }

    public boolean isTurn() {
        return turn;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }
}
