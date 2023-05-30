package com.github.qhss;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.*;

import com.github.qhss.listeners.ButtonListener;
import com.github.qhss.listeners.CommandListener;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class Main {
    private static HashMap<String, CompletableFuture<Message>> userMessage = new HashMap<String, CompletableFuture<Message>>();

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    public static void main(String[] args) {
        DiscordApi api =
                new DiscordApiBuilder()
                        .setToken(System.getenv("DISCORD_TOKEN"))
                        .addIntents(Intent.MESSAGE_CONTENT)
                        .login()
                        .join();
        ButtonListener bListener = new ButtonListener();
        CommandListener cListener = new CommandListener();
        api.addListener(bListener);
        api.addListener(cListener);

        
        /*
         * think these only need to be run once but not sure
         * will refactor later (maybe) 
        */
        SlashCommand games =
                SlashCommand.with(
                        "cb",
                        "Card games",
                        Arrays.asList(
                                SlashCommandOption.createDecimalOption("bj", "Plays a game of Blackjack", true, 1, 10000)))
                        .createGlobal(api)
                        .join();

        SlashCommand daily = SlashCommand.with("cbdaily", "Claims daily money for Card Bot")
                .createGlobal(api)
                .join();

        Set<SlashCommand> commands = api.getGlobalSlashCommands().join();

        // Add a listener which answers with "Pong!" if someone writes "!ping"
        api.addMessageCreateListener(
                event -> {
                        if (!event.getMessageAuthor().isBotUser()) {
                                String msg = event.getMessageContent();
                                String author = event.getMessageAuthor().getDiscriminatedName();
                                Player[] array = JsonUtils.read();

                        if (msg.equalsIgnoreCase("!ping")) {
                                Card c = new Card(CardSymbol.EIGHT, 'c');
                                Card c4 = new Card(CardSymbol.FOUR, 'd');

                                EmbedBuilder embed =
                                        new EmbedBuilder()
                                                .setTitle("Blackjack")
                                                .setDescription("Play against the dealer!")
                                                .addField("Dealer's Cards: ", "VALUE")
                                                .addField("Your Cards: ", "VALUE")
                                                .setColor(Color.CYAN)
                                                .setImage(
                                                        new File(
                                                                Main.getClassLoader()
                                                                        .getResource(
                                                                                "assets/PlayingCards/PNG-cards-1.3/" + c4.suit() + "/" + c4.cardSymbol().name().toLowerCase() + ".png")
                                                                        .getFile()))
                                                .setFooter(
                                                        "Footer",
                                                        new File(
                                                                Main.getClassLoader()
                                                                        .getResource(
                                                                                "assets/PlayingCards/PNG-cards-1.3/" + c.suit() + "/" + c.cardSymbol().name().toLowerCase() + ".png")
                                                                        .getFile()))
                                                .setThumbnail(
                                                        new File(
                                                                Main.getClassLoader()
                                                                        .getResource("assets/profile.png")
                                                                        .getFile()));

                                event.getChannel().sendMessage(embed);
                        } else if (msg.equalsIgnoreCase("!json")) {
                                String message = "";
                                int plrIndex = JsonUtils.findPlayer(array, author);
                                if (plrIndex != -1) {
                                        message = String.valueOf(array[plrIndex].getMoney());
                                }
                                else {
                                Player[] a = new Player[array.length + 1];
                                for (int i = 0; i < array.length; i++) {
                                        a[i] = array[i];
                                }
                                a[array.length] = new Player(100, author);
                        
                                JsonUtils.write(a);
                                message = author + ", money: " + a[array.length].getMoney();
                                }

                                event.getChannel().sendMessage(message);
                        } else if (msg.equalsIgnoreCase("!money+")) {
                                int plrIndex = JsonUtils.findPlayer(array, author);
                                if (plrIndex != -1) {
                                    array[plrIndex].setMoney(array[plrIndex].getMoney() + 100);
                                }
                                JsonUtils.write(array);
                                event.getChannel().sendMessage("money added! current money: " + array[array.length - 1].getMoney());
                        }
                }
                });
    }

    public static boolean removeMessage(String username) {
        return false;
    }

    public static void addMessage(String username, CompletableFuture<Message> msg) {
        userMessage.put(username, msg);
    }
}
