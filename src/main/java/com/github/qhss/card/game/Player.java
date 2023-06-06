package com.github.qhss.card.game;

import java.util.*;

import com.github.qhss.card.Card;

public class Player {
    private transient boolean turn;
    private int money;
    private final transient ArrayList<Card> hand = new ArrayList<>();

    private String username;

    // dealer
    public Player() {
        turn = false;
        this.money = Integer.MAX_VALUE;
    }

    // normal player
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
