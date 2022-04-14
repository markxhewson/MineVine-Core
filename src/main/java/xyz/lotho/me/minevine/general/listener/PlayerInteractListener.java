package xyz.lotho.me.minevine.general.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.lotho.me.minevine.games.Game;
import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.general.enums.GameType;
import xyz.lotho.me.minevine.plugin.Minevine;

public class PlayerInteractListener implements Listener {

    private final Minevine instance;

    public PlayerInteractListener(Minevine instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) throws Exception {
        Player player = event.getPlayer();

        Game game = this.instance.getGameManager().findGameByPlayer(player.getUniqueId());
        if (game == null) return;

        if (game.getType() == GameType.BEDWARS) {
            BedwarsGame bedwarsGame = (BedwarsGame) game;
            bedwarsGame.getBedwarsListenerManager().getInteractManager().listen(event);
        }
    }
}
