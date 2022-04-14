package xyz.lotho.me.minevine.general.managers.scoreboard;

import dev.jcsoftware.jscoreboards.JPerPlayerMethodBasedScoreboard;
import dev.jcsoftware.jscoreboards.JScoreboardTeam;
import org.bukkit.entity.Player;
import xyz.lotho.me.minevine.general.managers.user.User;
import xyz.lotho.me.minevine.general.enums.GameType;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class LobbyBoard {

    private final Minevine instance;
    private JPerPlayerMethodBasedScoreboard scoreboard;

    public LobbyBoard(Minevine instance) {
        this.instance = instance;

        initBoard();
    }

    public void initBoard() {
        this.scoreboard = new JPerPlayerMethodBasedScoreboard();
        this.instance.getTabRankManager().load(scoreboard);
    }

    public void addToBoard(Player player) {
        User user = this.instance.getUserManager().getUser(player.getUniqueId());
        JScoreboardTeam team = this.instance.getTabRankManager().getRankTeam(user.getRank());

        this.scoreboard.setTitle(player, "&6&lMineVine");
        this.scoreboard.addPlayer(player);
        team.addPlayer(player);

        user.setScoreboard(this.scoreboard);
        this.updateBoard(player);
    }

    public void updateBoard(Player player) {
        User user = this.instance.getUserManager().getUser(player.getUniqueId());

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
        String formattedDate = formatter.format(LocalDate.now());

        if (this.instance.getGameManager().findGameByPlayer(player.getUniqueId()) != null) return;

        if (this.instance.getQueueManager().isPresent(player.getUniqueId())) {
            GameType queuedGame = this.instance.getQueueManager().getQueuedGame(player.getUniqueId());

            user.getScoreboard().setLines(player,
                    "&7&m--------------------",
                    "&7" + formattedDate,
                    "",
                    "&6&lYou",
                    "  &fName: &e" + player.getName(),
                    "  &fRank: " + user.getRank().getPrefix(),
                    "",
                    "&6&lQueue",
                    "  &fGame: &a" + queuedGame.name(),
                    "  &fQueued: &e" + this.instance.getQueueManager().getGameQueue(queuedGame).size(),
                    "",
                    "&7play.minevine.org",
                    "&7&m--------------------"
            );
            return;
        }

        user.getScoreboard().setLines(player,
                "&7&m--------------------",
                "&7" + formattedDate,
                "",
                "&6&lYou",
                "  &fName: &e" + player.getName(),
                "  &fRank: " + user.getRank().getPrefix(),
                "",
                "&7play.minevine.org",
                "&7&m--------------------"
        );
    }
}
