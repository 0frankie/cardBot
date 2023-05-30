package com.github.qhss.listeners;

import java.awt.Color;
import java.io.File;
import java.util.concurrent.CompletableFuture;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import com.github.qhss.JsonUtils;
import com.github.qhss.Main;
import com.github.qhss.Player;

public class CommandListener implements SlashCommandCreateListener {

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction slashCommandInteraction =
                            event.getSlashCommandInteraction();
                    Player[] array = JsonUtils.read();
                            

                    
                    if (slashCommandInteraction.getCommandName().equals("cbdaily")) {
                        int plrIndex = JsonUtils.findPlayer(array, slashCommandInteraction.getUser().getDiscriminatedName());
                        if (plrIndex != -1) {
                                array[plrIndex].setMoney(array[plrIndex].getMoney() + 500);
                                JsonUtils.write(array);
                        }
                        slashCommandInteraction.createImmediateResponder().setContent("Added 500. Your current balance: " + array[plrIndex].getMoney()).setFlags(MessageFlag.EPHEMERAL).respond();
                        return;
                    }
                    SlashCommandInteractionOption s = slashCommandInteraction.getOptions().get(0);
                    

                    if (s.getName().equals("bj")) {
                        File aFile = new File(
                                Main.getClassLoader()
                                        .getResource(
                                                "assets/PlayingCards/PNG-cards-1.3/c/two.png")
                                        .getFile());

                        EmbedBuilder embed =
                                new EmbedBuilder()
                                        .setTitle("Blackjack")
                                        .setDescription("Play against the dealer!")
                                        .addField("Dealer's Cards: ", "VALUE")
                                        .addField("Your Cards: ", "VALUE")
                                        .setColor(Color.CYAN)
                                        .setFooter(
                                                "Your bet: $" + s.getDecimalValue().get(),
                                                aFile)
                                        .setThumbnail(
                                                new File(
                                                        Main.getClassLoader()
                                                                .getResource("assets/profile.png")
                                                                .getFile()));
                        CompletableFuture<Message> msg = new MessageBuilder()
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
