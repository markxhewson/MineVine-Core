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

public class EndGameCommand implements CommandExecutor {

    private final Minevine instance;

    public EndGameCommand(Minevine instance) {
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

        Game game = this.instance.getGameManager().findGameByPlayer(player.getUniqueId());
        if (game == null) {
            player.sendMessage(Chat.colorize("&cYou are not in a game!"));
            return false;
        }

        if (game.getType() == GameType.BEDWARS) {
            BedwarsGame bedwarsGame = (BedwarsGame) game;
            bedwarsGame.announce("&cAn administrator has forcefully ended the game..");

            this.instance.getServer().getScheduler().runTaskLater(this.instance, bedwarsGame::endGame, 60L);
        }

        return true;
    }
}
