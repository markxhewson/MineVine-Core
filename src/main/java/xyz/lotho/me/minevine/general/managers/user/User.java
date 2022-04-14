package xyz.lotho.me.minevine.general.managers.user;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.jcsoftware.jscoreboards.JPerPlayerMethodBasedScoreboard;
import dev.jcsoftware.jscoreboards.JScoreboardTeam;
import org.bson.Document;
import org.bukkit.entity.Player;
import xyz.lotho.me.minevine.general.enums.UserRank;
import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.UUID;

public class User {

    private final Minevine instance;

    private final UUID uuid;

    private int id;
    private UserRank rank;
    private long lastLogin;
    private long firstLogin;

    private JPerPlayerMethodBasedScoreboard scoreboard;

    private boolean loaded = false;

    public User(Minevine instance, UUID uuid) {
        this.instance = instance;
        this.uuid = uuid;
    }

    public void load() {
        this.instance.getMongoManager().getUsersCollection().find(Filters.eq("uuid", this.uuid.toString())).first((document, throwable) -> {
            if (document == null || document.isEmpty()) {
                this.instance.getMongoManager().getUsersCollection().countDocuments(((documents, t) -> {
                    Document insertDocument = new Document();

                    insertDocument.append("_id", documents.intValue() + 1);
                    insertDocument.append("uuid", this.getUniqueID().toString());
                    insertDocument.append("rank", UserRank.MEMBER.name());
                    insertDocument.append("lastLogin", System.currentTimeMillis());
                    insertDocument.append("firstLogin", System.currentTimeMillis());

                    this.instance.getMongoManager().getUsersCollection().replaceOne(Filters.eq("uuid", uuid), insertDocument, new UpdateOptions().upsert(true), (result, error) -> {});
                    this.instance.getServer().getScheduler().runTaskLaterAsynchronously(this.instance, this::load, 10L);
                }));
            } else {
                this.setId(document.getInteger("_id"));
                this.setRank(UserRank.valueOf(document.getString("rank")));
                this.setLastLogin(System.currentTimeMillis());
                this.setFirstLogin(document.getLong("firstLogin"));

                this.setLoaded(true);
                this.instance.getServer().getScheduler().runTaskLater(this.instance, this::setLobbyBoard, 5L);
            }
        });
    }

    public void save() {
        Document saveDocument = new Document();

        saveDocument.append("_id", this.getId());
        saveDocument.append("uuid", this.getUniqueID().toString());
        saveDocument.append("rank", this.getRank().name());
        saveDocument.append("lastLogin", this.getLastLogin());
        saveDocument.append("firstLogin", this.getFirstLogin());

        this.instance.getMongoManager().getUsersCollection().replaceOne(Filters.eq("uuid", this.getUniqueID().toString()), saveDocument, new UpdateOptions().upsert(true), (result, error) -> {});
    }

    public Player getPlayer() {
        return this.instance.getServer().getPlayer(this.getUniqueID());
    }

    public void setLobbyBoard() {
        this.instance.getLobbyBoard().addToBoard(this.getPlayer());
    }

    public UUID getUniqueID() {
        return this.uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserRank getRank() {
        return rank;
    }

    public void setRank(UserRank rank) {
        UserRank previousRank = this.getRank();
        if (previousRank != null) {
            JScoreboardTeam oldTeam = this.instance.getTabRankManager().getRankTeam(previousRank);
            oldTeam.removePlayer(this.getPlayer());
        }

        JScoreboardTeam team = this.instance.getTabRankManager().getRankTeam(rank);
        team.addPlayer(this.getPlayer());

        this.rank = rank;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public long getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(long firstLogin) {
        this.firstLogin = firstLogin;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public JPerPlayerMethodBasedScoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(JPerPlayerMethodBasedScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }
}
