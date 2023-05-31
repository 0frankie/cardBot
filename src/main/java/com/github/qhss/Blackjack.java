package com.github.qhss;
import java.util.*;

public class Blackjack implements CardGame{
  private Player dealer, player;
  private Deck deck;
  private int betAmount;

  public Blackjack(Player one, int betAmount) {
      dealer = new Player();
      player = one;
      deck = new Deck();
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

  public void setBetAmount(int betAmount) {
    this.betAmount = betAmount;
  }

  public void dealerPlay() {
      while (getScore(dealer) < 17){
        dealer.addToHand(deck.getTop());
      }
  }

  public void stand() {
       player.changeTurn(false);
  }

  public boolean hit() {
    player.addToHand(deck.getTop());
    if (getScore(player) > 21) {
        return false;
    }
    return true;
  }

  public boolean doubleDown() {
       if (player.getHand().size() == 2) {
           this.betAmount *= 2;
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
    if (score <= 21 && (score > getScore(dealer) || getScore(dealer) > 21)) {
        return 1;
    }
    else if (score <= 21 && score == getScore(dealer)) {
        return -1;
    }
    else {
        return 0;
    }
}

  public int getScore(Player p) {
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
 /*
  * Card.getSuit() == suit
  * Card.cardSymbol().name() == number
  */
   public String[] getPlayerCards() {
    String[] handPaths = new String[player.getHand().size()];
    for (int i = 0; i < handPaths.length; i++) {
            handPaths[i] = "src/main/resources/assets/PlayingCards/PNG-cards-1.3/" + player.getHand().get(i).suit() + "/" + player.getHand().get(i).cardSymbol().name().toLowerCase() + ".png";
    }
    return handPaths;
   }

   public String[] getDealerCards() {
    String[] handPaths = new String[dealer.getHand().size()];
    for (int i = 0; i < handPaths.length; i++) {
            handPaths[i] = "src/main/resources/assets/PlayingCards/PNG-cards-1.3/" + dealer.getHand().get(i).suit() + "/" + dealer.getHand().get(i).cardSymbol().name().toLowerCase() + ".png";
    }
    return handPaths;
   }    

   public String getPlayerName() {
        return player.getUsername();
   }

   public Player getPlayer() {
    return player;
   }
   
   public Player getDealer() {
    return dealer;
   }
}





