package com.github.qhss.card;

import java.time.LocalDate;
import java.util.*;

public class Player {
    private transient boolean turn;
    private final transient ArrayList<Card> hand = new ArrayList<>();
    
    private int money;
    private transient LocalDate time; // transient rn because it errors out otherwise

    private String username;

    // dealer
    public Player() {
        turn = false;
        this.money = Integer.MAX_VALUE;
    }

    // normal player
    public Player(int money, String username) {
        turn = true;
        time = LocalDate.now();
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

    public boolean isDayAfter(LocalDate time) {
        System.out.println(this.time.compareTo(time));
        return true;
    }
}
