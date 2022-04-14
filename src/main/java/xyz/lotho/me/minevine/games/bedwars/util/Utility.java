package xyz.lotho.me.minevine.games.bedwars.util;

import org.bukkit.entity.Player;

public class Utility {

    public static void destroyInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }
}
