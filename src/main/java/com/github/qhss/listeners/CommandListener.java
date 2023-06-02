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
                                array[plrIndex].setMoney(1000);
                                JsonUtils.changeMoney(playerName, array[plrIndex]);
                                slashCommandInteraction.createImmediateResponder().setContent("Your balance is reset!").respond();
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
                        if (Main.userGame.containsKey(playerName) || Main.channelUser.containsKey(slashCommandInteraction.getChannel().get())) {
                                slashCommandInteraction.createImmediateResponder().setContent("You're already playing a game or the channel already has an ongoing game!").setFlags(MessageFlag.EPHEMERAL).respond();
                                return;
                        }

                        if (plrIndex == -1) {
                                slashCommandInteraction.createImmediateResponder().setContent("You're not in the balance sheet! Do /cbaddself to do so.").setFlags(MessageFlag.EPHEMERAL).respond();
                                return;
                        }

                        if (s.getDecimalValue().get().intValue() > array[plrIndex].getMoney()) {
                                slashCommandInteraction.createImmediateResponder().setContent("You're too poor to bet that much right now. Check your balance!").setFlags(MessageFlag.EPHEMERAL).respond();
                                return;
                        }

                        Main.channelUser.put(slashCommandInteraction.getChannel().get(), playerName);
                        Blackjack bj = new Blackjack(array[plrIndex], s.getDecimalValue().get().intValue());
                        Main.userGame.put(playerName, bj);
                        if (bj.getScore(bj.getPlayer()) == 21) {
                                try {
                                        Main.appendImages(bj, true, playerName);
                                } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                }

                                Player player = bj.getPlayer();
                                player.setMoney(player.getMoney() + (int) (2.5 * bj.getBetAmount()));
                                JsonUtils.changeMoney(playerName, player);

                                new MessageBuilder().setEmbed(
                                DefaultEmbeds.finalEmbed("You've got Blackjack and won!", playerName, bj, (int) (2.5 * bj.getBetAmount())))
                                        .send(slashCommandInteraction.getChannel().get());
                                Main.userGame.remove(playerName);
                                Main.channelUser.remove(slashCommandInteraction.getChannel().get());

                                // ignores error msg
                                slashCommandInteraction.createImmediateResponder().respond();
                                return;
                        }
                        try {
                                Main.appendImages(bj, false, playerName);
                        } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        Player player = bj.getPlayer();
                        JsonUtils.changeMoney(playerName, player);
                        DefaultEmbeds.defaultMessage("Play Blackjack against dealer!", slashCommandInteraction.getUser(), bj)
                                .send(slashCommandInteraction.getChannel().get());
                        slashCommandInteraction.createImmediateResponder().respond();
                    }
                
    }
    
}
