package xyz.lotho.me.minevine.games.bedwars.ui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.games.bedwars.player.GamePlayer;
import xyz.lotho.me.minevine.games.bedwars.team.Team;
import xyz.lotho.me.minevine.general.managers.user.User;
import xyz.lotho.me.minevine.general.ui.util.Menu;
import xyz.lotho.me.minevine.general.util.Chat;
import xyz.lotho.me.minevine.general.util.ItemBuilder;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.concurrent.atomic.AtomicInteger;

public class TeamSelectMenu extends Menu {

    private final Minevine instance;
    private final BedwarsGame bedwarsGame;

    public TeamSelectMenu(Minevine instance, BedwarsGame bedwarsGame) {
        this.instance = instance;
        this.bedwarsGame = bedwarsGame;
    }

    @Override
    public String getMenuName() {
        return "Team Selector";
    }

    @Override
    public int getSlots() {
        return 18;
    }

    @Override
    public void setItems() {
        Inventory inventory = this.getInventory();

        AtomicInteger counter = new AtomicInteger(0);

        for (Team team : this.bedwarsGame.getTeamManager().getTeams()) {
            ItemBuilder itemBuilder = new ItemBuilder(Material.STAINED_GLASS_PANE);

            itemBuilder.setDurability(team.getTeamType().getMetaID());
            itemBuilder.setDisplayName(team.getTeamType().getTeamColor() + team.getTeamType().name());
            itemBuilder.setLore("&7Click to join the " + team.getTeamType().getTeamColor() + team.getTeamType().name() + " &7Team!", "", "&7Current players (" + team.getTeamMembers().size() + "/" + team.getMaxTeamSize() + ")");

            if (team.getTeamMembers().isEmpty()) {
                itemBuilder.addLore("&cN/A");
            }

            for (GamePlayer gamePlayer : team.getTeamMembers()) {
                Player player = this.instance.getServer().getPlayer(gamePlayer.getUniqueID());
                if (player == null) return;

                User user = this.instance.getUserManager().getUser(player.getUniqueId());

                itemBuilder.addLore(user.getRank().getPrefix() + player.getName());
            }

            itemBuilder.addLore("");
            itemBuilder.addLore("&eClick to join!");

            inventory.setItem(
                    counter.getAndIncrement(),
                    itemBuilder.build()
            );
        }
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        String selectedTeamName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
        Team selectedTeam = this.bedwarsGame.getTeamManager().getTeam(selectedTeamName);

        if (selectedTeam == null) return;

        if (selectedTeam.getTeamMembers().size() >= 2) {
            player.sendMessage(Chat.colorize("&cThis team is full!"));
            return;
        }

        GamePlayer gamePlayer = bedwarsGame.getGamePlayerManager().getGamePlayer(player.getUniqueId());
        if (gamePlayer.getTeam() != null) {
            gamePlayer.getTeam().removeFromTeam(gamePlayer);
        }

        selectedTeam.addToTeam(gamePlayer);
        gamePlayer.setTeam(selectedTeam);

        player.sendMessage(Chat.colorize("&aYou joined the " + selectedTeam.getTeamType().name() + " team!"));
        this.bedwarsGame.getTeamSelectMenu().open(player);
    }
}
