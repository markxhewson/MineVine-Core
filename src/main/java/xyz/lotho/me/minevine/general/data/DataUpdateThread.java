package xyz.lotho.me.minevine.general.data;

import org.bukkit.entity.Player;
import xyz.lotho.me.minevine.general.managers.user.User;
import xyz.lotho.me.minevine.plugin.Minevine;

public class DataUpdateThread extends Thread {

    private final Minevine instance;

    public DataUpdateThread(Minevine instance) {
        this.instance = instance;

        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                check();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                sleep(1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    public void check() {
        this.instance.getTabManager().sendTablist();

        for (Player onlinePlayer : this.instance.getServer().getOnlinePlayers()) {
            User user = this.instance.getUserManager().getUser(onlinePlayer.getUniqueId());

            if (user.getScoreboard() == null) return;
            this.instance.getLobbyBoard().updateBoard(onlinePlayer);
        }
    }
}
