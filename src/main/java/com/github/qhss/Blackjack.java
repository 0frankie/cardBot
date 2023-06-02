package com.github.qhss;

import java.util.*;

public class Blackjack implements CardGame {
    private final Player dealer, player;
    private final int betAmount;

    public Blackjack(Player one, int betAmount) {
        dealer = new Player();
        player = one;
        this.betAmount = betAmount;
        play();
    }

    @Override
    public void play() {
        player.addToHand(deck.getTop());
        dealer.addToHand(deck.getTop());
        player.addToHand(deck.getTop());
        dealer.addToHand(deck.getTop());
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void dealerPlay() {
        while (getScore(dealer) < 17) {
            dealer.addToHand(deck.getTop());
        }
    }

    public void stand() {
        player.changeTurn(false);
    }

    public boolean hit() {
        player.addToHand(deck.getTop());
        return getScore(player) <= 21;
    }

    public boolean doubleDown() {
        if (player.getHand().size() == 2) {
            player.addToHand(deck.getTop());
            player.changeTurn(false);
            return true;
        }
        return false;
    }

    @Override
    public String turn() {
        if (player.isTurn()) {
            return player.getUsername();
        }
        return dealer.getUsername();
    }

    /*
     * 1 is true
     * 0 is false
     * -1 is tied
     */
    @Override
    public String checkWinner() {
        int score = getScore(player);
        if (score <= 21 && (score > getScore(dealer) || getScore(dealer) > 21)) {
            return "you won";
        } else if (score <= 21 && score == getScore(dealer)) {
            return "you tied";
        } else {
            return "you lost";
        }
    }

    public int getScore(Player p) {
        int score = 0;
        ArrayList<Card> playerHand = p.getHand();
        for (Card c : playerHand) {
            score += c.cardSymbol().getValue();
        }
        int counter = 0;
        while (score > 21 && counter < playerHand.size()) {
            if (playerHand.get(counter).cardSymbol().name().equals("A")) {
                score -= 10;
            }
            counter++;
        }
        return score;
    }
    /*
     * Card.getSuit() == suit
     * Card.cardSymbol().name() == number
     */
    public String[] getPlayerCards() {
        return getStrings(player);
    }

    public String[] getDealerCards() {
        return getStrings(dealer);
    }

    private String[] getStrings(Player plr) {
        String[] handPaths = new String[plr.getHand().size()];
        for (int i = 0; i < handPaths.length; i++) {
            handPaths[i] =
                    "src/main/resources/assets/PlayingCards/PNG-cards-1.3/"
                            + plr.getHand().get(i).suit()
                            + "/"
                            + plr.getHand().get(i).cardSymbol().name().toLowerCase()
                            + ".png";
        }
        return handPaths;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getDealer() {
        return dealer;
    }
}
