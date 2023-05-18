package com.github.qhss;

import io.github.cdimascio.dotenv.Dotenv;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
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

        // Add a listener which answers with "Pong!" if someone writes "!ping"
        api.addMessageCreateListener(
                event -> {
                    if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                        event.getChannel().sendMessage(String.valueOf(CardSymbol.J));
                    }
                });
    }
}
