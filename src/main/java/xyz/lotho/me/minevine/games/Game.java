package xyz.lotho.me.minevine.games;

import xyz.lotho.me.minevine.games.bedwars.game.GameState;
import xyz.lotho.me.minevine.general.enums.GameType;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Game {

    public abstract void gameTick();

    public abstract ArrayList<UUID> getGamePlayers();

    public abstract void setGameState(GameState gameState);

    public abstract GameType getType();

    public abstract boolean isStarted();

}
