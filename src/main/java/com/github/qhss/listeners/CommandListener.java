package com.github.qhss.listeners;

import com.github.qhss.Main;
import com.github.qhss.card.Player;
import com.github.qhss.card.game.Blackjack;
import com.github.qhss.utils.DefaultEmbeds;
import com.github.qhss.utils.ImageUtils;
import com.github.qhss.utils.JsonUtils;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.time.LocalDate;
import java.util.function.Function;

public class CommandListener implements SlashCommandCreateListener {

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
        Player[] array = JsonUtils.read();
        String playerName = slashCommandInteraction.getUser().getDiscriminatedName();
        int plrIndex =
                JsonUtils.findPlayer(
                        array, slashCommandInteraction.getUser().getDiscriminatedName());

        // default response via lambda (captures environment)
        Function<String, Void> respond =
                msg -> {
                    slashCommandInteraction
                            .createImmediateResponder()
                            .setContent(msg)
                            .setFlags(MessageFlag.EPHEMERAL)
                            .respond();
                    return null;
                };

        // DON'T run this; incomplete
        if (slashCommandInteraction.getCommandName().equals("cbdaily")) {
            if (plrIndex != -1) {
                System.out.println (array[plrIndex].isDayAfter(LocalDate.now()));
                array[plrIndex].setMoney(array[plrIndex].getMoney() + 500);
                JsonUtils.write(array);
                respond.apply("Added 500. Your current balance: " + array[plrIndex].getMoney());
            } else {
                respond.apply("You haven't added yourself to the balance sheet yet. Do that first with /cbaddself!");
            }
            return;
        }

        if (slashCommandInteraction.getCommandName().equals("cbaddself")) {
            if (JsonUtils.addPlayer(playerName)) {
                respond.apply("Added! Your init balance: $1000");
            } else {
                array[plrIndex].setMoney(1000);
                JsonUtils.changeMoney(playerName, array[plrIndex]);
                respond.apply("Your balance is reset!");
            }
            return;
        }

        if (slashCommandInteraction.getCommandName().equals("cbbalance")) {
            if (plrIndex != -1) {
                respond.apply("Your balance: $" + array[plrIndex].getMoney());
            } else {
                respond.apply("Add yourself to the balance sheet first!");
            }
            return;
        }
        SlashCommandInteractionOption s = slashCommandInteraction.getOptions().get(0);

        if (s.getName().equals("bj")) {
            if (Main.userGame.containsKey(playerName)
                    || Main.channelUser.containsKey(slashCommandInteraction.getChannel().get())) {
                respond.apply("You're already playing a game or the channel already has an ongoing game!");
                return;
            }

            if (plrIndex == -1) {
                respond.apply("You're not in the balance sheet! Do /cbaddself to do so.");
                return;
            }

            if (s.getDecimalValue().get().intValue() > array[plrIndex].getMoney()) {
                respond.apply("You're too poor to bet that much right now. Check your balance!");
                return;
            }

            Blackjack bj = new Blackjack(array[plrIndex], s.getDecimalValue().get().intValue());
            Main.addToMaps(playerName, slashCommandInteraction.getChannel().get(), bj);

            if (bj.getScore(bj.getPlayer()) == 21) {
                ImageUtils.appendImages(bj, true, playerName);
                if (bj.getScore(bj.getDealer()) == 21) {
                    new MessageBuilder()
                        .setEmbed(
                                DefaultEmbeds.finalEmbed(
                                        "You've got Blackjack, but so did the dealer! You tied!",
                                        playerName,
                                        bj,
                                        0))
                        .send(slashCommandInteraction.getChannel().get());
                    Main.userGame.remove(playerName);
                    Main.channelUser.remove(slashCommandInteraction.getChannel().get());

                    // ignores error msg
                    slashCommandInteraction.createImmediateResponder().respond();
                    return;
                }

                Player player = bj.getPlayer();
                player.setMoney(player.getMoney() + (int) (2.5 * bj.getBetAmount()));
                JsonUtils.changeMoney(playerName, player);

                new MessageBuilder()
                        .setEmbed(
                                DefaultEmbeds.finalEmbed(
                                        "You've got Blackjack and won!",
                                        playerName,
                                        bj,
                                        (int) (2.5 * bj.getBetAmount())))
                        .send(slashCommandInteraction.getChannel().get());
                Main.userGame.remove(playerName);
                Main.channelUser.remove(slashCommandInteraction.getChannel().get());

                // ignores error msg
                slashCommandInteraction.createImmediateResponder().respond();
                return;
            }
            ImageUtils.appendImages(bj, false, playerName);
            
            DefaultEmbeds.defaultMessage(
                            "Play Blackjack against dealer!", slashCommandInteraction.getUser(), bj)
                    .send(slashCommandInteraction.getChannel().get());
            slashCommandInteraction.createImmediateResponder().respond();
        }
    }
}
