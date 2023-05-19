package com.github.qhss;

import io.github.cdimascio.dotenv.Dotenv;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;

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
                                SlashCommandOption.createWithOptions(
                                        SlashCommandOptionType.SUB_COMMAND,
                                        "game",
                                        "Starts a specific game",
                                        Arrays.asList(
                                                SlashCommandOption.createWithChoices(
                                                        SlashCommandOptionType.DECIMAL,
                                                        "bj",
                                                        "Plays a game of Blackjack",
                                                        true,
                                                        Arrays.asList(
                                                                SlashCommandOptionChoice.create(
                                                                        "bot", 0),
                                                                SlashCommandOptionChoice.create(
                                                                        "player", 1)))))))
                        .createGlobal(api)
                        .join();

        Set<SlashCommand> commands = api.getGlobalSlashCommands().join();
        // Add a listener which answers with "Pong!" if someone writes "!ping"
        api.addMessageCreateListener(
                event -> {
                    if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                        event.getChannel().sendMessage(String.valueOf(CardSymbol.J));
                    }
                });
    }
}
