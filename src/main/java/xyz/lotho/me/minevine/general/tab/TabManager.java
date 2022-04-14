package xyz.lotho.me.minevine.general.tab;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import xyz.lotho.me.minevine.general.util.Chat;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.lang.reflect.Field;

public class TabManager {

    private final Minevine instance;

    public TabManager(Minevine instance) {
        this.instance = instance;
    }

    public void sendTablist() {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        Object header = new ChatComponentText(Chat.colorize("\n &6&lMineVine\n      &7play.minevine.org      \n "));
        Object footer = new ChatComponentText(Chat.colorize("\n&estore.minevine.org\n "));

        try {
            Field a = packet.getClass().getDeclaredField("a");
            a.setAccessible(true);

            Field b = packet.getClass().getDeclaredField("b");
            b.setAccessible(true);

            a.set(packet, header);
            b.set(packet, footer);

            if (this.instance.getServer().getOnlinePlayers().size() == 0) return;

            for (Player onlinePlayer : this.instance.getServer().getOnlinePlayers()) {
                CraftPlayer craftPlayer = (CraftPlayer) onlinePlayer;
                craftPlayer.getHandle().playerConnection.sendPacket(packet);
            }
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

}
