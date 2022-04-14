package xyz.lotho.me.minevine.general.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.lotho.me.minevine.general.enums.UserRank;
import xyz.lotho.me.minevine.general.managers.user.User;
import xyz.lotho.me.minevine.general.util.Chat;
import xyz.lotho.me.minevine.plugin.Minevine;

public class SetRankCommand implements CommandExecutor {

    private final Minevine instance;

    public SetRankCommand(Minevine instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            this.setRank(sender, args[0], args[1]);
            return true;
        }

        final Player player = (Player) sender;
        final User user = this.instance.getUserManager().getUser(player.getUniqueId());

        if (!user.getRank().has(UserRank.ADMIN)) {
            player.sendMessage(Chat.colorize("&cYou must be " + UserRank.ADMIN.name() + " or higher to use this!"));
            return false;
        }

        if (args.length == 2) {
            this.setRank(sender, args[0], args[1]);
        } else {
            sender.sendMessage(Chat.colorize("&cInvalid Usage! &7Usage: /setrank <user> <rank>"));
            return false;
        }

        return true;
    }

    public void setRank(CommandSender sender, String targetName, String rankName) {
        final Player target = this.instance.getServer().getPlayer(targetName);

        if (target == null) {
            sender.sendMessage(Chat.colorize("&cCannot modify rank of player that is offline."));
            return;
        }

        final User targetUser = this.instance.getUserManager().getUser(target.getUniqueId());
        UserRank rankType;

        try {
            rankType = UserRank.valueOf(rankName.toUpperCase());
        } catch (IllegalArgumentException exception) {
            rankType = UserRank.MEMBER;
        }

        targetUser.setRank(rankType);

        sender.sendMessage(Chat.colorize("&aYou have set " + target.getName() + "'s rank to " + rankType.getDisplayName()));
        if (!sender.getName().equals(target.getName())) target.sendMessage(Chat.colorize("&aYour rank has been updated to " + rankType.getDisplayName()));
    }
}
