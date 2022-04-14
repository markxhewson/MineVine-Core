package xyz.lotho.me.minevine.general.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.lotho.me.minevine.general.enums.UserRank;
import xyz.lotho.me.minevine.general.managers.user.User;
import xyz.lotho.me.minevine.general.util.Chat;
import xyz.lotho.me.minevine.plugin.Minevine;

public class PlayerChatListener implements Listener {

    private final Minevine instance;

    public PlayerChatListener(Minevine instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        User user = this.instance.getUserManager().getUser(player.getUniqueId());

        UserRank rankType = user.getRank();
        event.setFormat(Chat.colorize(rankType.getPrefix() + "%s" + (rankType.isDonator() ? "&f: " + "%s" : "&7: " + "%s")));
    }
}
