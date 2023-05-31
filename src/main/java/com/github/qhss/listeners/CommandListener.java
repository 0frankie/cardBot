package com.github.qhss.listeners;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import com.github.qhss.Blackjack;
import com.github.qhss.JsonUtils;
import com.github.qhss.Main;
import com.github.qhss.Player;

public class CommandListener implements SlashCommandCreateListener {

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction slashCommandInteraction =
                            event.getSlashCommandInteraction();
                    Player[] array = JsonUtils.read();
                    String playerName = slashCommandInteraction.getUser().getDiscriminatedName();
                    int plrIndex = JsonUtils.findPlayer(array, slashCommandInteraction.getUser().getDiscriminatedName());

                    if (slashCommandInteraction.getCommandName().equals("cbdaily")) {
                        if (plrIndex != -1) {
                                array[plrIndex].setMoney(array[plrIndex].getMoney() + 500);
                                JsonUtils.write(array);
                                slashCommandInteraction.createImmediateResponder().setContent("Added 500. Your current balance: " + array[plrIndex].getMoney()).setFlags(MessageFlag.EPHEMERAL).respond();
                        }
                        else {
                                slashCommandInteraction.createImmediateResponder().setContent("You haven't played a game yet. Do that first!").setFlags(MessageFlag.EPHEMERAL).respond();
                        }
                        return;
                    }

                    if (slashCommandInteraction.getCommandName().equals("cbaddplr")) {
                        if (JsonUtils.addPlayer(playerName)) {
                                slashCommandInteraction.createImmediateResponder().setContent("Added! Your init balance: $1000");
                        }

                    }
                    SlashCommandInteractionOption s = slashCommandInteraction.getOptions().get(0);

                    if (s.getName().equals("bj")) {
                        if (Main.getGame().containsKey(playerName)) {
                                slashCommandInteraction.createImmediateResponder().setContent("You're already playing a game!").setFlags(MessageFlag.EPHEMERAL).respond();
                                return;
                        }
                        Blackjack bj = new Blackjack(array[JsonUtils.findPlayer(array, playerName)], s.getDecimalValue().get().intValue());
                        if (bj.getScore(bj.getPlayer()) == 21) {
                                try {
                                        Main.appendImages(bj, true);
                                } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }
                                EmbedBuilder embed =
                                new EmbedBuilder()
                                        .setTitle("Blackjack")
                                        .setDescription("You've got Blackjack!")
                                        .addField("Dealer's Cards: ", String.valueOf(bj.getScore(bj.getDealer())))
                                        .addField("Your Cards: ", String.valueOf(bj.getScore(bj.getPlayer())))
                                        .setColor(Color.CYAN)
                                        .setFooter(
                                                "Your bet: $" + s.getDecimalValue().get())
                                        .setImage(new File("src/main/resources/assets/combined.png"))
                                        .setThumbnail(
                                                new File(
                                                        Main.getClassLoader()
                                                                .getResource("assets/profile.png")
                                                                .getFile()));
                        new MessageBuilder()
                                .setEmbed(embed)
                                .send(slashCommandInteraction.getChannel().get());
                                return;
                        }
                        Main.getGame().put(playerName, bj);
                        try {
                                Main.beginningBoard(bj);
                        } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        EmbedBuilder embed =
                                new EmbedBuilder()
                                        .setTitle("Blackjack")
                                        .setDescription("Play against the dealer!")
                                        .addField("Dealer's Cards: ", "VALUE")
                                        .addField("Your Cards: ", String.valueOf(bj.getScore(bj.getPlayer())))
                                        .setColor(Color.CYAN)
                                        .setFooter(
                                                "Your bet: $" + s.getDecimalValue().get())
                                        .setImage(new File("src/main/resources/assets/beginning.png"))
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
                                .send(slashCommandInteraction.getChannel().get());
                    }
                
    }
    
}
