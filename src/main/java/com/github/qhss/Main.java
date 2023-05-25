package com.github.qhss;

import com.google.gson.Gson;

import io.github.cdimascio.dotenv.Dotenv;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.*;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        // Dotenv dotenv = Dotenv.load();

        // attempts to open a file
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File f =
                new File(
                        classLoader
                                .getResource("assets/PlayingCards/PNG-cards-1.3/c/2.png")
                                .getFile());
        System.out.println(f.length());

        Gson gson = new Gson();
        Player[] p = {new Player(100, "HELLO"), new Player(200, "JOHNNY")};

        // writes to a JSON file
        try (FileWriter fileWriter = new FileWriter("src/main/resources/balance.json")) {
            gson.toJson(p, fileWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DiscordApi api =
                new DiscordApiBuilder()
                        .setToken(System.getenv("DISCORD_TOKEN"))
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
                        EmbedBuilder embed =
                                new EmbedBuilder()
                                        .setTitle("Blackjack")
                                        .setDescription("Play against the dealer!")
                                        .addField("Dealer's Cards: ", "VALUE")
                                        .addField("Your Cards: ", "VALUE")
                                        .setColor(Color.CYAN)
                                        .setFooter(
                                                "Footer",
                                                new File(
                                                        classLoader
                                                                .getResource(
                                                                        "assets/PlayingCards/PNG-cards-1.3/c/2.png")
                                                                .getFile()))
                                        .setThumbnail(
                                                new File(
                                                        classLoader
                                                                .getResource("assets/profile.png")
                                                                .getFile()));

                        slashCommandInteraction
                                .createImmediateResponder()
                                .addEmbed(embed)
                                .respond();
                    } else if (option.equals("_")) {
                        slashCommandInteraction
                                .createImmediateResponder()
                                .setContent("Alternative working properly!")
                                .setFlags(MessageFlag.EPHEMERAL)
                                .respond();
                    }
                });
        // Add a listener which answers with "Pong!" if someone writes "!ping"
        api.addMessageCreateListener(
                event -> {
                    if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                        EmbedBuilder embed =
                                new EmbedBuilder()
                                        .setTitle("Blackjack")
                                        .setDescription("Play against the dealer!")
                                        .addField("Dealer's Cards: ", "VALUE")
                                        .addField("Your Cards: ", "VALUE")
                                        .setColor(Color.CYAN)
                                        .setFooter(
                                                "Footer",
                                                new File(
                                                        classLoader
                                                                .getResource(
                                                                        "assets/PlayingCards/PNG-cards-1.3/c/2.png")
                                                                .getFile()))
                                        .setThumbnail(
                                                new File(
                                                        classLoader
                                                                .getResource("assets/profile.png")
                                                                .getFile()));

                        event.getChannel().sendMessage(embed);
                    } else if (event.getMessageContent().equalsIgnoreCase("!json")) {
                        try (Reader reader = new FileReader(classLoader.getResource("balance.json").getFile())) {
                            Player[] array = gson.fromJson(reader, Player[].class);
                            for (Player s : array)
                                event.getChannel().sendMessage(s.getUsername());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }
}
