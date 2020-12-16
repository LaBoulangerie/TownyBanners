package net.laboulangerie.townybanners;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import net.laboulangerie.townybanners.commands.BannersCommands;
import net.laboulangerie.townybanners.events.BannersEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Base64;
import java.util.Collection;

public class TownyBanners extends JavaPlugin {
    public String townyBannersTag = ChatColor.GOLD + "[TownyBanners] ";

    @Override
    public void onEnable() {
        saveDefaultConfig();

        BannersCommands commands = new BannersCommands(this);

        getCommand("tbanner").setExecutor(commands);
        getCommand("nbanner").setExecutor(commands);
        getCommand("townybanners").setExecutor(commands);


        getServer().getPluginManager().registerEvents(new BannersEvents(this), this);
        getServer().getConsoleSender().sendMessage(townyBannersTag + "Ready!");

        Collection<Town> towns = TownyAPI.getInstance().getDataSource().getTowns();
        if (this.getConfig().getBoolean("advancementPopUp")) {

            for (Town town : towns) {

                if (town.hasMeta("banner")) {
                    StringDataField townBannerField = (StringDataField) town.getMetadata("banner");
                    ItemStack townBanner = stringToItem(townBannerField.getValue());

                    String townName = town.getName();
                    String townAdvancementPrefix = "towny_banners_town_";

                    try {
                        Bukkit.getUnsafe().removeAdvancement(NamespacedKey.minecraft(townAdvancementPrefix + townName.toLowerCase()));
                        Bukkit.getServer().reloadData();
                        Bukkit.getUnsafe().loadAdvancement(NamespacedKey.minecraft(townAdvancementPrefix + townName.toLowerCase()),
                                new BannerAdvancement().getJsonAdvancement(this.getConfig().getString("messages.entering.town")
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
                            ItemStack nationBanner = stringToItem(nationBannerField.getValue());

                            String nationName = nation.getName();
                            String nationAdvancementPrefix = "towny_banners_nation_";

                            try {
                                Bukkit.getUnsafe().removeAdvancement(NamespacedKey.minecraft(nationAdvancementPrefix + nationName.toLowerCase()));
                                Bukkit.getServer().reloadData();
                                Bukkit.getUnsafe().loadAdvancement(NamespacedKey.minecraft(nationAdvancementPrefix + nationName.toLowerCase()), new BannerAdvancement().getJsonAdvancement(this.getConfig().getString("messages.entering.nation").replace("+nationName", nationName), nationBanner, "gold"));
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


    public ItemStack stringToItem(String string) {
        byte[] decoded = Base64.getDecoder().decode(string);
        return ItemStack.deserializeBytes(decoded);
    }
}
