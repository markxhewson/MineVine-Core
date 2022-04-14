package xyz.lotho.me.minevine.games.bedwars.player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.games.bedwars.scoreboard.GameBoard;
import xyz.lotho.me.minevine.games.bedwars.scoreboard.LobbyBoard;
import xyz.lotho.me.minevine.games.bedwars.team.Team;
import xyz.lotho.me.minevine.games.bedwars.team.TeamType;
import xyz.lotho.me.minevine.games.bedwars.util.Utility;
import xyz.lotho.me.minevine.general.util.Chat;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.UUID;

public class GamePlayer {

    private final Minevine instance;
    private final BedwarsGame bedwarsGame;
    private final UUID uuid;

    private LobbyBoard lobbyBoard;
    private GameBoard gameBoard;
    private Team team;

    private boolean finalKilled = false;

    private int kills = 0;
    private int deaths = 0;

    public GamePlayer(Minevine instance, BedwarsGame bedwarsGame, UUID uuid) {
        this.instance = instance;
        this.bedwarsGame = bedwarsGame;
        this.uuid = uuid;
    }

    public void sendTitle(Player player, String title, String subtitle, int time) {
        IChatBaseComponent titleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Chat.colorize(title) + "\"}");
        IChatBaseComponent subtitleComponent = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Chat.colorize(subtitle) + "\"}");

        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleComponent);
        PacketPlayOutTitle subTitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleComponent);
        PacketPlayOutTitle length = new PacketPlayOutTitle(5, time, 5);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitlePacket);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }

    public void spawn() {
        Player player = this.getPlayer();
        if (player == null) return;

        Utility.destroyInventory(player);
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(this.getTeam().getSpawnLocation());

        TeamType teamType = this.getTeam().getTeamType();
        ChatColor teamColor = this.getTeam().getTeamType().getTeamColor();
        player.setPlayerListName(teamColor + "" + ChatColor.BOLD + teamType.name().charAt(0) + ChatColor.RESET + teamColor + " " + player.getName());
    }

    public Player getPlayer() {
        return this.instance.getServer().getPlayer(this.getUniqueID());
    }

    public UUID getUniqueID() {
        return this.uuid;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setGameBoard() {
        if (this.gameBoard == null) this.gameBoard = new GameBoard(this.instance, this.bedwarsGame, this);
        else this.gameBoard.createBoard();
    }

    public void setLobbyBoard() {
        if (this.lobbyBoard == null) this.lobbyBoard = new LobbyBoard(this.instance, this.bedwarsGame, this);
        else this.lobbyBoard.createBoard();
    }

    public boolean isFinalKilled() {
        return finalKilled;
    }

    public void setFinalKilled(boolean finalKilled) {
        this.finalKilled = finalKilled;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
}
