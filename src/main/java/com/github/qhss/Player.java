package com.github.qhss;
import java.util.*;

public class Player {
    private transient boolean doubleDown;
    private transient boolean turn;
    private int money;
    private transient ArrayList<Card> hand = new ArrayList<>();

    private String username;

    public Player() {
        turn = false;
        this.money = Integer.MAX_VALUE;
    }

    public Player(int money, String username) {
        turn = true;
        this.money = money;
        this.username = username;
    }

    public int getMoney() {
        return money;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isDoubleDown() {
        return doubleDown;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void addToHand(Card c) {
        hand.add(c);
    }

    public void changeTurn(boolean change) {
        turn = change;
    }
}
