package com.github.qhss.listeners;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.ButtonClickEvent;
import org.javacord.api.listener.interaction.ButtonClickListener;

import com.github.qhss.Blackjack;
import com.github.qhss.Main;

public class ButtonListener implements ButtonClickListener {

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        HashMap<String, Blackjack> games = Main.getGame();
        String username = event.getButtonInteraction().getUser().getDiscriminatedName();

        System.out.println(event.getButtonInteraction().getCustomId());
        if (games.containsKey(username)) {
            Blackjack bj = games.get(username);
            switch (event.getButtonInteraction().getCustomId()) {
                case "stand": {
                    String message = "";
                    bj.stand();
                    bj.dealerPlay();

                    if (bj.checkWinner() == 1) {
                        message = "You won!";
                    }
                    else if (bj.checkWinner() == 0) {
                        message = "You lost!";
                    }
                    else {
                        message = "You tied!";
                    }
                    try {
                        Main.appendImages(bj, true);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (games.containsKey(event.getButtonInteraction().getUser().getDiscriminatedName())) {
                        event.getButtonInteraction().getMessage().delete();
                        EmbedBuilder embed =
                                    new EmbedBuilder()
                                            .setTitle("Blackjack")
                                            .setDescription(message)
                                            .addField("Dealer's Cards: ", String.valueOf(bj.getScore(bj.getDealer())))
                                            .addField("Your Cards: ", String.valueOf(bj.getScore(bj.getPlayer())))
                                            .setColor(Color.CYAN)
                                            .setFooter(
                                                    "Your bet: $" + games.get(username).getBetAmount())
                                            .setImage(new File("src/main/resources/assets/combined.png"))
                                            .setThumbnail(
                                                    new File(
                                                            Main.getClassLoader()
                                                                    .getResource("assets/profile.png")
                                                                    .getFile()));
                            new MessageBuilder()
                                .setEmbed(embed)
                                .send(event.getButtonInteraction().getChannel().get());
                            Main.getGame().remove(username);
                    }
                    break;
                }
                case "hit": {
                    if (!bj.hit()) {
                        try {
                            Main.appendImages(bj, true);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        EmbedBuilder embed =
                                    new EmbedBuilder()
                                            .setTitle("Blackjack")
                                            .setDescription("You hit and lost!")
                                            .addField("Dealer's Cards: ", String.valueOf(bj.getScore(bj.getDealer())))
                                            .addField("Your Cards: ", String.valueOf(bj.getScore(bj.getPlayer())))
                                            .setColor(Color.CYAN)
                                            .setFooter(
                                                    "Your bet: $" + games.get(username).getBetAmount())
                                            .setImage(new File("src/main/resources/assets/combined.png"))
                                            .setThumbnail(
                                                    new File(
                                                            Main.getClassLoader()
                                                                    .getResource("assets/profile.png")
                                                                    .getFile()));
                        new MessageBuilder()
                                .setEmbed(embed)
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
                        event.getButtonInteraction().getMessage().delete();
                        EmbedBuilder embed =
                                    new EmbedBuilder()
                                            .setTitle("Blackjack")
                                            .setDescription("You hit!")
                                            .addField("Dealer's Cards: ", "VALUE?!?")
                                            .addField("Your Cards: ", String.valueOf(bj.getScore(bj.getPlayer())))
                                            .setColor(Color.CYAN)
                                            .setFooter(
                                                    "Your bet: $" + games.get(username).getBetAmount())
                                            .setImage(new File("src/main/resources/assets/combined.png"))
                                            .setThumbnail(
                                                    new File(
                                                            Main.getClassLoader()
                                                                    .getResource("assets/profile.png")
                                                                    .getFile()));
                            new MessageBuilder()
                                .setEmbed(embed)
                                .addComponents(
                                        ActionRow.of(
                                                Button.success("hit", "Hit", "👏", false),
                                                Button.primary("stand", "Stand", "🧍", false),
                                                Button.danger("double-down", "Double Down", "🤑", false),
                                                Button.secondary("exit", "Exit", "🚪", false)
                                        )
                                )
                                .send(event.getButtonInteraction().getChannel().get());
                    }
                    break;
                    
                }

                /*
                 * Only 2 cards: draw 1 card, stand
                 */
                case "double-down": {
                    if (!bj.doubleDown()) {
                        event.getButtonInteraction().createImmediateResponder().setContent("You've already picked up! Not allowed to do that!");
                        break;
                    }
                    if (games.containsKey(event.getButtonInteraction().getUser().getDiscriminatedName())) {
                        event.getButtonInteraction().getMessage().delete();
                        bj.dealerPlay();
                        
                        try {
                            Main.appendImages(bj, true);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        String message = "";
                        if (bj.checkWinner() == 1) {
                            message = "You won!";
                        }
                        else if (bj.checkWinner() == 0) {
                            message = "You lost!";
                        }
                        else {
                            message = "You tied!";
                        }
                        EmbedBuilder embed =
                                    new EmbedBuilder()
                                            .setTitle("Blackjack")
                                            .setDescription("You doubled-down" + " and " + message)
                                            .addField("Dealer's Cards: ", String.valueOf(bj.getScore(bj.getDealer())))
                                            .addField("Your Cards: ", String.valueOf(bj.getScore(bj.getPlayer())))
                                            .setColor(Color.CYAN)
                                            .setFooter(
                                                    "Your bet: $" + games.get(username).getBetAmount())
                                            .setImage(new File("src/main/resources/assets/combined.png"))
                                            .setThumbnail(
                                                    new File(
                                                            Main.getClassLoader()
                                                                    .getResource("assets/profile.png")
                                                                    .getFile()));
                            new MessageBuilder()
                                .setEmbed(embed)
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