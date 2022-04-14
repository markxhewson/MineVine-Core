package xyz.lotho.me.minevine.general.managers.npc;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import xyz.lotho.me.minevine.general.util.Chat;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.UUID;

public class NPC {

    private final Minevine instance;

    private final World world;
    private final String name;
    private final Location location;

    private EntityPlayer npc;

    public NPC(Minevine instance, World world, String name, Location location) {
        this.instance = instance;
        this.world = world;
        this.name = name;
        this.location = location;
    }

    public void create() {
        MinecraftServer minecraftServer = ((CraftServer) this.instance.getServer()).getServer();
        WorldServer worldServer = ((CraftWorld) this.world).getHandle();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), Chat.colorize(name));
        this.npc = new EntityPlayer(minecraftServer, worldServer, gameProfile, new PlayerInteractManager(worldServer));

        getNpc().setLocation(this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
    }

    public void showNPC(Player player) {
        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;

        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, getNpc()));
        playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(getNpc()));
        playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation(getNpc(), (byte) (getNpc().yaw * 256 / 360)));
        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getNpc()));
    }

    public EntityPlayer getNpc() {
        return npc;
    }
}
