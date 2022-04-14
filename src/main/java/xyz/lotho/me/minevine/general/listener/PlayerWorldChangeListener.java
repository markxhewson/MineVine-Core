package xyz.lotho.me.minevine.general.listener;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import xyz.lotho.me.minevine.plugin.Minevine;

public class PlayerWorldChangeListener implements Listener {

    private final Minevine instance;

    public PlayerWorldChangeListener(Minevine instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onClick(PlayerChangedWorldEvent event) throws Exception {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (world.getName().equals(this.instance.getConfiguration().getString("lobby.worldName"))) {
            this.instance.getNpcManager().getNpcLocationsMap().forEach((npc, location) -> {
                npc.showNPC(player);
            });
        }
    }
}
