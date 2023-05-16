package com.github.qhss;

import io.github.cdimascio.dotenv.Dotenv;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        DiscordApi api =
                new DiscordApiBuilder()
                        .setToken(dotenv.get("DISCORD_TOKEN"))
                        .addIntents(Intent.MESSAGE_CONTENT)
                        .login()
                        .join();

        // Add a listener which answers with "Pong!" if someone writes "!ping"
        api.addMessageCreateListener(
                event -> {
                    if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                        event.getChannel().sendMessage(Card.J.getIcon());
                    }
                });
    }
}
