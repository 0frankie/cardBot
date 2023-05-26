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
import java.util.ArrayList;
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
        // ArrayList<Player> p = new ArrayList<>();
        // p.add(new Player(100, "HELLO"));
        // p.add(new Player(200, "JOHNNY"));

        // // writes to a JSON file
        // try (FileWriter fileWriter = new FileWriter("src/main/resources/balance.json")) {
        //     gson.toJson(p, fileWriter);
        //     fileWriter.close();
        // } catch (IOException e) {
        //     throw new RuntimeException(e);
        // }

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
                        String msg = event.getMessageContent();
                        String author = event.getMessageAuthor().getDiscriminatedName();
                        Player[] array;

                        try (Reader reader = new FileReader(classLoader.getResource("balance.json").getFile())) {
                            array = gson.fromJson(reader, Player[].class);
                            reader.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    if (msg.equalsIgnoreCase("!ping")) {
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
                    } else if (msg.equalsIgnoreCase("!json")) {
                        String message = "";
                        for (Player player : array) {
                            if (player.getUsername().equals(author)) {
                                message = String.valueOf(player.getMoney());
                            }
                        }
                        if (message.equals("")) {
                            Player[] a = new Player[array.length + 1];
                            for (int i = 0; i < array.length; i++) {
                                a[i] = array[i];
                            }
                            a[array.length] = new Player(100, author);
                
                            try (FileWriter fileWriter = new FileWriter("src/main/resources/balance.json")) {
                                gson.toJson(a, fileWriter);
                                fileWriter.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            message = author + ", money: " + a[array.length].getMoney();
                        }

                        event.getChannel().sendMessage(message);
                    } else if (msg.equalsIgnoreCase("!money+")) {
                        try (FileWriter fileWriter = new FileWriter("src/main/resources/balance.json")) {
                            for (Player plr : array) {
                                if (plr.getUsername().equals(author)) {
                                    plr.setMoney(plr.getMoney() + 100);
                                }
                            }
                            gson.toJson(array, fileWriter);
                            fileWriter.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        event.getChannel().sendMessage("money added! current money: " + event.getMessageAuthor().getDisplayName());
                    }
                });
    }

//     public static String appendPlayer(Player[] array, String username) {

        
//     }
}
