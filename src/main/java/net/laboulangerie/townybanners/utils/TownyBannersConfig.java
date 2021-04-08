package net.laboulangerie.townybanners.utils;

import net.laboulangerie.townybanners.TownyBanners;
import org.bukkit.configuration.file.FileConfiguration;

public class TownyBannersConfig {

    private TownyBanners townyBanners;

    private boolean showPopup;

    private String consoleTryCommand;
    private String townBannerSaved;
    private String playerDoesNotBelongToATown;
    private String playerHasNoBannerInHand;
    private String nationBannerSaved;
    private String townDoesNotBelongToANation;

    private String town;
    private String townColor;
    private String nation;
    private String nationColor;

    public TownyBannersConfig(TownyBanners townyBanners) {
        this.townyBanners = townyBanners;
        FileConfiguration configuration = this.townyBanners.getConfig();

        this.showPopup = configuration.getBoolean("advancementPopUp");

        this.consoleTryCommand = configuration.getString("messages.command.consoleTryCommand");
        this.townBannerSaved = configuration.getString("messages.command.townBannerSaved");
        this.playerDoesNotBelongToATown = configuration.getString("messages.command.playerDoesNotBelongToATown");
        this.playerHasNoBannerInHand =  configuration.getString("messages.command.playerHasNoBannerInHand");
        this.nationBannerSaved = configuration.getString("messages.command.nationBannerSaved");
        this.townDoesNotBelongToANation = configuration.getString("messages.command.townDoesNotBelongToANation");

        this.town = configuration.getString("entering.town");
        this.townColor = configuration.getString("entering.townColor");
        this.nation = configuration.getString("entering.nation");
        this.nationColor = configuration.getString("entering.nationColor");
    }

    public boolean isPoppingOut() {
        return showPopup;
    }

    public String getConsoleTryCommand() {
        return consoleTryCommand;
    }

    public String getTownBannerSaved(String townName) {
        return townBannerSaved.replace("+townName", townName);
    }

    public String getPlayerDoesNotBelongToATown() {
        return playerDoesNotBelongToATown;
    }

    public String getPlayerHasNoBannerInHand() {
        return playerHasNoBannerInHand;
    }

    public String getNationBannerSaved(String nationName) {
        return nationBannerSaved.replace("+nationName", nationName);
    }

    public String getTownDoesNotBelongToANation() {
        return townDoesNotBelongToANation;
    }

    public String enteringTown(String townName) {
        return town.replace("+townName", townName);
    }

    public String getTownColor() {
        return townColor;
    }

    public String enteringNation(String nationName) {
        return nation.replace("+nationName", nationName);
    }

    public String getNationColor() {
        return nationColor;
    }
}
