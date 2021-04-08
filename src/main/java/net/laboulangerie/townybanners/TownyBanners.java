package net.laboulangerie.townybanners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.db.TownyDataSource;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import net.laboulangerie.townybanners.advancement.BannerAdvancement;
import net.laboulangerie.townybanners.commands.BannersCommands;
import net.laboulangerie.townybanners.listeners.BannersListener;
import net.laboulangerie.townybanners.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public class TownyBanners extends JavaPlugin {

    public String townyBannersTag = ChatColor.GOLD + "[TownyBanners] ";
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

        this.townyAPI = TownyAPI.getInstance();
        this.townyDataSource = this.townyAPI.getDataSource();
        this.bannerAdvancement = new BannerAdvancement(this);

        BannersCommands commands = new BannersCommands(this);

        this.getCommand("tbanner").setExecutor(commands);
        this.getCommand("nbanner").setExecutor(commands);
        this.getCommand("townybanners").setExecutor(commands);


        getServer().getPluginManager().registerEvents(new BannersListener(this), this);
        getServer().getConsoleSender().sendMessage(townyBannersTag + "Ready!");

        Collection<Town> towns = this.townyDataSource.getTowns();
        if (this.getConfig().getBoolean("advancementPopUp")) {

            for (Town town : towns) {

                if (town.hasMeta("banner")) {
                    StringDataField townBannerField = (StringDataField) town.getMetadata("banner");
                    ItemStack townBanner = ItemUtils.stringToItem(townBannerField.getValue());

                    String townName = town.getName();
                    String townAdvancementPrefix = "towny_banners_town_";

                    try {
                        Bukkit.getUnsafe().removeAdvancement(NamespacedKey.minecraft(townAdvancementPrefix + townName.toLowerCase()));
                        Bukkit.getServer().reloadData();
                        Bukkit.getUnsafe().loadAdvancement(NamespacedKey.minecraft(townAdvancementPrefix + townName.toLowerCase()),
                                this.bannerAdvancement.getJsonAdvancement(this.getConfig().getString("messages.entering.town")
                                                .replace("+townName", townName),
                                        townBanner, this.getConfig().getString("messages.entering.townColor")));
                        getServer().getConsoleSender().sendMessage(townyBannersTag + ChatColor.GREEN + "Advancement " + townAdvancementPrefix + townName.toLowerCase() + " saved");
                    } catch (IllegalArgumentException e) {
                        getServer().getConsoleSender().sendMessage(townyBannersTag + ChatColor.DARK_RED + "Error while saving, Advancement " + townAdvancementPrefix + townName.toLowerCase() + " seems to already exist");
                    }
                }


                if (town.hasNation()) {
                    try {
                        Nation nation = town.getNation();
                        if (nation.hasMeta("banner")) {

                            StringDataField nationBannerField = (StringDataField) nation.getMetadata("banner");
                            ItemStack nationBanner = ItemUtils.stringToItem(nationBannerField.getValue());

                            String nationName = nation.getName();
                            String nationAdvancementPrefix = "towny_banners_nation_";

                            try {
                                Bukkit.getUnsafe().removeAdvancement(NamespacedKey.minecraft(nationAdvancementPrefix + nationName.toLowerCase()));
                                Bukkit.getServer().reloadData();
                                Bukkit.getUnsafe().loadAdvancement(NamespacedKey.minecraft(nationAdvancementPrefix + nationName.toLowerCase()),this.bannerAdvancement.getJsonAdvancement(this.getConfig().getString("messages.entering.nation").replace("+nationName", nationName), nationBanner, "gold"));
                                getServer().getConsoleSender().sendMessage(townyBannersTag + ChatColor.GREEN + "Advancement " + nationAdvancementPrefix + nationName.toLowerCase() + " saved");
                            } catch (IllegalArgumentException e) {
                                getServer().getConsoleSender().sendMessage(townyBannersTag + ChatColor.DARK_RED + "Error while saving, Advancement " + nationAdvancementPrefix + nationName.toLowerCase() + " seems to already exist");
                            }
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
        getServer().getConsoleSender().sendMessage(townyBannersTag + "Bye!");

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
}
