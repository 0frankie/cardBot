package com.github.qhss;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.interaction.*;

import com.github.qhss.listeners.ButtonListener;
import com.github.qhss.listeners.CommandListener;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import javax.imageio.ImageIO;

public class Main {
    private static HashMap<String, Blackjack> userGame = new HashMap<String, Blackjack>();

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
    }

    public static boolean removeGame(String username) {
        if (userGame.containsKey(username)) {
            userGame.remove(username);
            return true;
        }
        return false;
    }

    public static HashMap<String, Blackjack> getGame() {
        return userGame;
    }

    public static void appendImages(Blackjack bj, boolean fin) throws IOException {

        // load source images
        BufferedImage background = ImageIO.read(new File("src/main/resources/assets/table.png"));
        // create the new image, canvas size is the max. of both image sizes
        BufferedImage combined = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        g.drawImage(background, 0, 0, null);
        if (!fin) {
                BufferedImage hidden = ImageIO.read(new File("src/main/resources/assets/Back.png"));
                BufferedImage dealer2 = ImageIO.read(new File(bj.getDealerCards()[1]));
                g.drawImage(hidden, 360, 40, null); // hidden card
                g.drawImage(dealer2, 510, 30, null); // actual card
        }
        else {
                int xDealer = background.getWidth() / bj.getDealerCards().length - 120;
        
                for (int i = 0; i < bj.getDealerCards().length; i++) {
                        g.drawImage(ImageIO.read(new File(bj.getDealerCards()[i])), xDealer, 40, null);
                        xDealer += 120;
                }
        }

        int xPlayer = background.getWidth() / bj.getPlayerCards().length - 120;
        
        
        for (int i = 0; i < bj.getPlayerCards().length; i++) {
                g.drawImage(ImageIO.read(new File(bj.getPlayerCards()[i])), xPlayer, 210, null);
                xPlayer += 120;
        }

        g.dispose();

        // Save as new image
        ImageIO.write(combined, "PNG", new File("src/main/resources/assets/combined.png"));

    }

    public static void beginningBoard(Blackjack bj) throws IOException {

        // load source images
        BufferedImage background = ImageIO.read(new File("src/main/resources/assets/table.png"));
        BufferedImage hidden = ImageIO.read(new File("src/main/resources/assets/Back.png"));
        BufferedImage dealer2 = ImageIO.read(new File(bj.getDealerCards()[1]));

        // create the new image, canvas size is the max. of both image sizes
        BufferedImage combined = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        g.drawImage(background, 0, 0, null);
        
        g.drawImage(hidden, 360, 40, null); // hidden card
        g.drawImage(dealer2, 510, 30, null); // actual card

        int index = 0;
        for (int i = 360; i <= 510; i += 150) {
                g.drawImage(ImageIO.read(new File(bj.getPlayerCards()[index])), i, 210, null);
                index++;
        }

        g.dispose();

        // Save as new image
        ImageIO.write(combined, "PNG", new File("src/main/resources/assets/beginning.png"));
    }
}
