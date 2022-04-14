package xyz.lotho.me.minevine.games;

import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.ArrayList;
import java.util.UUID;

public class GameManager {

    private final Minevine instance;
    private final ArrayList<Game> games = new ArrayList<>();

    public GameManager(Minevine instance) {
        this.instance = instance;
    }

    public ArrayList<Game> getGames() {
        return this.games;
    }

    public void addGame(Game game) {
        this.getGames().add(game);
    }

    public void removeGame(Game game) {
        this.getGames().remove(game);
    }

    public Game findGameByPlayer(UUID uuid) {
        for (Game game : this.getGames()) {
            for (UUID playerUUID : game.getGamePlayers()) {
                if (playerUUID == uuid) {
                    return game;
                }
            }
        }

        return null;
    }
}
