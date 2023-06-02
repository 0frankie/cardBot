package com.github.qhss.listeners;

import com.github.qhss.Blackjack;
import com.github.qhss.DefaultEmbeds;
import com.github.qhss.JsonUtils;
import com.github.qhss.Main;
import com.github.qhss.Player;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.event.interaction.ButtonClickEvent;
import org.javacord.api.interaction.ButtonInteraction;
import org.javacord.api.listener.interaction.ButtonClickListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Function;

public class ButtonListener implements ButtonClickListener {

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        HashMap<String, Blackjack> games = Main.userGame;
        ButtonInteraction interaction = event.getButtonInteraction();
        String username = interaction.getUser().getDiscriminatedName();
        TextChannel channel = interaction.getChannel().get();
        Function<String, Void> respond =
                msg -> {
                    interaction
                            .createImmediateResponder()
                            .setContent(msg)
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                    return null;
                };

        if (games.containsKey(username) && Main.channelUser.get(channel).equals(username)) {
            Blackjack bj = games.get(username);
            Player player = bj.getPlayer();

            switch (interaction.getCustomId()) {
                case "stand" -> {
                    String message;
                    bj.stand();
                    bj.dealerPlay();
                    int bet = bj.getBetAmount();

                    if (bj.getScore(bj.getDealer()) == 21 && bj.getDealerCards().length == 2) {
                        message = "You lost! Dealer got Blackjack!";
                        bet = (int) (-2.5 * bet);
                    } else {
                        message = bj.checkWinner();
                        bet =
                                message.contains("won")
                                        ? bj.getBetAmount()
                                        : message.contains("lost") ? -bj.getBetAmount() : 0;
                    }
                    try {
                        Main.appendImages(bj, true, username);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    player.setMoney(player.getMoney() + bet);
                    JsonUtils.changeMoney(username, player);

                    if (games.containsKey(username)) {
                        interaction.getMessage().delete();
                        new MessageBuilder()
                                .setEmbed(
                                        DefaultEmbeds.finalEmbed(
                                                message, username, bj, Math.abs(bet)))
                                .send(channel);
                        Main.removeFromMaps(username, channel);
                    }
                }
                case "hit" -> {
                    interaction.getMessage().delete();
                    if (!bj.hit()) {
                        try {
                            Main.appendImages(bj, true, username);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        player.setMoney(player.getMoney() - bj.getBetAmount());
                        JsonUtils.changeMoney(username, player);

                        new MessageBuilder()
                                .setEmbed(
                                        DefaultEmbeds.finalEmbed(
                                                "You hit and lost!",
                                                username,
                                                bj,
                                                bj.getBetAmount()))
                                .send(channel);
                        Main.removeFromMaps(username, channel);
                        break;
                    }
                    if (bj.getPlayer().getHand().size() == 5) {
                        try {
                            Main.appendImages(bj, true, username);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        player.setMoney(player.getMoney() + bj.getBetAmount());
                        JsonUtils.changeMoney(username, player);

                        new MessageBuilder()
                                .setEmbed(
                                        DefaultEmbeds.finalEmbed(
                                                "You hit and got 5 in a row, so you won!",
                                                username,
                                                bj,
                                                bj.getBetAmount()))
                                .send(channel);
                        Main.removeFromMaps(username, channel);
                        break;
                    }

                    if (games.containsKey(username)) {
                        if (bj.getScore(bj.getPlayer()) == 21) {
                            try {
                                Main.appendImages(bj, true, username);
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                            player.setMoney(player.getMoney() + bj.getBetAmount());
                            JsonUtils.changeMoney(username, player);

                            Main.removeFromMaps(username, channel);
                            new MessageBuilder()
                                    .setEmbed(
                                            DefaultEmbeds.finalEmbed(
                                                    "You hit and won by getting 21!",
                                                    username,
                                                    bj,
                                                    bj.getBetAmount()))
                                    .send(channel);
                            break;
                        }
                        DefaultEmbeds.defaultMessage("You hit!", interaction.getUser(), bj)
                                .send(channel);
                    }
                }

                    /*
                     * Only 2 cards: draw 1 card, stand
                     */
                case "double-down" -> {
                    if (bj.getPlayer().getMoney() < 2 * bj.getBetAmount()) {
                        respond.apply("You can't double down with not enough money!");
                        break;
                    }
                    if (!bj.doubleDown()) {
                        respond.apply("You've already picked up! Not allowed to do that!");
                        break;
                    }
                    if (games.containsKey(username)) {
                        interaction.getMessage().delete();
                        try {
                            Main.appendImages(bj, true, username);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if (bj.getScore(bj.getDealer()) == 21) {
                            player.setMoney(player.getMoney() - 5 * bj.getBetAmount());
                            JsonUtils.changeMoney(username, player);
                            new MessageBuilder()
                                    .setEmbed(
                                            DefaultEmbeds.finalEmbed(
                                                    "You doubled-down and lost to Blackjack!",
                                                    username,
                                                    bj,
                                                    5 * bj.getBetAmount()))
                                    .send(channel);

                            Main.removeFromMaps(username, channel);
                            break;
                        }

                        if (bj.checkWinner().contains("lost")) {
                            player.setMoney(player.getMoney() - 2 * bj.getBetAmount());
                            JsonUtils.changeMoney(username, player);
                            new MessageBuilder()
                                    .setEmbed(
                                            DefaultEmbeds.finalEmbed(
                                                    "You doubled-down and lost!",
                                                    username,
                                                    bj,
                                                    2 * bj.getBetAmount()))
                                    .send(channel);

                            Main.removeFromMaps(username, channel);
                            break;
                        }
                        bj.dealerPlay();

                        try {
                            Main.appendImages(bj, true, username);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        String message = bj.checkWinner();
                        int bet =
                                message.contains("won")
                                        ? bj.getBetAmount()
                                        : message.contains("lost") ? -bj.getBetAmount() : 0;

                        player.setMoney(player.getMoney() + 2 * bet);
                        JsonUtils.changeMoney(username, player);

                        new MessageBuilder()
                                .setEmbed(
                                        DefaultEmbeds.finalEmbed(
                                                "You doubled-down" + " and " + message,
                                                username,
                                                bj,
                                                Math.abs(2 * bj.getBetAmount())))
                                .send(channel);

                        Main.removeFromMaps(username, channel);
                    }
                }
                case "exit" -> {
                    if (games.containsKey(username)) {
                        if (games.get(username).getDealerCards().length == 2
                                && games.get(username).getPlayerCards().length == 2) {
                            interaction.getMessage().delete();
                            respond.apply("Exited game");
                            Main.removeFromMaps(username, channel);
                            break;
                        }
                    }
                    respond.apply("You're either not the player or you already began playing!");
                }
            }
        } else {
            respond.apply("You're not playing this game! Start your own with /cb");
        }
    }
}
