package xyz.lotho.me.minevine.general.util;

import org.bukkit.ChatColor;

public class Chat {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
