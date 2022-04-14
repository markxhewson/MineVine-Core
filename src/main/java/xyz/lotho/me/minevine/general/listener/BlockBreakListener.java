package xyz.lotho.me.minevine.general.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import xyz.lotho.me.minevine.games.Game;
import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.general.enums.GameType;
import xyz.lotho.me.minevine.plugin.Minevine;

public class BlockBreakListener implements Listener {

    private final Minevine instance;

    public BlockBreakListener(Minevine instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBreak(BlockBreakEvent event) throws Exception {
        Player player = event.getPlayer();

        Game game = this.instance.getGameManager().findGameByPlayer(player.getUniqueId());
        if (game == null) {
            if (player.getWorld().getName().equals(this.instance.getLobbySpawn().getWorld().getName())) {
                event.setCancelled(true);
            }

            return;
        };

        if (game.getType() == GameType.BEDWARS) {
            BedwarsGame bedwarsGame = (BedwarsGame) game;
            bedwarsGame.getBedwarsListenerManager().getBlockBreak().listen(event);
        }
    }
}
