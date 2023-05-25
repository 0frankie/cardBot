package com.github.qhss;
import java.util.*;


public class Blackjack implements CardGame{
   Player player1, player2;


   public Blackjack(Player one) {
       player1 = new Player();
       player2 = one;
   }


   @Override
   public void play() {
       deck.getTop();
   }


   @Override
   public String turn() {
       if (player1.isTurn()) {
           return player1.getUsername();
       }
       return player2.getUsername();
   }


   @Override
   public boolean checkWinner() {
      
   }
   // A + (values) > 21
   // A = 1
   private int getScore(Player p) {
       int score = 0;
       ArrayList<Card> playerHand = p.getHand();
       for (Card c: playerHand) {
           score += c.getValue();
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
