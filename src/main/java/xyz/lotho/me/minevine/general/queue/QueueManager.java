package xyz.lotho.me.minevine.general.queue;

import xyz.lotho.me.minevine.general.enums.GameType;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QueueManager {

    private final Minevine instance;
    private final Map<UUID, GameType> gamesQueueMap = new HashMap<>();

    public QueueManager(Minevine instance) {
        this.instance = instance;
    }

    public Map<UUID, GameType> getGamesQueueMap() {
        return this.gamesQueueMap;
    }

    public void enQueue(UUID uuid, GameType gameType) {
        this.getGamesQueueMap().put(uuid, gameType);
    }

    public void deQueue(UUID uuid) {
        this.getGamesQueueMap().remove(uuid);
    }

    public boolean isPresent(UUID uuid) {
        return this.getGamesQueueMap().containsKey(uuid);
    }

    public GameType getQueuedGame(UUID uuid) {
        return this.getGamesQueueMap().getOrDefault(uuid, null);
    }

    public ArrayList<UUID> getGameQueue(GameType gameType) {
        ArrayList<UUID> queuedPlayers = new ArrayList<>();

        this.getGamesQueueMap().forEach((uuid, type) -> {
            if (type == gameType) queuedPlayers.add(uuid);
        });

        return queuedPlayers;
    }
}
