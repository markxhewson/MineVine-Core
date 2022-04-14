package xyz.lotho.me.minevine.general.tab;

import dev.jcsoftware.jscoreboards.JPerPlayerScoreboard;
import dev.jcsoftware.jscoreboards.JScoreboardTeam;
import xyz.lotho.me.minevine.general.enums.UserRank;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.HashMap;
import java.util.Map;

public class TabRankManager {

    private final Minevine instance;
    private final Map<UserRank, JScoreboardTeam> rankTeams = new HashMap<>();

    public TabRankManager(Minevine instance) {
        this.instance = instance;
    }

    public void load(JPerPlayerScoreboard scoreboard) {
        int counter = 0;

        for (UserRank value : UserRank.values()) {
            JScoreboardTeam team = scoreboard.createTeam(counter + "_" + value.name(), value.getPrefix());
            team.getScoreboard().getOptions().setShowHealthUnderName(true);

            this.addRankTeam(value, team);
            counter++;
        }
    }

    public JScoreboardTeam getRankTeam(UserRank rankType) {
        return this.getRankTeams().getOrDefault(rankType, null);
    }

    public Map<UserRank, JScoreboardTeam> getRankTeams() {
        return rankTeams;
    }

    public void addRankTeam(UserRank rankType, JScoreboardTeam team) {
        this.getRankTeams().put(rankType, team);
    }
}
