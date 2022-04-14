package xyz.lotho.me.minevine.games.bedwars.listener.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.games.bedwars.player.GamePlayer;
import xyz.lotho.me.minevine.games.bedwars.team.Team;
import xyz.lotho.me.minevine.general.util.Chat;
import xyz.lotho.me.minevine.plugin.Minevine;

public class BlockBreakManager {

    private final Minevine instance;
    private final BedwarsGame bedwarsGame;

    public BlockBreakManager(Minevine instance, BedwarsGame bedwarsGame) {
        this.instance = instance;
        this.bedwarsGame = bedwarsGame;
    }

    public void listen(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getType() == Material.BED_BLOCK) {
            GamePlayer breaker = bedwarsGame.getGamePlayerManager().getGamePlayer(player.getUniqueId());
            Team nearestTeam = bedwarsGame.getWorldManager().getNearestBed(block.getLocation());

            if (nearestTeam.getTeamName().equals(breaker.getTeam().getTeamName())) {
                player.sendMessage(Chat.colorize("&cYou cannot break your own bed!"));
                event.setCancelled(true);
                return;
            }

            nearestTeam.breakBed(breaker);
        }
    }
}
