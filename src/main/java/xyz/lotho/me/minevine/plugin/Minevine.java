package xyz.lotho.me.minevine.plugin;

import com.grinderwolf.swm.api.SlimePlugin;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.lotho.me.minevine.games.GameManager;
import xyz.lotho.me.minevine.general.command.EndGameCommand;
import xyz.lotho.me.minevine.general.command.SetRankCommand;
import xyz.lotho.me.minevine.general.command.StartGameCommand;
import xyz.lotho.me.minevine.general.data.DataUpdateThread;
import xyz.lotho.me.minevine.general.listener.*;
import xyz.lotho.me.minevine.general.managers.database.MongoManager;
import xyz.lotho.me.minevine.general.managers.npc.NPCInteractListener;
import xyz.lotho.me.minevine.general.managers.npc.NPCManager;
import xyz.lotho.me.minevine.general.managers.scoreboard.LobbyBoard;
import xyz.lotho.me.minevine.general.managers.user.UserManager;
import xyz.lotho.me.minevine.general.managers.world.WorldGenerator;
import xyz.lotho.me.minevine.general.queue.QueueManager;
import xyz.lotho.me.minevine.general.tab.TabManager;
import xyz.lotho.me.minevine.general.tab.TabRankManager;
import xyz.lotho.me.minevine.general.util.Configuration;

import java.util.Arrays;


public final class Minevine extends JavaPlugin {

    private final Configuration configuration = new Configuration(this, "config.yml");
    private final Configuration mapsConfiguration = new Configuration(this, "maps.yml");

    private final MongoManager mongoManager = new MongoManager(this);

    private final TabManager tabManager = new TabManager(this);
    private final TabRankManager tabRankManager = new TabRankManager(this);

    private final NPCManager npcManager = new NPCManager(this);
    private final UserManager userManager = new UserManager(this);

    private final QueueManager queueManager = new QueueManager(this);
    private final GameManager gameManager = new GameManager(this);

    private LobbyBoard lobbyBoard;
    private SlimePlugin slimePlugin;
    private WorldGenerator worldGenerator;

    private Location lobbySpawn;

    @Override
    public void onEnable() {
        this.slimePlugin = (SlimePlugin) this.getServer().getPluginManager().getPlugin("SlimeWorldManager");
        this.worldGenerator = new WorldGenerator(this);
        this.lobbyBoard = new LobbyBoard(this);

        this.getWorldGenerator().loadWorld(this.getConfiguration().getString("lobby.loader"), this.getConfiguration().getString("lobby.mongoWorldName"), this.getConfiguration().getString("lobby.worldName"));

        this.initListeners();
        this.initCommands();

        this.getServer().getScheduler().runTaskLater(this, () -> {
            this.getNpcManager().createNPC(
                    this.getServer().getWorld(this.getWorldGenerator().getWorld(this.getConfiguration().getString("lobby.worldName")).getName()),
                    "&e&lBedwars",
                    4.5, 62, -6.5, 36.5, 1.0
            );

            this.getNpcManager().createNPC(
                    this.getServer().getWorld(this.getWorldGenerator().getWorld(this.getConfiguration().getString("lobby.worldName")).getName()),
                    "&d&lManhunt",
                    6.5, 62, -3.5, 55.4, -1.1
            );

            this.setLobbySpawn(new Location(this.getServer().getWorld(this.getWorldGenerator().getWorld(this.getConfiguration().getString("lobby.worldName")).getName()), 0.5, 62.0, 0.5, (float) -180, (float) -0.5));
            new DataUpdateThread(this);
        }, 20L);

        new NPCInteractListener(this);
    }

    @Override
    public void onDisable() {
        this.getMongoManager().destroy();
    }

    public void initListeners() {
        Arrays.asList(
                new PlayerConnectionsListener(this),
                new PlayerChatListener(this),
                new InventoryClickListener(this),
                new PlayerInteractListener(this),
                new BlockBreakListener(this),
                new PlayerWorldChangeListener(this)
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    public void initCommands() {
        this.getCommand("setrank").setExecutor(new SetRankCommand(this));
        this.getCommand("startgame").setExecutor(new StartGameCommand(this));
        this.getCommand("endgame").setExecutor(new EndGameCommand(this));
    }

    public SlimePlugin getSlimePlugin() {
        return slimePlugin;
    }

    public YamlConfiguration getConfiguration() {
        return configuration.get();
    }

    public WorldGenerator getWorldGenerator() {
        return worldGenerator;
    }

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public TabRankManager getTabRankManager() {
        return tabRankManager;
    }

    public TabManager getTabManager() {
        return tabManager;
    }

    public LobbyBoard getLobbyBoard() {
        return lobbyBoard;
    }

    public NPCManager getNpcManager() {
        return npcManager;
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public YamlConfiguration getMapsConfiguration() {
        return this.mapsConfiguration.get();
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }
}
