package com.github.qhss.utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.github.qhss.card.game.Blackjack;

public class ImageUtils {
    // Original code from: https://stackoverflow.com/questions/2318020/merging-two-images/2319251#2319251

    /**
     * Overlays cards (top for dealer, bottom for player)
     * Writes the final image to combined + username + .png
     * 
     * @param   bj          an instance of Blackjack class
     * @param   fin         whether or not it's the final turn
     * @param   username    the username of the player
     * 
     */
    public static void appendImages(Blackjack bj, boolean fin, String username) {
        try {
            // load source images
            BufferedImage background = ImageIO.read(new File("src/main/resources/assets/table.png"));
            // create the new image, canvas size is the max. of both image sizes
            BufferedImage combined =
                    new BufferedImage(
                            background.getWidth(), background.getHeight(), BufferedImage.TYPE_INT_ARGB);

            // paint both images, preserving the alpha channels
            Graphics g = combined.getGraphics();
            g.drawImage(background, 0, 0, null);

            int xDealer = background.getWidth() / 2 - (bj.getDealerCards().length * 60);

            if (!fin) {
                BufferedImage hidden = ImageIO.read(new File("src/main/resources/assets/Back.png"));
                BufferedImage dealer2 = ImageIO.read(new File(bj.getDealerCards()[1]));
                g.drawImage(hidden, xDealer, 40, null); // hidden card
                g.drawImage(dealer2, xDealer + 120, 40, null); // actual card
            } else {
                for (int i = 0; i < bj.getDealerCards().length; i++) {
                    g.drawImage(ImageIO.read(new File(bj.getDealerCards()[i])), xDealer, 40, null);
                    xDealer += 120;
                }
            }

            int xPlayer = background.getWidth() / 2 - (bj.getPlayerCards().length * 60);

            for (int i = 0; i < bj.getPlayerCards().length; i++) {
                g.drawImage(ImageIO.read(new File(bj.getPlayerCards()[i])), xPlayer, 210, null);
                xPlayer += 120;
            }

            g.dispose();

            // Save as new image
            ImageIO.write(
                    combined,
                    "PNG",
                    new File("src/main/resources/assets/combined" + username + ".png"));
        } catch (Exception e) {
            System.out.println(
                "A file is likely missing (Back.png, table.png, or playing card)\n" +
                "Please ensure they exist. It could also be read or write issues from the operating system.\n" +
                "Stacktrace follows:\n"
                );
            e.printStackTrace();
        }
    }
}
