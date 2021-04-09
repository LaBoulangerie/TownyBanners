package net.laboulangerie.townybanners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.db.TownyDataSource;
import net.laboulangerie.townybanners.advancement.BannerAdvancement;
import net.laboulangerie.townybanners.commands.NationBannerCommand;
import net.laboulangerie.townybanners.commands.TownBannerCommand;
import net.laboulangerie.townybanners.commands.TownyBannersCommand;
import net.laboulangerie.townybanners.listeners.BannersListener;
import net.laboulangerie.townybanners.utils.TownyBannersConfig;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

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


        this.getServer().getPluginManager().registerEvents(new BannersListener(this), this);

        this.bannerAdvancement.registerAdvancements();
        this.getServer().getConsoleSender().sendMessage(BANNER_TAG + "Ready!");

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

    public void reloadTownyBannerConfig() {
        this.reloadConfig();
        this.config = new TownyBannersConfig(this);
    }
}
