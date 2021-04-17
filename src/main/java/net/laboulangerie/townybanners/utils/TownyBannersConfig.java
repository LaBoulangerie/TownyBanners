package net.laboulangerie.townybanners.utils;

import net.laboulangerie.townybanners.TownyBanners;
import org.bukkit.configuration.file.FileConfiguration;

public class TownyBannersConfig {

    private TownyBanners townyBanners;

    private boolean popup;

    private Long townCooldown;
    private Long nationCooldown;

    private String days;
    private String day;
    private String hours;
    private String hour;
    private String minutes;
    private String minute;
    private String seconds;
    private String second;

    private String consoleTryCommand;
    private String townBannerSaved;
    private String playerNotInTown;
    private String playerBannerNotInHand;
    private String nationBannerSaved;
    private String townNotInNation;
    private String playerPermsMissing;
    private String townInCooldown;
    private String nationInCooldown;

    private String enteringTown;
    private String enteringTownColor;
    private String enteringNation;
    private String enteringNationColor;

    public TownyBannersConfig(TownyBanners townyBanners) {
        this.townyBanners = townyBanners;
        FileConfiguration configuration = this.townyBanners.getConfig();

        this.popup = configuration.getBoolean("popup");

        this.townCooldown = configuration.getLong("town_cooldown");
        this.nationCooldown = configuration.getLong("nation_cooldown");

        this.days = configuration.getString("lang.days");
        this.day = configuration.getString("lang.day");
        this.hours = configuration.getString("lang.hours");
        this.hour = configuration.getString("lang.hour");
        this.minutes = configuration.getString("lang.minutes");
        this.minute = configuration.getString("lang.minute");
        this.seconds = configuration.getString("lang.seconds");
        this.second = configuration.getString("lang.second");

        this.consoleTryCommand = configuration.getString("lang.console_try_command");
        this.townBannerSaved = configuration.getString("lang.town_banner_saved");
        this.playerNotInTown = configuration.getString("lang.player_not_in_town");
        this.playerBannerNotInHand = configuration.getString("lang.player_banner_not_in_hand");
        this.nationBannerSaved = configuration.getString("lang.nation_banner_saved");
        this.townNotInNation = configuration.getString("lang.town_not_in_nation");
        this.playerPermsMissing = configuration.getString("lang.player_perms_missing");
        this.townInCooldown = configuration.getString("lang.town_in_cooldown");
        this.nationInCooldown = configuration.getString("lang.nation_in_cooldown");

        this.enteringTown = configuration.getString("lang.entering.town");
        this.enteringTownColor = configuration.getString("lang.entering.town_color");
        this.enteringNation = configuration.getString("lang.entering.nation");
        this.enteringNationColor = configuration.getString("lang.entering.nation_color");
    }

    public boolean isPoppingOut() {
        return popup;
    }

    public long getTownCooldown() {
        return townCooldown;
    }

    public long getNationCooldown() {
        return nationCooldown;
    }

    public String getConsoleTryCommand() {
        return consoleTryCommand;
    }

    public String getTownBannerSaved(String townName) {
        return townBannerSaved.replace("%town_name%", townName);
    }

    public String getPlayerNotInTown() {
        return playerNotInTown;
    }

    public String getPlayerBannerNotInHand() {
        return playerBannerNotInHand;
    }

    public String getNationBannerSaved(String nationName) {
        return nationBannerSaved.replace("%nation_name%", nationName);
    }

    public String getTownNotInNation() {
        return townNotInNation;
    }
    
    public String getTownInCooldown(Long cooldown) {
        return townInCooldown.replace("%cooldown_left%", CooldownUtils.getHumanReadableTime(cooldown, days, day, hours, hour, minutes, minute, seconds, second));
    }

    public String getNationInCooldown(Long cooldown) {
        return nationInCooldown.replace("%cooldown_left%", CooldownUtils.getHumanReadableTime(cooldown, days, day, hours, hour, minutes, minute, seconds, second));
    }

    public String getEnteringTown(String townName) {
        return enteringTown.replace("%town_name%", townName);
    }

    public String getEnteringTownColor() {
        return enteringTownColor;
    }

    public String getEnteringNation(String nationName) {
        return enteringNation.replace("%nation_name%", nationName);
    }

    public String getEnteringNationColor() {
        return enteringNationColor;
    }
}

