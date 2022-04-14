package xyz.lotho.me.minevine.general.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import xyz.lotho.me.minevine.general.ui.util.Menu;
import xyz.lotho.me.minevine.plugin.Minevine;

public class InventoryClickListener implements Listener {

    private final Minevine instance;

    public InventoryClickListener(Minevine instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onClick(InventoryClickEvent event) throws Exception {
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof Menu)) return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) return;

        event.setCancelled(true);

        Menu menu = (Menu) holder;
        menu.handleClick(event);
    }
}
