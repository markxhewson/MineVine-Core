package xyz.lotho.me.minevine.general.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.me.minevine.games.Game;
import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.general.enums.GameType;
import xyz.lotho.me.minevine.general.enums.UserRank;
import xyz.lotho.me.minevine.general.managers.user.User;
import xyz.lotho.me.minevine.general.util.Chat;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class StartGameCommand implements CommandExecutor {

    private final Minevine instance;

    public StartGameCommand(Minevine instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        User user = this.instance.getUserManager().getUser(player.getUniqueId());

        if (!user.getRank().isStaff()) {
            player.sendMessage(Chat.colorize("&cYou need to be " + UserRank.MOD.name() + " or higher to use this!"));
            return false;
        }

        GameType gameType = this.instance.getQueueManager().getQueuedGame(player.getUniqueId());
        if (gameType == null) {
            player.sendMessage(Chat.colorize("&cYou are not currently queued for a game!"));
            return false;
        }

        if (gameType == GameType.BEDWARS) {
            ArrayList<UUID> players = (ArrayList<UUID>) this.instance.getQueueManager().getGameQueue(GameType.BEDWARS).stream().limit(16).collect(Collectors.toList());
            players.forEach(uuid -> this.instance.getQueueManager().deQueue(uuid));

            Game game = new BedwarsGame(this.instance, UUID.randomUUID(), "Lighthouse", players);
            this.instance.getGameManager().addGame(game);

            player.sendMessage(Chat.colorize("&aStarting &eBedwars &agame.."));
        }


        return true;
    }
}
