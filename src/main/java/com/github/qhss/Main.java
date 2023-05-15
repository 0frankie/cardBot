package com.github.qhss;

import io.github.cdimascio.dotenv.Dotenv;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

public class Main {
    public static void main(String[] args) {
        //    DiscordApi api =
        //        new DiscordApiBuilder()
        //
        // .setToken("NzUzMzgwMTY1MzkxODc2MTg3.GcL-sZ.SP1tz1DCV1KLD3omBye7LPw6DK4DKx25rgc1W4")
        //            .addIntents(Intent.MESSAGE_CONTENT, Intent.GUILD_MESSAGE_REACTIONS)
        //            .login().join();
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
