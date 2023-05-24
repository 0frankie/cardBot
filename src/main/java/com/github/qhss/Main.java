package com.github.qhss;

import io.github.cdimascio.dotenv.Dotenv;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.*;

import java.io.*;
import java.util.Arrays;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File f = new File(classLoader.getResource("test.txt").getFile());
        System.out.println(f.length());

        DiscordApi api =
                new DiscordApiBuilder()
                        .setToken(dotenv.get("DISCORD_TOKEN"))
                        .addIntents(Intent.MESSAGE_CONTENT)
                        .login()
                        .join();

        SlashCommand command =
                SlashCommand.with(
                                "cb",
                                "Card games",
                                Arrays.asList(
                                        SlashCommandOption.createWithChoices(
                                                SlashCommandOptionType.STRING,
                                                "game",
                                                "Plays a game of something",
                                                true,
                                                Arrays.asList(
                                                        SlashCommandOptionChoice.create(
                                                                "blackjack", "bj"),
                                                        SlashCommandOptionChoice.create(
                                                                "_", "_")))))
                        .createGlobal(api)
                        .join();

        Set<SlashCommand> commands = api.getGlobalSlashCommands().join();

        api.addSlashCommandCreateListener(
                event -> {
                    SlashCommandInteraction slashCommandInteraction =
                            event.getSlashCommandInteraction();
                    String option =
                            slashCommandInteraction.getArguments().get(0).getStringValue().get();

                    if (option.equals("bj")) {
                        slashCommandInteraction
                                .createImmediateResponder()
                                .setContent("Ok")
                                .setFlags(MessageFlag.EPHEMERAL)
                                .respond();
                    }
                    else if (option.equals("_")) {
                        slashCommandInteraction
                                .createImmediateResponder()
                                .setContent("Working properly!")
                                .setFlags(MessageFlag.EPHEMERAL)
                                .respond();
                    }
                });
        // Add a listener which answers with "Pong!" if someone writes "!ping"
        api.addMessageCreateListener(
                event -> {
                    if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                        event.getChannel().sendMessage(String.valueOf(CardSymbol.J));
                    }
                });
    }
}
