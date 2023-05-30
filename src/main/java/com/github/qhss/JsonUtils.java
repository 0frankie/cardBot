package com.github.qhss;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import com.google.gson.Gson;

public class JsonUtils {
    /**
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

    /**
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

    /**
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
