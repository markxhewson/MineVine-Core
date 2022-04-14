package xyz.lotho.me.minevine.general.managers.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.lotho.me.minevine.general.util.Chat;
import xyz.lotho.me.minevine.plugin.Minevine;
import xyz.lotho.me.minevine.general.ui.menus.BedwarsGameMenu;

import java.util.concurrent.atomic.AtomicReference;

public class NPCInteractListener {

    private final Minevine instance;

    public NPCInteractListener(Minevine instance) {
        this.instance = instance;

        checkInteract();
    }

    public void checkInteract() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(
                new PacketAdapter(this.instance, PacketType.Play.Client.USE_ENTITY) {

                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
                            try {
                                PacketContainer packet = event.getPacket();
                                EnumWrappers.EntityUseAction useAction = packet.getEntityUseActions().read(0);

                                if (useAction != EnumWrappers.EntityUseAction.INTERACT) return;

                                int entityID = packet.getIntegers().read(0);
                                handleInteract(event.getPlayer(), findEntity(entityID));
                            } catch (Exception e) {}
                        }
                    }
        });
    }

    public void handleInteract(Player player, NPC npc) {
        String name = ChatColor.stripColor(npc.getNpc().getName().toLowerCase());

        if (name.equals("bedwars")) {
            player.sendMessage(Chat.colorize("&aYou have interacted with the Bedwars NPC!"));
            new BedwarsGameMenu(this.instance).open(player);
        }
    }

    public NPC findEntity(int entityID) {
        AtomicReference<NPC> clickedNPC = new AtomicReference<>();

        this.instance.getNpcManager().getNpcLocationsMap().forEach((npc, location) -> {
            if (npc.getNpc().getId() == entityID) {
                clickedNPC.set(npc);
            }
        });


        return clickedNPC.get();
    }
}
