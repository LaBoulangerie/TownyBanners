package net.laboulangerie.townybanners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.db.TownyDataSource;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import net.laboulangerie.townybanners.advancement.BannerAdvancementType;
import net.laboulangerie.townybanners.advancement.BannerAdvancement;
import net.laboulangerie.townybanners.commands.NationBannerCommand;
import net.laboulangerie.townybanners.commands.TownBannerCommand;
import net.laboulangerie.townybanners.commands.TownyBannersCommand;
import net.laboulangerie.townybanners.listeners.BannersListener;
import net.laboulangerie.townybanners.utils.ItemUtils;
import net.laboulangerie.townybanners.utils.TownyBannersConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public class TownyBanners extends JavaPlugin {

    public final static String BANNER_TAG = ChatColor.GOLD + "[TownyBanners] ";

    private TownyBannersConfig config;

    private Gson gson;
    private TownyAPI townyAPI;
    private TownyDataSource townyDataSource;
    private BannerAdvancement bannerAdvancement;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        this.config = new TownyBannersConfig(this);

        this.townyAPI = TownyAPI.getInstance();
        this.townyDataSource = this.townyAPI.getDataSource();
        this.bannerAdvancement = new BannerAdvancement(this);

        this.getCommand("tbanner").setExecutor(new TownBannerCommand(this));
        this.getCommand("nbanner").setExecutor(new NationBannerCommand(this));
        this.getCommand("townybanners").setExecutor(new TownyBannersCommand(this));


        getServer().getPluginManager().registerEvents(new BannersListener(this), this);
        getServer().getConsoleSender().sendMessage(BANNER_TAG + "Ready!");

        Collection<Town> towns = this.townyDataSource.getTowns();
        if (this.config.isPoppingOut()) {

            for (Town town : towns) {

                if (town.hasMeta("banner")) {
                    StringDataField townBannerField = (StringDataField) town.getMetadata("banner");
                    ItemStack townBanner = ItemUtils.stringToItem(townBannerField.getValue());

                    String townName = town.getName();
                    this.bannerAdvancement.loadAdvancement(BannerAdvancementType.TOWN, townBanner, townName);
                }

                if (town.hasNation()) {
                    try {
                        Nation nation = town.getNation();
                        if (nation.hasMeta("banner")) {

                            StringDataField nationBannerField = (StringDataField) nation.getMetadata("banner");
                            ItemStack nationBanner = ItemUtils.stringToItem(nationBannerField.getValue());

                            String nationName = nation.getName();
                            this.bannerAdvancement.loadAdvancement(BannerAdvancementType.TOWN, nationBanner, nationName);
                        }
                    } catch (NotRegisteredException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(BANNER_TAG + "Bye!");

    }


    public Gson getGson() {
        return gson;
    }

    public TownyAPI getTownyAPI() {
        return townyAPI;
    }

    public TownyDataSource getTownyDataSource() {
        return townyDataSource;
    }

    public BannerAdvancement getBannerAdvancement() {
        return bannerAdvancement;
    }

    public TownyBannersConfig getTownyBannerConfig() {
        return this.config;
    }
}
