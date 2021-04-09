package net.laboulangerie.townybanners.advancement;

import org.bukkit.NamespacedKey;

public enum Keys {
    TOWN("towny_banners_town_"),
    NATION("towny_banners_nation_");
    private String keyPrefix;

    Keys(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getKeyString() {
        return this.keyPrefix;
    }

    public String getKeyString(String name) {
        return this.keyPrefix+name.toLowerCase();
    }

    public NamespacedKey getKey(String name) {
        return NamespacedKey.minecraft(this.getKeyString(name));
    }
}
