package com.github.qhss.utils;

import com.github.qhss.card.game.Player;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class JsonUtils {
    /**
     * finds a player in the array.
     * returns -1 if not found
     * 
     * @param   players     the array of players to search in
     * @param   username    the username of the Player
     * 
     * @return              The index of the player if found, else -1
     */
    public static int findPlayer(Player[] players, String username) {
        for (int i = 0; i < players.length; i++) {
                if (players[i].getUsername().equals(username))
                        return i;
        }
        return -1;
    }

    /**
     * writes array data to balance sheet
     * 
     * @param   array   the array of players to write
     *
     */
    public static void write(Player[] array) {
        try (FileWriter fileWriter = new FileWriter("src/main/resources/balance.json")) {
                new Gson().toJson(array, fileWriter);
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }

    /**
     * Reads the balance sheet and returns an array of stripped-down Player objects
     * 
     * @return          Array of stripped-down player objects in balance sheet
     */
    public static Player[] read() {
        try (Reader reader = new FileReader("src/main/resources/balance.json")) {
                return new Gson().fromJson(reader, Player[].class);
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }

    /**
     * Writes a user to the balance sheet with balance $1000
     *
     * @param   author  writer of the command
     *
     * @return          whether adding the player was successful
     */
    public static boolean addPlayer(String author) {
        Player[] array = read();
        int plrIndex = JsonUtils.findPlayer(array, author);
        if (plrIndex == -1) {
                Player[] a = new Player[array.length + 1];
                for (int i = 0; i < array.length; i++) {
                        a[i] = array[i];
                }
                a[array.length] = new Player(1000, author);

                JsonUtils.write(a);
                return true;
        }
        return false;
    }

    /**
     * Writes a user to the balance sheet with balance $1000
     *
     * @param   username    writer of the command
     * @param   player      Player object of writer of command
     */
    public static void changeMoney(String username, Player player) {
        Player[] players = read();
        if (findPlayer(players, username) != -1) {
                players[findPlayer(players, username)] = player;
                write(players);
        }
    }
    
}
