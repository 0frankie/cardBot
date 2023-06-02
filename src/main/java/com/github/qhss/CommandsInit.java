package com.github.qhss;

import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;

import java.util.Arrays;
import java.util.Set;

public interface CommandsInit {
    static void init(DiscordApi api) {
        SlashCommand games =
                SlashCommand.with(
                                "cb",
                                "Card games",
                                Arrays.asList(
                                        SlashCommandOption.createDecimalOption(
                                                "bj",
                                                "Plays a game of Blackjack",
                                                true,
                                                1,
                                                Integer.MAX_VALUE)))
                        .createGlobal(api)
                        .join();

        SlashCommand daily =
                SlashCommand.with("cbdaily", "Claims daily money for Card Bot")
                        .createGlobal(api)
                        .join();

        SlashCommand plrAdd =
                SlashCommand.with("cbaddself", "Adds yourself to the balance sheet")
                        .createGlobal(api)
                        .join();

        SlashCommand balance =
                SlashCommand.with("cbbalance", "Checks your balance!").createGlobal(api).join();

        Set<SlashCommand> commands = api.getGlobalSlashCommands().join();
    }
}
