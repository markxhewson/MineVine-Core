package xyz.lotho.me.minevine.games.bedwars.team;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum TeamType {

    RED("Red", ChatColor.RED, Color.RED, (short) 14),
    BLUE("Blue", ChatColor.BLUE, Color.BLUE, (short) 11),
    GREEN("Green", ChatColor.GREEN, Color.LIME, (short) 5),
    YELLOW("Yellow", ChatColor.YELLOW, Color.YELLOW, (short) 4),
    AQUA("Aqua", ChatColor.AQUA, Color.AQUA, (short) 3),
    WHITE("White", ChatColor.WHITE, Color.WHITE, (short) 0),
    PINK("Pink", ChatColor.LIGHT_PURPLE, Color.FUCHSIA, (short) 6),
    GRAY("Gray", ChatColor.DARK_GRAY, Color.GRAY, (short) 7);

    private final String displayName;
    private final ChatColor teamColor;
    private final Color armorColor;
    private final short metaID;

    TeamType(String displayName, ChatColor teamColor, Color armorColor, short metaID) {
        this.displayName = displayName;
        this.teamColor = teamColor;
        this.armorColor = armorColor;
        this.metaID = metaID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ChatColor getTeamColor() {
        return teamColor;
    }

    public Color getArmorColor() {
        return armorColor;
    }

    public short getMetaID() {
        return metaID;
    }
}
