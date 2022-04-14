package xyz.lotho.me.minevine.games.bedwars.listener;

import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.games.bedwars.listener.events.BlockBreakManager;
import xyz.lotho.me.minevine.games.bedwars.listener.events.InteractManager;
import xyz.lotho.me.minevine.plugin.Minevine;

public class BedwarsListenerManager {

    private final Minevine instance;
    private final BedwarsGame bedwarsGame;

    private final BlockBreakManager blockBreakManager;
    private final InteractManager interactManager;

    public BedwarsListenerManager(Minevine instance, BedwarsGame bedwarsGame) {
        this.instance = instance;
        this.bedwarsGame = bedwarsGame;

        this.blockBreakManager = new BlockBreakManager(this.instance, bedwarsGame);
        this.interactManager = new InteractManager(this.instance, bedwarsGame);
    }

    public BlockBreakManager getBlockBreak() {
        return blockBreakManager;
    }

    public InteractManager getInteractManager() {
        return interactManager;
    }
}
