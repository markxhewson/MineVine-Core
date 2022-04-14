package xyz.lotho.me.minevine.general.enums;

public enum UserRank {

    OWNER("Owner", "&c&l[Owner]&r &c", true, true, false),
    ADMIN("Admin", "&a&l[Admin]&r &a", true, true, false),
    DEV("Dev", "&d&l[Dev]&r &d", true, true, false),
    MOD("Mod", "&b&l[Mod]&r &b", true, true, false),
    MEMBER("Member", "&7[Member]&r &7", false, false, true);

    private final String displayName;
    private final String prefix;
    private final boolean isStaff;
    private final boolean isDonator;
    private final boolean defaultRank;

    UserRank(String displayName, String prefix, boolean isStaff, boolean isDonator, boolean defaultRank) {
        this.displayName = displayName;
        this.prefix = prefix;
        this.isStaff = isStaff;
        this.isDonator = isDonator;
        this.defaultRank = defaultRank;
    }

    public boolean has(UserRank rank){
        return compareTo(rank) <= 0;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public boolean isDonator() {
        return isDonator;
    }

    public boolean isDefaultRank() {
        return defaultRank;
    }
}
