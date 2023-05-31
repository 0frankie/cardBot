package com.github.qhss.listeners;

import java.awt.Color;
import java.io.File;
import java.util.concurrent.CompletableFuture;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.mention.AllowedMentions;
import org.javacord.api.entity.message.mention.AllowedMentionsBuilder;
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
                        Main.getGame().put(playerName, new Blackjack(array[JsonUtils.findPlayer(array, playerName)]));
                        
                        File aFile = new File(
                                Main.getClassLoader()
                                        .getResource(
                                                "assets/PlayingCards/PNG-cards-1.3/c/two.png")
                                        .getFile());

                                        AllowedMentions allowedMentions = new AllowedMentionsBuilder()
                                                .addUser(event.getSlashCommandInteraction().getUser().getId())
                                                .setMentionRoles(true)
                                                .setMentionEveryoneAndHere(false)
                                                .build();

                                                /*
                                                 *         new MessageBuilder()
                                                                .setAllowedMentions(allowedMentions)
                                                                .append(user0.getMentionTag())
                                                                .append(user1.getMentionTag())
                                                                .append(role.getMentionTag())
                                                                .append(role2.getMentionTag())
                                                                .append("@everyone")
                                                                .send(channel);
                                                 */
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
                        .setAllowedMentions(allowedMentions)
                        .append(event.getSlashCommandInteraction().getUser().getMentionTag())
                                .setContent("@" + event.getInteraction().getUser().getDiscriminatedName())
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
                        Main.getMsg().put(playerName, msg);
                    }
                
    }
    
}
