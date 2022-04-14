package xyz.lotho.me.minevine.games.bedwars.team;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.games.bedwars.player.GamePlayer;
import xyz.lotho.me.minevine.general.util.Chat;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.ArrayList;

public class Team {

    private final Minevine instance;
    private final BedwarsGame bedwarsGame;
    private final TeamType teamType;

    private Location spawnLocation;
    private int maxTeamSize = 2;

    private boolean bedBroken = false;

    private final ArrayList<GamePlayer> teamMembers = new ArrayList<>();

    public Team(Minevine instance, BedwarsGame bedwarsGame, TeamType teamType) {
        this.instance = instance;
        this.bedwarsGame = bedwarsGame;
        this.teamType = teamType;
    }

    public void breakBed(GamePlayer breaker) {
        if (this.isBedBroken()) return;
        this.bedBroken = true;

        Player bedBreaker = this.instance.getServer().getPlayer(breaker.getUniqueID());
        Team breakerTeam = breaker.getTeam();

        this.bedwarsGame.getWorld().playSound(bedBreaker.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
        this.bedwarsGame.announce("\n&f&lBED DESTRUCTION > " + this.getTeamType().getTeamColor() + this.getTeamName() + " Bed &7was incinerated by " + breakerTeam.getTeamType().getTeamColor() + bedBreaker.getName() + "\n ");

        this.getTeamMembers().forEach(gamePlayer -> gamePlayer.sendTitle(gamePlayer.getPlayer(), "&cBED DESTROYED!", "&fYou will no longer respawn!", 50));
    }

    public void announce(String message) {
        for (GamePlayer gamePlayer : this.getTeamMembers()) {
            Player player = gamePlayer.getPlayer();
            if (player == null) return;

            if (player.getWorld().getName().equals(this.bedwarsGame.getWorld().getName())) {
                player.sendMessage(Chat.colorize(message));
            }
        }
    }

    public void loadTeam() {
        for (GamePlayer teamMember : this.getTeamMembers()) {
            teamMember.spawn();
        }
    }

    public String getTeamName() {
        return this.teamType.getDisplayName();
    }

    public ArrayList<GamePlayer> getAliveMembers() {
        ArrayList<GamePlayer> aliveMembers = new ArrayList<>();

        for (GamePlayer teamMember : this.getTeamMembers()) {
            if (!teamMember.isFinalKilled()) aliveMembers.add(teamMember);
        }

        return aliveMembers;
    }

    public void addToTeam(GamePlayer gamePlayer) {
        this.getTeamMembers().add(gamePlayer);
    }

    public void removeFromTeam(GamePlayer gamePlayer) {
        this.getTeamMembers().remove(gamePlayer);
    }

    public boolean isPresent(GamePlayer gamePlayer) {
        return this.getTeamMembers().contains(gamePlayer);
    }

    public TeamType getTeamType() {
        return teamType;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public ArrayList<GamePlayer> getTeamMembers() {
        return teamMembers;
    }

    public int getMaxTeamSize() {
        return maxTeamSize;
    }

    public void setMaxTeamSize(int maxTeamSize) {
        this.maxTeamSize = maxTeamSize;
    }

    public boolean isBedBroken() {
        return bedBroken;
    }
}
