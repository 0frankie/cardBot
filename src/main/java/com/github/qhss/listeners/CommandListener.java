package com.github.qhss.listeners;

import java.io.IOException;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import com.github.qhss.Blackjack;
import com.github.qhss.DefaultEmbeds;
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

                    if (slashCommandInteraction.getCommandName().equals("cbaddself")) {
                        if (JsonUtils.addPlayer(playerName)) {
                                slashCommandInteraction.createImmediateResponder().setContent("Added! Your init balance: $1000").respond();
                        }
                        else {
                                slashCommandInteraction.createImmediateResponder().setContent("You're already in the balance sheet!").respond();
                        }
                        return;
                    }

                    if (slashCommandInteraction.getCommandName().equals("cbbalance")) {
                        if (plrIndex != -1) {
                                slashCommandInteraction.createImmediateResponder().setContent("Your balance: $" + array[plrIndex].getMoney()).respond();
                        }
                        else {
                                slashCommandInteraction.createImmediateResponder().setContent("Add yourself to the balance sheet first!").respond();
                        }
                        return;
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

                                Player player = bj.getPlayer();
                                player.setMoney(player.getMoney() + (int) (2.5 * bj.getBetAmount()));
                                JsonUtils.changeMoney(playerName, player);

                                new MessageBuilder().setEmbed(
                                DefaultEmbeds.finalEmbed("You've got Blackjack and won!", playerName, bj))
                                        .send(slashCommandInteraction.getChannel().get());
                                return;
                        }
                        Main.getGame().put(playerName, bj);
                        try {
                                Main.appendImages(bj, false);
                        } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        Player player = bj.getPlayer();
                        JsonUtils.changeMoney(playerName, player);
                        DefaultEmbeds.defaultMessage("Play Blackjack against dealer!", slashCommandInteraction.getUser(), bj)
                                .send(slashCommandInteraction.getChannel().get());
                    }
                
    }
    
}
