package xyz.lotho.me.minevine.games.bedwars.scoreboard;

import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.games.bedwars.player.GamePlayer;
import xyz.lotho.me.minevine.general.managers.user.User;
import xyz.lotho.me.minevine.plugin.Minevine;

public class LobbyBoard {

    private final Minevine instance;
    private final BedwarsGame bedwarsGame;
    private GamePlayer gamePlayer;

    public LobbyBoard(Minevine instance, BedwarsGame game, GamePlayer gamePlayer) {
        this.instance = instance;
        this.bedwarsGame = game;
        this.gamePlayer = gamePlayer;

        createBoard();
    }

    public void createBoard() {
        User user = this.instance.getUserManager().getUser(gamePlayer.getUniqueID());

        user.getScoreboard().setLines(gamePlayer.getPlayer(),
                "&7&m--------------------",
                "State: &a" + bedwarsGame.getGameState().name(),
                "",
                "&fPlayers: &e" + bedwarsGame.getGamePlayers().size() + "/16",
                "&fMap: &a" + bedwarsGame.getMapName(),
                "",
                "&7play.minevine.org",
                "&7&m--------------------"
        );
    }
}
