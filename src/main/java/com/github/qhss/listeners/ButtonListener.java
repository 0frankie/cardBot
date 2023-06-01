package com.github.qhss.listeners;

import java.io.IOException;
import java.util.HashMap;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.event.interaction.ButtonClickEvent;
import org.javacord.api.listener.interaction.ButtonClickListener;

import com.github.qhss.Blackjack;
import com.github.qhss.DefaultEmbeds;
import com.github.qhss.JsonUtils;
import com.github.qhss.Main;
import com.github.qhss.Player;

public class ButtonListener implements ButtonClickListener {

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        HashMap<String, Blackjack> games = Main.getGame();
        String username = event.getButtonInteraction().getUser().getDiscriminatedName();

        if (games.containsKey(username)) {
            Blackjack bj = games.get(username);
            switch (event.getButtonInteraction().getCustomId()) {
                case "stand": {
                    String message = "";
                    bj.stand();
                    bj.dealerPlay();
                    int bet = bj.getBetAmount();

                    if (bj.getScore(bj.getDealer()) == 21) {
                        message = "You lost! Dealer got Blackjack!";
                        bet = (int) (2.5 * bet);
                    }
                    else if (bj.checkWinner() == 1) {
                        message = "You won!";
                    }
                    else if (bj.checkWinner() == 0) {
                        message = "You lost!";
                        bet = -bet;
                    }
                    else {
                        message = "You tied!";
                        bet = 0;
                    }
                    try {
                        Main.appendImages(bj, true);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Player player = bj.getPlayer();
                    player.setMoney(player.getMoney() + bet);
                    JsonUtils.changeMoney(username, player);

                    if (games.containsKey(event.getButtonInteraction().getUser().getDiscriminatedName())) {
                        event.getButtonInteraction().getMessage().delete();
                            new MessageBuilder()
                                .setEmbed(DefaultEmbeds.finalEmbed(message, username, bj))
                                .send(event.getButtonInteraction().getChannel().get());
                            Main.getGame().remove(username);
                    }
                    break;
                }
                case "hit": {
                    event.getButtonInteraction().getMessage().delete();
                    if (!bj.hit()) {
                        try {
                            Main.appendImages(bj, true);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        Player player = bj.getPlayer();
                        player.setMoney(player.getMoney() - bj.getBetAmount());
                        JsonUtils.changeMoney(username, player);
                        
                        new MessageBuilder()
                                .setEmbed(DefaultEmbeds.finalEmbed("You hit and lost!", username, bj))
                                .send(event.getButtonInteraction().getChannel().get());                                
                        Main.getGame().remove(username);
                        break;
                    }
                    if (bj.getPlayer().getHand().size() == 5) {
                        try {
                            Main.appendImages(bj, true);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        Player player = bj.getPlayer();
                        player.setMoney(player.getMoney() + bj.getBetAmount());
                        JsonUtils.changeMoney(username, player);

                        new MessageBuilder()
                                .setEmbed(DefaultEmbeds.finalEmbed("You hit and got 5 in a row, so you won!", username, bj))
                                .send(event.getButtonInteraction().getChannel().get());                                
                        Main.getGame().remove(username);
                        break;
                    }
                    try {
                        Main.appendImages(bj, false);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (games.containsKey(event.getButtonInteraction().getUser().getDiscriminatedName())) {
                        if (bj.getScore(bj.getPlayer()) == 21) {
                            Player player = bj.getPlayer();
                            player.setMoney(player.getMoney() + bj.getBetAmount());
                            JsonUtils.changeMoney(username, player);

                            Main.getGame().remove(username);
                            new MessageBuilder()
                                .setEmbed(DefaultEmbeds.finalEmbed("You hit and got 21!", username, bj))
                                .send(event.getButtonInteraction().getChannel().get());
                                break;
                        }
                            DefaultEmbeds.defaultMessage("You hit!", event.getButtonInteraction().getUser(), bj)
                                .send(event.getButtonInteraction().getChannel().get());
                    }
                    break;
                    
                }

                /*
                 * Only 2 cards: draw 1 card, stand
                 */
                case "double-down": {
                    if (!bj.doubleDown()) {
                        event.getButtonInteraction().createImmediateResponder().setContent("You've already picked up! Not allowed to do that!").setFlags(MessageFlag.EPHEMERAL).respond();
                        break;
                    }
                    if (games.containsKey(event.getButtonInteraction().getUser().getDiscriminatedName())) {
                        event.getButtonInteraction().getMessage().delete();

                        if (bj.getScore(bj.getDealer()) == 21) {
                            Player player = bj.getPlayer();
                            player.setMoney(player.getMoney() - 5 * bj.getBetAmount());
                            JsonUtils.changeMoney(username, player);
                            new MessageBuilder()
                                .setEmbed(DefaultEmbeds.finalEmbed("You doubled-down and lost to Blackjack!", username, bj))
                                .send(event.getButtonInteraction().getChannel().get());

                            Main.getGame().remove(username);
                        }
                        bj.dealerPlay();
                        
                        try {
                            Main.appendImages(bj, true);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        int bet = bj.getBetAmount();

                        String message = "";
                        if (bj.checkWinner() == 1) {
                            message = "you won!";
                        }
                        else if (bj.checkWinner() == 0) {
                            message = "you lost!";
                            bet = -bet;
                        }
                        else {
                            message = "you tied!";
                            bet = 0;
                        }

                        Player player = bj.getPlayer();
                        player.setMoney(player.getMoney() + 2 * bet);
                        JsonUtils.changeMoney(username, player);
                        new MessageBuilder()
                            .setEmbed(DefaultEmbeds.finalEmbed("You doubled-down" + " and " + message, username, bj))
                            .send(event.getButtonInteraction().getChannel().get());

                            Main.getGame().remove(username);
                    }
                    break;
                }
                case "exit": {
                    if (games.containsKey(username)) {
                        if (games.get(username).getDealerCards().length == 2 && games.get(username).getPlayerCards().length == 2) {
                            event.getButtonInteraction().getMessage().delete();
                            event.getButtonInteraction().createImmediateResponder().setContent("Exited game").setFlags(MessageFlag.EPHEMERAL).respond();
                            Main.getGame().remove(username);
                            break;
                        }
                    }
                    event.getButtonInteraction().createImmediateResponder().setContent("You're either not the player or you already began playing!").setFlags(MessageFlag.EPHEMERAL).respond();
                    break;
                }
            }    
        }
    }

}