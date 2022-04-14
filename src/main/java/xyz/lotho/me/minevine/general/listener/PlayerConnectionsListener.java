package xyz.lotho.me.minevine.general.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.lotho.me.minevine.general.managers.user.User;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.UUID;

public class PlayerConnectionsListener implements Listener {

    private final Minevine instance;

    public PlayerConnectionsListener(Minevine instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        this.instance.getUserManager().addUser(uuid);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = this.instance.getUserManager().getUser(player.getUniqueId());

        event.setJoinMessage(null);
        player.teleport(this.instance.getLobbySpawn());

        this.instance.getServer().getScheduler().runTaskAsynchronously(this.instance, user::load);
        this.instance.getNpcManager().getNpcLocationsMap().forEach((npc, location) -> npc.showNPC(player));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = this.instance.getUserManager().getUser(player.getUniqueId());

        event.setQuitMessage(null);
        if (this.instance.getQueueManager().isPresent(player.getUniqueId())) {
            this.instance.getQueueManager().deQueue(player.getUniqueId());
        }

        if (user != null) {
            this.instance.getServer().getScheduler().runTaskAsynchronously(this.instance, user::save);
            this.instance.getUserManager().removeUser(player.getUniqueId());
        }

    }
}
