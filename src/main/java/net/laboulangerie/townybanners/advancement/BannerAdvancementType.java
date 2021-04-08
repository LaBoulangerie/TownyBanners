package net.laboulangerie.townybanners.advancement;

public enum BannerAdvancementType {
    TOWN("towny_banners_town_"),
    NATION("towny_banners_nation_");
    private String keyPrefix;

    BannerAdvancementType(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getKey() {
        return this.keyPrefix;
    }

    public String getKey(String name) {
        return this.keyPrefix+name;
    }

}
