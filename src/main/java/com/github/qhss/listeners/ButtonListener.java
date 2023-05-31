package com.github.qhss.listeners;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
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
        HashMap<String, CompletableFuture<Message>> messages = Main.getMsg();
        
        if (games.containsKey(username)) {
            Blackjack bj = games.get(username);
            switch (event.getButtonInteraction().getCustomId()) {
                case "stand": {
                    bj.stand();
                }
                case "hit": {
                    bj.hit();
                    EmbedBuilder embed =
                                        new EmbedBuilder()
                                                .setTitle("Blackjack")
                                                .setDescription("Play against the dealer!")
                                                .addField("Dealer's Cards: ", "VALUE")
                                                .addField("Your Cards: ", "VALUE");
                                                // .setColor(Color.CYAN)
                                                // .setImage(
                                                //         new File(
                                                //                 Main.getClassLoader()
                                                //                         .getResource(
                                                //                                 "assets/PlayingCards/PNG-cards-1.3/" + c4.suit() + "/" + c4.cardSymbol().name().toLowerCase() + ".png")
                                                //                         .getFile()))
                                                // .setFooter(
                                                //         "Footer",
                                                //         new File(
                                                //                 Main.getClassLoader()
                                                //                         .getResource(
                                                //                                 "assets/PlayingCards/PNG-cards-1.3/" + c.suit() + "/" + c.cardSymbol().name().toLowerCase() + ".png")
                                                //                         .getFile()))
                                                // .setThumbnail(
                                                //         new File(
                                                //                 Main.getClassLoader()
                                                //                         .getResource("assets/profile.png")
                                                //                         .getFile()));
                    if (messages.containsKey(username)) {
                        try {
                            messages.get(username).get().delete();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Main.removeMsg(username);
                    }
                }
                case "double-down": {
                    bj.doubleDown();
                    event.getButtonInteraction().getMessage().edit();
                    EmbedBuilder embed = new EmbedBuilder()
                                                .setTitle("Blackjack")
                                                .setDescription("Play against the dealer!")
                                                .addField("Dealer's Cards: ", "VALUE")
                                                .addField("Your Cards: ", "VALUE")
                                                .setColor(Color.CYAN);
                                                // .setImage(
                                                //         new File(
                                                //                 Main.getClassLoader()
                                                //                         .getResource(
                                                //                                 "assets/PlayingCards/PNG-cards-1.3/" + c4.suit() + "/" + c4.cardSymbol().name().toLowerCase() + ".png")
                                                //                         .getFile()))
                                                // .setFooter(
                                                //         "Footer",
                                                //         new File(
                                                //                 Main.getClassLoader()
                                                //                         .getResource(
                                                //                                 "assets/PlayingCards/PNG-cards-1.3/" + c.suit() + "/" + c.cardSymbol().name().toLowerCase() + ".png")
                                                //                         .getFile()))
                                                // .setThumbnail(
                                                //         new File(
                                                //                 Main.getClassLoader()
                                                //                         .getResource("assets/profile.png")
                                                //                         .getFile()));

                                event.getButtonInteraction().getChannel().get().sendMessage(embed);
                }
                case "exit": {
                    if (Main.getGame().containsKey(username)) {
                        Main.getGame().remove(username);
                    }
                    event.getButtonInteraction().getMessage().delete();
                    event.getButtonInteraction().createImmediateResponder().setContent("Exited game").setFlags(MessageFlag.EPHEMERAL).respond();
                }
            }    
        }
    }

}