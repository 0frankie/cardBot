package com.github.qhss;
import java.util.*;

public class Blackjack implements CardGame{
  Player dealer, player;
  Deck deck;

  public Blackjack(Player one) {
      dealer = new Player();
      player = one;
      deck = new Deck();
  }

  @Override
  public void play() {
       player.addToHand(deck.getTop());
       dealer.addToHand(deck.getTop());
       player.addToHand(deck.getTop());
       dealer.addToHand(deck.getTop());
  }

  public void stand() {
       player.changeTurn(false);
  }

  public void hit() {
       if (player.isTurn()) {
           player.addToHand(deck.getTop());
           if (getScore(player) > 21) {
               player.changeTurn(false);
           }
       }      
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
   * -1 is tie
   */
  @Override
  public int checkWinner() {
    int score = getScore(player);
    if (score <= 21 && score > getScore(dealer)) {
        return 1;
    }
    else if (score <= 21 && score == getScore(dealer)) {
        return -1;
    }
    else {
        return 0;
    }
}

  private int getScore(Player p) {
      int score = 0;
      ArrayList<Card> playerHand = p.getHand();
      for (Card c: playerHand) {
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
}





