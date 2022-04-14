package xyz.lotho.me.minevine.games.bedwars.team;

import xyz.lotho.me.minevine.games.bedwars.game.BedwarsGame;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.ArrayList;

public class TeamManager {

    private final Minevine instance;
    private final BedwarsGame bedwarsGame;

    private final ArrayList<Team> teams = new ArrayList<>();

    public TeamManager(Minevine instance, BedwarsGame bedwarsGame) {
        this.instance = instance;
        this.bedwarsGame = bedwarsGame;
    }

    public ArrayList<Team> getTeams() {
        return this.teams;
    }

    public void addTeam(Team team) {
        this.getTeams().add(team);
    }

    public void removeTeam(Team team) {
        this.getTeams().remove(team);
    }

    public Team getWinningTeam() {
        Team winningTeam = null;

        for (Team team : this.getTeams()) {
            if (team.getAliveMembers().size() != 0) {
                winningTeam = team;
            }
        }

        return winningTeam;
    }

    public ArrayList<Team> getAliveTeams() {
        ArrayList<Team> aliveTeams = new ArrayList<>();

        for (Team team : this.getTeams()) {
            if (team.getAliveMembers().size() != 0) {
                aliveTeams.add(team);
            }
        }

        return aliveTeams;
    }

    public Team getTeam(String teamID) {
        for (Team team : this.getTeams()) {
            if (team.getTeamType().name().equals(teamID)) {
                return team;
            }
        }

        return null;
    }
}
