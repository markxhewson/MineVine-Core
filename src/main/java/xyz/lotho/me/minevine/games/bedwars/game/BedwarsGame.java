package xyz.lotho.me.minevine.games.bedwars.game;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import xyz.lotho.me.minevine.games.Game;
import xyz.lotho.me.minevine.games.bedwars.generator.Generator;
import xyz.lotho.me.minevine.games.bedwars.listener.BedwarsListenerManager;
import xyz.lotho.me.minevine.games.bedwars.player.GamePlayer;
import xyz.lotho.me.minevine.games.bedwars.player.GamePlayerManager;
import xyz.lotho.me.minevine.games.bedwars.team.Team;
import xyz.lotho.me.minevine.games.bedwars.team.TeamManager;
import xyz.lotho.me.minevine.games.bedwars.ui.TeamSelectMenu;
import xyz.lotho.me.minevine.games.bedwars.util.Utility;
import xyz.lotho.me.minevine.games.bedwars.world.WorldManager;
import xyz.lotho.me.minevine.general.enums.GameType;
import xyz.lotho.me.minevine.general.util.Chat;
import xyz.lotho.me.minevine.general.util.ItemBuilder;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.ArrayList;
import java.util.UUID;

public class BedwarsGame extends Game {

    private final Minevine instance;
    private final UUID gameUUID;
    private final String mapName;
    private final ArrayList<UUID> gamePlayers;

    private GameState gameState;
    private World world;
    private Location lobbyLocation;

    private Location cornerOne;
    private Location cornerTwo;

    private boolean started = false;
    private int lobbyTime = 20;
    private int elapsedTime = 0;

    private int gameTickID;

    private final ArrayList<Generator> generators = new ArrayList<>();

    private final WorldManager worldManager;
    private final TeamManager teamManager;
    private final GamePlayerManager gamePlayerManager;
    private final TeamSelectMenu teamSelectMenu;

    private final BedwarsListenerManager bedwarsListenerManager;

    public BedwarsGame(Minevine instance, UUID gameUUID, String mapName, ArrayList<UUID> gamePlayers) {
        this.instance = instance;
        this.gameUUID = gameUUID;
        this.mapName = mapName;
        this.gamePlayers = gamePlayers;

        this.worldManager = new WorldManager(this.instance, this);
        this.teamManager = new TeamManager(this.instance, this);
        this.gamePlayerManager = new GamePlayerManager(this.instance, this);
        this.teamSelectMenu = new TeamSelectMenu(this.instance, this);

        this.bedwarsListenerManager = new BedwarsListenerManager(this.instance, this);

        setGameState(GameState.PRELOBBY);
    }

    public void announce(String message) {
        for (GamePlayer gamePlayer : this.getGamePlayerManager().getGamePlayers()) {
            Player player = gamePlayer.getPlayer();
            if (player == null) return;

            if (player.getWorld().getName().equals(this.getWorld().getName())) {
                player.sendMessage(Chat.colorize(message));
            }
        }
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameState = gameState;

        switch (gameState) {
            case PRELOBBY:
                this.getWorldManager().loadMap("Lighthouse");
                break;

            case LOBBY:
                for (UUID uuid : this.getGamePlayers()) {
                    this.getGamePlayerManager().addPlayer(uuid);
                }

                this.loadLobbyPlayers();

                BukkitTask bukkitTask = this.instance.getServer().getScheduler().runTaskTimer(this.instance, this::gameTick, 0L, 20L);
                this.gameTickID = bukkitTask.getTaskId();

                break;

            case PLAYING:
                this.setStarted(true);

                for (Team team : this.getTeamManager().getTeams()) {
                    team.loadTeam();
                }

                break;

            case ENDED:
                Team winningTeam = this.getTeamManager().getWinningTeam();

                for (GamePlayer gamePlayer : this.getGamePlayerManager().getGamePlayers()) {
                    Player player = gamePlayer.getPlayer();
                    if (player == null) return;

                    if (gamePlayer.getTeam() == winningTeam) gamePlayer.sendTitle(player, "&a&lYOU WIN!", "", 100);
                    else gamePlayer.sendTitle(player, "&c&lGAME OVER", "", 100);

                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_DEATH, 1, 1);

                    player.sendMessage(Chat.colorize("\n&c&lGAME OVER!\n" + winningTeam.getTeamType().getTeamColor() + winningTeam.getTeamName() + " &fhas won the game!"));
                    player.sendMessage(Chat.colorize("\n\n&aCongratulations:"));

                    winningTeam.getTeamMembers().forEach(winningPlayer -> {
                        player.sendMessage(Chat.colorize("&8 - &f" + winningPlayer.getPlayer().getName() + " &7(Kills: &f" + winningPlayer.getKills() + " &7| Deaths: &f" + winningPlayer.getDeaths() + "&7)"));
                    });

                    player.sendMessage("");
                }
                this.instance.getServer().getScheduler().runTaskLater(this.instance, this::endGame, 60);

                break;

        }
    }

    public void endGame() {
        for (GamePlayer gamePlayer : this.getGamePlayerManager().getGamePlayers()) {
            Player player = gamePlayer.getPlayer();
            if (player == null) return;

            player.getEnderChest().clear();
            Utility.destroyInventory(player);
            player.setGameMode(GameMode.SURVIVAL);

            player.teleport(this.instance.getLobbySpawn());
            player.setPlayerListName(null);
        }

        this.getWorldManager().unloadMap();
        this.instance.getServer().getScheduler().cancelTask(this.gameTickID);
        this.instance.getGameManager().removeGame(this);
    }

    @Override
    public void gameTick() {
        switch (getGameState()) {
            case LOBBY:
                if (lobbyTime <= 0) {
                    setGameState(GameState.PLAYING);
                    return;
                }

                if (lobbyTime == 15 || lobbyTime == 10 || lobbyTime <= 5) {
                    this.announce("&eThe game starts in &a" + lobbyTime + " &esecond" + (lobbyTime == 1 ? "" : "s") + "..");
                }
                lobbyTime -= 1;

                break;

            case PLAYING:
                this.getGenerators().forEach(Generator::spawn);

                for (GamePlayer gamePlayer : this.getGamePlayerManager().getGamePlayers()) {
                   gamePlayer.setGameBoard();
                }

                if (this.getTeamManager().getAliveTeams().size() == 1) {
                    this.setGameState(GameState.ENDED);
                }

                break;
        }
    }

    public void loadLobbyPlayers() {
        for (GamePlayer gamePlayer : this.getGamePlayerManager().getGamePlayers()) {
            Player player = gamePlayer.getPlayer();
            if (player == null) return;

            gamePlayer.setLobbyBoard();

            Utility.destroyInventory(player);
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(new Location(this.getWorld(), 0, 90, 0));
            player.getInventory().setItem(4, new ItemBuilder(Material.NOTE_BLOCK).setDisplayName("&aSelect Your Team").build());
        }
    }

    @Override
    public GameType getType() {
        return GameType.BEDWARS;
    }

    @Override
    public ArrayList<UUID> getGamePlayers() {
        return this.gamePlayers;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    public String getMapName() {
        return this.mapName;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }

    public GameState getGameState() {
        return gameState;
    }

    public ArrayList<Generator> getGenerators() {
        return generators;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public TeamSelectMenu getTeamSelectMenu() {
        return teamSelectMenu;
    }

    public GamePlayerManager getGamePlayerManager() {
        return gamePlayerManager;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }

    public BedwarsListenerManager getBedwarsListenerManager() {
        return bedwarsListenerManager;
    }
}
