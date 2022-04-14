package xyz.lotho.me.minevine.general.managers.world;

import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WorldGenerator {

    private final Minevine instance;
    private final Map<String, SlimeWorld> worldsMap = new HashMap<>();

    public WorldGenerator(Minevine instance) {
        this.instance = instance;
    }

    public Map<String, SlimeWorld> getWorldsMap() {
        return this.worldsMap;
    }

    public void addWorld(String worldName, SlimeWorld world) {
        this.getWorldsMap().put(worldName, world);
    }

    public SlimeWorld getWorld(String worldName) {
        return this.getWorldsMap().getOrDefault(worldName, null);
    }

    public void loadWorld(String loader, String templateWorld, String worldName) {
        SlimeLoader slimeLoader = this.instance.getSlimePlugin().getLoader(loader);

        this.instance.getServer().getScheduler().runTaskAsynchronously(this.instance, () -> {
            try {
                SlimeWorld template = this.instance.getSlimePlugin().loadWorld(slimeLoader, templateWorld, true, new SlimePropertyMap());
                SlimeWorld world = template.clone(worldName);

                this.instance.getServer().getScheduler().runTask(this.instance, () -> {
                    this.instance.getSlimePlugin().generateWorld(world);
                    this.addWorld(worldName, world);
                });

            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException e) {
                e.printStackTrace();
            }
        });
    }


}
