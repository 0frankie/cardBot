package com.github.qhss;

import com.github.qhss.card.game.Blackjack;
import com.github.qhss.listeners.ButtonListener;
import com.github.qhss.listeners.CommandListener;
import com.github.qhss.utils.CommandsInit;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.intent.Intent;

import java.util.HashMap;

public class Main implements CommandsInit {
    public static final HashMap<String, Blackjack> userGame = new HashMap<>();
    public static final HashMap<TextChannel, String> channelUser = new HashMap<>();

    public static void main(String[] args) {
        DiscordApi api =
                new DiscordApiBuilder()
                        .setToken(System.getenv("DISCORD_TOKEN")) // fetches from environment, not .env
                        .addIntents(Intent.MESSAGE_CONTENT)
                        .login()
                        .join();
        
        // adding listeners to the bot
        ButtonListener bListener = new ButtonListener();
        CommandListener cListener = new CommandListener();
        api.addListener(bListener);
        api.addListener(cListener);

        // Only needs to run once
        CommandsInit.init(api);
    }

    public static void addToMaps(String playerName, TextChannel textChannel, Blackjack blackjack) {
        channelUser.put(textChannel, playerName);
        userGame.put(playerName, blackjack);
    }

    public static void removeFromMaps(String playerName, TextChannel textChannel) {
        channelUser.remove(textChannel);
        userGame.remove(playerName);
    }
}
