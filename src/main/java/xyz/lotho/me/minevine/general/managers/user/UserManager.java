package xyz.lotho.me.minevine.general.managers.user;

import xyz.lotho.me.minevine.plugin.Minevine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserManager {

    private final Minevine instance;
    private final Map<UUID, User> usersMap = new HashMap<>();

    public UserManager(Minevine instance) {
        this.instance = instance;
    }

    public Map<UUID, User> getUsersMap() {
        return this.usersMap;
    }

    public User getUser(UUID uuid) {
        return this.getUsersMap().getOrDefault(uuid, null);
    }

    public void addUser(UUID uuid) {
        this.getUsersMap().put(uuid, new User(this.instance, uuid));
    }

    public void removeUser(UUID uuid) {
        this.getUsersMap().remove(uuid);
    }
}
