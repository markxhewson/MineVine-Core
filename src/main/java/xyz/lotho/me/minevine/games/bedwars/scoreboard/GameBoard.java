package xyz.lotho.me.minevine.games.bedwars.scoreboard;

import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.games.bedwars.player.GamePlayer;
import xyz.lotho.me.minevine.games.bedwars.team.Team;
import xyz.lotho.me.minevine.games.bedwars.team.TeamManager;
import xyz.lotho.me.minevine.general.managers.user.User;
import xyz.lotho.me.minevine.plugin.Minevine;

public class GameBoard {

    private final Minevine instance;
    private final BedwarsGame bedwarsGame;
    private GamePlayer gamePlayer;

    public GameBoard(Minevine instance, BedwarsGame game, GamePlayer gamePlayer) {
        this.instance = instance;
        this.bedwarsGame = game;
        this.gamePlayer = gamePlayer;

        createBoard();
    }

    public void createBoard() {
        User user = this.instance.getUserManager().getUser(gamePlayer.getUniqueID());

        TeamManager teamManager = bedwarsGame.getTeamManager();

        String bedAlive = "&a✓";
        String teamDead = "&c✗";

        Team redTeam = teamManager.getTeam("RED");
        Team blueTeam = teamManager.getTeam("BLUE");
        Team greenTeam = teamManager.getTeam("GREEN");
        Team yellowTeam = teamManager.getTeam("YELLOW");
        Team aquaTeam = teamManager.getTeam("AQUA");
        Team whiteTeam = teamManager.getTeam("WHITE");
        Team pinkTeam = teamManager.getTeam("PINK");
        Team grayTeam = teamManager.getTeam("GRAY");

        user.getScoreboard().setLines(gamePlayer.getPlayer(),
                "&7&m----------------",
                "&fState: &a" + bedwarsGame.getGameState().name(),
                "",
                "&c" + redTeam.getTeamName().charAt(0) + " &fRed: " + (redTeam.isBedBroken() ? (redTeam.getAliveMembers().size() > 0 ? "&a" + redTeam.getAliveMembers().size() : teamDead) : bedAlive),
                "&9" + blueTeam.getTeamName().charAt(0) + " &fBlue: " + (blueTeam.isBedBroken() ? (blueTeam.getAliveMembers().size() > 0 ? "&a" + blueTeam.getAliveMembers().size() : teamDead) : bedAlive),
                "&a" + greenTeam.getTeamName().charAt(0) + " &fGreen: " + (greenTeam.isBedBroken() ? (greenTeam.getAliveMembers().size() > 0 ? "&a" + greenTeam.getAliveMembers().size() : teamDead) : bedAlive),
                "&e" + yellowTeam.getTeamName().charAt(0) + " &fYellow: " + (yellowTeam.isBedBroken() ? (yellowTeam.getAliveMembers().size() > 0 ? "&a" + yellowTeam.getAliveMembers().size() : teamDead) : bedAlive),
                "&b" + aquaTeam.getTeamName().charAt(0) + " &fAqua: " + (aquaTeam.isBedBroken() ? (aquaTeam.getAliveMembers().size() > 0 ? "&a" + aquaTeam.getAliveMembers().size() : teamDead) : bedAlive),
                "&f" + whiteTeam.getTeamName().charAt(0) + " &fWhite: " + (whiteTeam.isBedBroken() ? (whiteTeam.getAliveMembers().size() > 0 ? "&a" + whiteTeam.getAliveMembers().size() : teamDead) : bedAlive),
                "&d" + pinkTeam.getTeamName().charAt(0) + " &fPink: " + (pinkTeam.isBedBroken() ? (pinkTeam.getAliveMembers().size() > 0 ? "&a" + pinkTeam.getAliveMembers().size() : teamDead) : bedAlive),
                "&8" + grayTeam.getTeamName().charAt(0) + " &fGray: " + (grayTeam.isBedBroken() ? (grayTeam.getAliveMembers().size() > 0 ? "&a" + grayTeam.getAliveMembers().size() : teamDead) : bedAlive),
                "",
                "&7play.minevine.org",
                "&7&m----------------"
        );
    }

}
