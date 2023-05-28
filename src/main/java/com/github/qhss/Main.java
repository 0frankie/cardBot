package com.github.qhss;

import com.google.gson.Gson;

import io.github.cdimascio.dotenv.Dotenv;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.*;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        
        DiscordApi api =
                new DiscordApiBuilder()
                        .setToken(System.getenv("DISCORD_TOKEN"))
                        .addIntents(Intent.MESSAGE_CONTENT)
                        .login()
                        .join();

        
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

        api.addSlashCommandCreateListener(
                event -> {
                    SlashCommandInteraction slashCommandInteraction =
                            event.getSlashCommandInteraction();
                    Player[] array = read();
                            

                    
                    if (slashCommandInteraction.getCommandName().equals("cbdaily")) {
                        int plrIndex = findPlayer(array, slashCommandInteraction.getUser().getDiscriminatedName());
                        if (plrIndex != -1) {
                                array[plrIndex].setMoney(array[plrIndex].getMoney() + 500);
                                write(array);
                        }
                        slashCommandInteraction.createImmediateResponder().setContent("Added 500. Your current balance: " + array[plrIndex].getMoney()).setFlags(MessageFlag.EPHEMERAL).respond();
                        return;
                    }
                    SlashCommandInteractionOption s = slashCommandInteraction.getOptions().get(0);
                    

                    if (s.getName().equals("bj")) {
                        File aFile = new File(
                                classLoader
                                        .getResource(
                                                "assets/PlayingCards/PNG-cards-1.3/c/two.png")
                                        .getFile());

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
                                                        classLoader
                                                                .getResource("assets/profile.png")
                                                                .getFile()));
                        new MessageBuilder()
                                .append("Pls work", MessageDecoration.BOLD)
                                .addAttachment(aFile)
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
                    }
                });
        // Add a listener which answers with "Pong!" if someone writes "!ping"
        api.addMessageCreateListener(
                event -> {
                        if (!event.getMessageAuthor().isBotUser()) {
                                String msg = event.getMessageContent();
                                String author = event.getMessageAuthor().getDiscriminatedName();
                                Player[] array = read();

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
                                                                classLoader
                                                                        .getResource(
                                                                                "assets/PlayingCards/PNG-cards-1.3/" + c4.suit() + "/" + c4.cardSymbol().name().toLowerCase() + ".png")
                                                                        .getFile()))
                                                .setFooter(
                                                        "Footer",
                                                        new File(
                                                                classLoader
                                                                        .getResource(
                                                                                "assets/PlayingCards/PNG-cards-1.3/" + c.suit() + "/" + c.cardSymbol().name().toLowerCase() + ".png")
                                                                        .getFile()))
                                                .setThumbnail(
                                                        new File(
                                                                classLoader
                                                                        .getResource("assets/profile.png")
                                                                        .getFile()));

                                event.getChannel().sendMessage(embed);
                        } else if (msg.equalsIgnoreCase("!json")) {
                                String message = "";
                                int plrIndex = findPlayer(array, author);
                                if (plrIndex != -1) {
                                        message = String.valueOf(array[plrIndex].getMoney());
                                }
                                else {
                                Player[] a = new Player[array.length + 1];
                                for (int i = 0; i < array.length; i++) {
                                        a[i] = array[i];
                                }
                                a[array.length] = new Player(100, author);
                        
                                write(a);
                                message = author + ", money: " + a[array.length].getMoney();
                                }

                                event.getChannel().sendMessage(message);
                        } else if (msg.equalsIgnoreCase("!money+")) {
                                int plrIndex = findPlayer(array, author);
                                if (plrIndex != -1) {
                                    array[plrIndex].setMoney(array[plrIndex].getMoney() + 100);
                                }
                                write(array);
                                event.getChannel().sendMessage("money added! current money: " + array[array.length - 1].getMoney());
                        }
                }
                });
    }

    /*
     * finds a player in the array.
     * returns -1 if not found
     * 
     * @param   players   the array of players to search in
     * @param   username  the username of the Player
     * 
     * @return          The index of the player if found, else -1
     */
    public static int findPlayer(Player[] players, String username) {
        for (int i = 0; i < players.length; i++) {
                if (players[i].getUsername().equals(username))
                        return i;
        }
        return -1;
    }

    /*
     * finds a player in the array.
     * returns -1 if not found
     * 
     * @param   players   the array of players to search in
     * @param   username  the username of the Player
     * 
     * @return          The index of the player if found, else -1
     */
    public static void write(Player[] array) {
        try (FileWriter fileWriter = new FileWriter("src/main/resources/balance.json")) {
                new Gson().toJson(array, fileWriter);
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }

    /*
     * finds a player in the array.
     * returns -1 if not found
     * 
     * @param   players   the array of players to search in
     * @param   username  the username of the Player
     * 
     * @return          The index of the player if found, else -1
     */
    public static Player[] read() {
        try (Reader reader = new FileReader("src/main/resources/balance.json")) {
                return new Gson().fromJson(reader, Player[].class);
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }
}
