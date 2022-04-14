package xyz.lotho.me.minevine.games.bedwars.player;

import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.ArrayList;
import java.util.UUID;

public class GamePlayerManager {

    private final Minevine instance;
    private final BedwarsGame bedwarsGame;

    private final ArrayList<GamePlayer> gamePlayers = new ArrayList<>();

    public GamePlayerManager(Minevine instance, BedwarsGame bedwarsGame) {
        this.instance = instance;
        this.bedwarsGame = bedwarsGame;
    }

    public ArrayList<GamePlayer> getGamePlayers() {
        return this.gamePlayers;
    }

    public void addPlayer(UUID uuid) {
        this.getGamePlayers().add(new GamePlayer(this.instance, this.bedwarsGame, uuid));
    }

    public void removePlayer(GamePlayer gamePlayer) {
        this.getGamePlayers().remove(gamePlayer);
    }

    public GamePlayer getGamePlayer(UUID uuid) {
        for (GamePlayer gamePlayer : this.getGamePlayers()) {
            if (gamePlayer.getUniqueID() == uuid) {
                return gamePlayer;
            }
        }

        return null;
    }
}
