package xyz.lotho.me.minevine.games.bedwars.generator;

import org.bukkit.Material;

public enum GeneratorType {

    EMERALD(Material.EMERALD),
    DIAMOND(Material.DIAMOND),
    GOLD(Material.GOLD_INGOT),
    IRON(Material.IRON_INGOT);

    private final Material materialType;

    GeneratorType(Material materialType) {
        this.materialType = materialType;
    }

    public Material getMaterialType() {
        return this.materialType;
    }

}
