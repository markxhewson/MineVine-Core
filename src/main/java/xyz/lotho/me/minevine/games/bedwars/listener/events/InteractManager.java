package xyz.lotho.me.minevine.games.bedwars.listener.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.general.util.Chat;
import xyz.lotho.me.minevine.plugin.Minevine;

public class InteractManager {

    private final Minevine instance;
    private final BedwarsGame bedwarsGame;

    public InteractManager(Minevine instance, BedwarsGame bedwarsGame) {
        this.instance = instance;
        this.bedwarsGame = bedwarsGame;
    }

    public void listen(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

        Player player = event.getPlayer();

        if (!bedwarsGame.isStarted() && event.getItem().getType() == Material.NOTE_BLOCK) {
            player.sendMessage(Chat.colorize("&aGame found! &7Opening menu.."));
            bedwarsGame.getTeamSelectMenu().open(player);
        }
    }
}
