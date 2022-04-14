package xyz.lotho.me.minevine.general.ui.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.me.minevine.general.ui.util.Menu;
import xyz.lotho.me.minevine.general.util.Chat;
import xyz.lotho.me.minevine.general.enums.GameType;
import xyz.lotho.me.minevine.general.util.ItemBuilder;
import xyz.lotho.me.minevine.plugin.Minevine;

public class BedwarsGameMenu extends Menu {

    private final Minevine instance;

    public BedwarsGameMenu(Minevine instance) {
        this.instance = instance;
    }

    @Override
    public String getMenuName() {
        return "Bedwars";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void setItems() {
        Inventory inventory = this.getInventory();

        inventory.setItem(13,
                new ItemBuilder(Material.BED).setDisplayName("&a&lBedwars (8 Teams)").setLore("&7Click to join the game queue!").build()
        );
    }

    @Override
    public void handleClick(InventoryClickEvent event) throws Exception {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem.getType() == Material.BED) {
            if (!this.instance.getQueueManager().isPresent(player.getUniqueId())) {
                this.instance.getQueueManager().enQueue(player.getUniqueId(), GameType.BEDWARS);
                player.sendMessage(Chat.colorize("&aYou have joined the queue for &eBedwars (8 Teams)"));
            } else {
                player.sendMessage(Chat.colorize("&cYou have already joined the queue for &eBedwars (8 Teams)"));
            }
        }
    }
}
