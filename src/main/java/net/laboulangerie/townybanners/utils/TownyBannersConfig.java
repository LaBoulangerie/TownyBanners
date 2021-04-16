package net.laboulangerie.townybanners.utils;

import net.laboulangerie.townybanners.TownyBanners;
import org.bukkit.configuration.file.FileConfiguration;

public class TownyBannersConfig {

    private TownyBanners townyBanners;

    private boolean showPopup;

    private long townCooldown;
    private long nationCooldown;

    private String days;
    private String hours;
    private String minutes;
    private String seconds;

    private String consoleTryCommand;
    private String townBannerSaved;
    private String playerDoesNotBelongToATown;
    private String playerHasNoBannerInHand;
    private String nationBannerSaved;
    private String townDoesNotBelongToANation;
    private String townInCooldown;
    private String nationInCooldown;

    private String town;
    private String townColor;
    private String nation;
    private String nationColor;

    public TownyBannersConfig(TownyBanners townyBanners) {
        this.townyBanners = townyBanners;
        FileConfiguration configuration = this.townyBanners.getConfig();

        this.showPopup = configuration.getBoolean("advancementPopUp");

        this.townCooldown = configuration.getLong("townCooldown");
        this.nationCooldown = configuration.getLong("nationCooldown");

        this.days = configuration.getString("days");
        this.hours = configuration.getString("hours");
        this.minutes = configuration.getString("minutes");
        this.seconds = configuration.getString("seconds");

        this.consoleTryCommand = configuration.getString("messages.command.consoleTryCommand");
        this.townBannerSaved = configuration.getString("messages.command.townBannerSaved");
        this.playerDoesNotBelongToATown = configuration.getString("messages.command.playerDoesNotBelongToATown");
        this.playerHasNoBannerInHand =  configuration.getString("messages.command.playerHasNoBannerInHand");
        this.nationBannerSaved = configuration.getString("messages.command.nationBannerSaved");
        this.townDoesNotBelongToANation = configuration.getString("messages.command.townDoesNotBelongToANation");
        this.townInCooldown = configuration.getString("messages.command.townInCooldown");
        this.nationInCooldown = configuration.getString("messages.command.nationInCooldown");

        this.town = configuration.getString("messages.entering.town");
        this.townColor = configuration.getString("messages.entering.townColor");
        this.nation = configuration.getString("messages.entering.nation");
        this.nationColor = configuration.getString("messages.entering.nationColor");
    }

    public boolean isPoppingOut() {
        return showPopup;
    }

    public long getTownCooldown() { return townCooldown; }

    public long getNationCooldown() { return nationCooldown; }

    public String getConsoleTryCommand() {
        return consoleTryCommand;
    }

    public String getTownBannerSaved(String townName) {
        return townBannerSaved.replace("%townName%", townName);
    }

    public String getPlayerDoesNotBelongToATown() {
        return playerDoesNotBelongToATown;
    }

    public String getPlayerHasNoBannerInHand() {
        return playerHasNoBannerInHand;
    }

    public String getNationBannerSaved(String nationName) {
        return nationBannerSaved.replace("%nationName%", nationName);
    }

    public String getTownDoesNotBelongToANation() {
        return townDoesNotBelongToANation;
    }

    public String getTownInCooldown(long cooldown) {
        return townInCooldown.replace("%cooldownLeft%", CooldownUtils.getHumanReadableTime(cooldown, days, hours, minutes, seconds));
    }

    public String getNationInCooldown(long cooldown) {
        return nationInCooldown.replace("%cooldownLeft%", CooldownUtils.getHumanReadableTime(cooldown, days, hours, minutes, seconds));
    }

    public String enteringTown(String townName) {
        return town.replace("%townName%", townName);
    }

    public String getTownColor() {
        return townColor;
    }

    public String enteringNation(String nationName) {
        return nation.replace("%nationName%", nationName);
    }

    public String getNationColor() {
        return nationColor;
    }
}
