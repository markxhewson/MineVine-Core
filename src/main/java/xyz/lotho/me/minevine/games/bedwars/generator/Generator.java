package xyz.lotho.me.minevine.games.bedwars.generator;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.plugin.Minevine;

public class Generator {

    private final Minevine instance;
    private final BedwarsGame bedwarsGame;
    private final Location spawnLocation;
    private final GeneratorType generatorType;

    private boolean active = false;
    private int secondsSinceActivation = 0;

    public Generator(Minevine instance, BedwarsGame bedwarsGame, Location spawnLocation, GeneratorType generatorType) {
        this.instance = instance;
        this.bedwarsGame = bedwarsGame;
        this.spawnLocation = spawnLocation;
        this.generatorType = generatorType;
    }

    public void spawn() {
        this.bedwarsGame.getWorld().dropItem(this.spawnLocation, new ItemStack(this.generatorType.getMaterialType()));
        this.bedwarsGame.getWorld().playSound(this.spawnLocation, Sound.CHICKEN_EGG_POP, 1, 2);
    }
}
