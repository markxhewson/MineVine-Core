package xyz.lotho.me.minevine.general.managers.npc;

import org.bukkit.Location;
import org.bukkit.World;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NPCManager {

    private final Minevine instance;
    private final Map<NPC, Location> npcLocationsMap = new HashMap<>();

    public NPCManager(Minevine instance) {
        this.instance = instance;
    }

    public Map<NPC, Location> getNpcLocationsMap() {
        return this.npcLocationsMap;
    }

    public void createNPC(World world, String name, double x, double y, double z, double yaw, double pitch) {
        Location npcLocation = new Location(world, x, y, z, (float) yaw, (float) pitch);

        NPC npc = new NPC(this.instance, world, name, npcLocation);
        npc.create();

        this.getNpcLocationsMap().put(npc, npcLocation);
    }
}
