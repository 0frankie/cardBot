package com.github.qhss;

import java.awt.Color;
import java.io.File;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.mention.AllowedMentions;
import org.javacord.api.entity.message.mention.AllowedMentionsBuilder;
import org.javacord.api.entity.user.User;

import okhttp3.internal.ws.RealWebSocket.Message;


public interface DefaultEmbeds {
    static MessageBuilder defaultMessage(String message, User user, Blackjack bj) {
        EmbedBuilder embed =
            new EmbedBuilder()
                    .setTitle("Blackjack")
                    .setDescription("Play against the dealer!")
                    .addField("Dealer's Cards: ", "VALUE")
                    .addField("Your Cards: ", String.valueOf(bj.getScore(bj.getPlayer())))
                    .setColor(Color.CYAN)
                    .setFooter(
                            "Your bet: $" + bj.getBetAmount())
                    .setImage(new File("src/main/resources/assets/combined.png"))
                    .setThumbnail(
                            new File(
                                    Main.getClassLoader()
                                            .getResource("assets/profile.png")
                                            .getFile()));

        AllowedMentions allowedMentions = new AllowedMentionsBuilder()
                .addUser(user.getId())
                .setMentionRoles(true)
                .setMentionEveryoneAndHere(false)
                .build();
        
        return new MessageBuilder()
                .setAllowedMentions(allowedMentions)
                .append(user.getMentionTag())
                .setEmbed(embed)
                .addComponents(
                        ActionRow.of(
                                Button.success("hit", "Hit", "👏", false),
                                Button.primary("stand", "Stand", "🧍", false),
                                Button.danger("double-down", "Double Down", "🤑", false),
                                Button.secondary("exit", "Exit", "🚪", false)
                        )
                );
    }

    static EmbedBuilder finalEmbed(String message, String username, Blackjack bj, int change) {
        String finalMsg = message.contains("won") ? "You won: " : message.contains("lost") ? "You lost: " : "You tied.";
        return new EmbedBuilder()
                .setTitle("Blackjack")
                .setDescription(message)
                .addField("Dealer's Cards: ", String.valueOf(bj.getScore(bj.getDealer())))
                .addField("Your Cards: ", String.valueOf(bj.getScore(bj.getPlayer())))
                .addInlineField(finalMsg, String.valueOf(change))
                .setColor(Color.CYAN)
                .setFooter(
                        "Your bet: $" + bj.getBetAmount())
                .setImage(new File("src/main/resources/assets/combined.png"))
                .setThumbnail(
                        new File(
                                Main.getClassLoader()
                                        .getResource("assets/profile.png")
                                        .getFile()));
    }
}
