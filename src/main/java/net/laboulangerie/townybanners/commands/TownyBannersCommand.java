package net.laboulangerie.townybanners.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import net.laboulangerie.townybanners.TownyBanners;
import net.laboulangerie.townybanners.advancement.Keys;
import net.laboulangerie.townybanners.utils.ItemUtils;
import net.laboulangerie.townybanners.utils.TownyBannersConfig;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class TownyBannersCommand implements CommandExecutor {

    private TownyBanners townyBanners;
    private TownyBannersConfig config;

    public TownyBannersCommand(TownyBanners townyBanners) {
        this.townyBanners = townyBanners;
        this.config = townyBanners.getTownyBannerConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            TownyMessaging.sendErrorMsg(sender, this.config.getConsoleTryCommand());
            return false;
        }

        Player player = (Player) sender;

        switch (args[0].toLowerCase()) {
            case "reload":
                if (player.hasPermission("townybanners.reload")) {
                    TownyMessaging.sendMsg(player, "Towny Banners config is reloading...");

                    PluginManager manager = Bukkit.getPluginManager();
                    manager.disablePlugin(this.townyBanners);

                    this.townyBanners.reloadTownyBannerConfig();
                    manager.enablePlugin(this.townyBanners);

                    TownyMessaging.sendMsg(player, "TownyBanners config has been reloaded!");
                    break;
                }
            case "setbanner":
                if (player.hasPermission("townybanners.setbanner")) {
                    switch (args[1].toLowerCase()) {
                        case "town":
                            if (args.length == 2) {
                                Town town = TownyAPI.getInstance().getTown(args[2]);
                                if (town != null && town.hasMeta("townybanners_banner")) {

                                    ItemStack itemInHand = player.getInventory().getItemInMainHand();
                                    if (Tag.BANNERS.isTagged(itemInHand.getType())) {
                                        ItemMeta bannerM = itemInHand.getItemMeta();
                                        ItemStack banner = new ItemStack(itemInHand.getType(), 1); // get only 1 banner
                                        banner.setItemMeta(bannerM);

                                        String serializedBanner = ItemUtils.itemToString(banner);

                                        StringDataField bannerField = new StringDataField("townybanners_banner", serializedBanner);

                                        town.addMetaData(bannerField);
                                        NamespacedKey townKey = Keys.TOWN.getKey(town.getName().toLowerCase());
                                        if (Bukkit.getAdvancement(townKey) != null) {
                                            Bukkit.getUnsafe().removeAdvancement(townKey);
                                            Bukkit.getServer().reloadData();
                                        }
                                        Bukkit.getUnsafe().loadAdvancement(townKey,
                                                this.townyBanners.getBannerAdvancement().getJsonAdvancement(this.config.getEnteringTown(town.getName()), banner, this.config.getEnteringTownColor()));

                                        TownyMessaging.sendMsg(player, this.config.getTownBannerSaved(town.getName()));
                                    } else {
                                        TownyMessaging.sendErrorMsg(player, this.config.getPlayerBannerNotInHand());
                                    }
                                }
                            }
                            break;
                        case "nation":
                            if (args.length == 2) {
                                Nation nation = TownyAPI.getInstance().getNation(args[2]);
                                if (nation != null && nation.hasMeta("townybanners_banner")) {

                                    ItemStack itemInHand = player.getInventory().getItemInMainHand();
                                    if (Tag.BANNERS.isTagged(itemInHand.getType())) {
                                        ItemMeta bannerM = itemInHand.getItemMeta();
                                        ItemStack banner = new ItemStack(itemInHand.getType(), 1); // get only 1 banner
                                        banner.setItemMeta(bannerM);

                                        String serializedBanner = ItemUtils.itemToString(banner);

                                        StringDataField bannerField = new StringDataField("townybanners_banner", serializedBanner);

                                        nation.addMetaData(bannerField);
                                        NamespacedKey nationKey = Keys.NATION.getKey(nation.getName().toLowerCase());
                                        if (Bukkit.getAdvancement(nationKey) != null) {
                                            Bukkit.getUnsafe().removeAdvancement(nationKey);
                                            Bukkit.getServer().reloadData();
                                        }
                                        Bukkit.getUnsafe().loadAdvancement(nationKey,
                                                this.townyBanners.getBannerAdvancement().getJsonAdvancement(this.config.getEnteringNation(nation.getName()), banner, this.config.getEnteringNationColor()));

                                        TownyMessaging.sendMsg(player, this.config.getNationBannerSaved(nation.getName()));
                                    } else {
                                        TownyMessaging.sendErrorMsg(player, this.config.getPlayerBannerNotInHand());
                                    }
                                }
                            }
                        break;
                    }
                }
            case "unsetbanner":
                if (player.hasPermission("townybanners.unsetbanner")) {
                    switch (args[1].toLowerCase()) {
                        case "town":
                                if (args.length == 2) {
                                    Town town = TownyAPI.getInstance().getTown(args[2]);

                                if (town != null && town.hasMeta("townybanners_banner")) {
                                    town.removeMetaData(town.getMetadata("townybanners_banner"));
                                    town.removeMetaData(town.getMetadata("townybanners_timestamp"));

                                    NamespacedKey townKey = Keys.TOWN.getKey(town.getName().toLowerCase());
                                    Bukkit.getUnsafe().removeAdvancement(townKey);
                                }
                            }
                            break;
                        case "nation":
                            if (args.length == 2) {
                                Nation nation = TownyAPI.getInstance().getNation(args[2]);

                                if (nation != null && nation.hasMeta("townybanners_banner")) {
                                    nation.removeMetaData(nation.getMetadata("townybanners_banner"));
                                    nation.removeMetaData(nation.getMetadata("townybanners_timestamp"));

                                    NamespacedKey nationKey = Keys.NATION.getKey(nation.getName().toLowerCase());
                                    Bukkit.getUnsafe().removeAdvancement(nationKey);
                                }
                            }
                            break;
                    }
                }
        }
        return true;
    }
}
