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
import org.bukkit.Material;
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

        if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            if (player.hasPermission("townybanners.reload")) {
                TownyMessaging.sendMsg(player, "Towny Banners config is reloading...");

                PluginManager manager = Bukkit.getPluginManager();
                manager.disablePlugin(this.townyBanners);

                this.townyBanners.reloadTownyBannerConfig();
                manager.enablePlugin(this.townyBanners);

                TownyMessaging.sendMsg(player, "TownyBanners config has been reloaded!");
                return true;
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                if (player.hasPermission("townybanners.set")) {
                    ItemStack itemInHand = player.getInventory().getItemInMainHand();
                    if (Tag.BANNERS.isTagged(itemInHand.getType())) {
                        ItemMeta bannerM = itemInHand.getItemMeta();
                        ItemStack banner = new ItemStack(itemInHand.getType(), 1); // get only 1 banner
                        banner.setItemMeta(bannerM);

                        String serializedBanner = ItemUtils.itemToString(banner);

                        StringDataField bannerField = new StringDataField("townybanners_banner", serializedBanner);

                        if (args[1].toLowerCase().equals("town")) {
                            Town town = TownyAPI.getInstance().getTown(args[2]);
                            if (town != null && town.hasMeta("townybanners_banner")) {
                                town.addMetaData(bannerField);
                                NamespacedKey townKey = Keys.TOWN.getKey(town.getName().toLowerCase());
                                if (Bukkit.getAdvancement(townKey) != null) {
                                    Bukkit.getUnsafe().removeAdvancement(townKey);
                                    Bukkit.getServer().reloadData();
                                }
                                Bukkit.getUnsafe().loadAdvancement(townKey,
                                        this.townyBanners.getBannerAdvancement().getJsonAdvancement(this.config.getEnteringTown(town.getName()), banner, this.config.getEnteringTownColor()));

                                TownyMessaging.sendMsg(player, this.config.getTownBannerSaved(town.getName()));
                                return true;
                            }
                        }

                        if (args[1].equalsIgnoreCase("nation")) {
                            Nation nation = TownyAPI.getInstance().getNation(args[2]);
                            if (nation != null && nation.hasMeta("townybanners_banner")) {
                                nation.addMetaData(bannerField);
                                NamespacedKey nationKey = Keys.NATION.getKey(nation.getName().toLowerCase());
                                if (Bukkit.getAdvancement(nationKey) != null) {
                                    Bukkit.getUnsafe().removeAdvancement(nationKey);
                                    Bukkit.getServer().reloadData();
                                }
                                Bukkit.getUnsafe().loadAdvancement(nationKey,
                                        this.townyBanners.getBannerAdvancement().getJsonAdvancement(this.config.getEnteringNation(nation.getName()), banner, this.config.getEnteringNationColor()));

                                TownyMessaging.sendMsg(player, this.config.getNationBannerSaved(nation.getName()));
                                return true;
                            }
                        }
                    } else {
                        TownyMessaging.sendErrorMsg(player, this.config.getPlayerBannerNotInHand());
                    }
                }
            }

            if (args[0].equalsIgnoreCase("unset")) {
                if (player.hasPermission("townybanners.unset")) {
                    if (args[1].equalsIgnoreCase("town")) {
                        Town town = TownyAPI.getInstance().getTown(args[2]);
                        if (town != null && town.hasMeta("townybanners_banner")) {
                            town.removeMetaData(town.getMetadata("townybanners_banner"));
                            town.removeMetaData(town.getMetadata("townybanners_timestamp"));

                            NamespacedKey townKey = Keys.TOWN.getKey(town.getName().toLowerCase());
                            Bukkit.getUnsafe().removeAdvancement(townKey);
                            return true;
                        }
                    }

                    if (args[1].equalsIgnoreCase("nation")) {
                        Nation nation = TownyAPI.getInstance().getNation(args[2]);

                        if (nation != null && nation.hasMeta("townybanners_banner")) {
                            nation.removeMetaData(nation.getMetadata("townybanners_banner"));
                            nation.removeMetaData(nation.getMetadata("townybanners_timestamp"));

                            NamespacedKey nationKey = Keys.NATION.getKey(nation.getName().toLowerCase());
                            Bukkit.getUnsafe().removeAdvancement(nationKey);
                            return true;
                        }
                    }
                }
            }

            if (args[0].equalsIgnoreCase("give")) {
                if (player.hasPermission("townybanners.give")) {
                    if (args[1].equalsIgnoreCase("town")) {
                        Town town = TownyAPI.getInstance().getTown(args[2]);
                        if (town != null && town.hasMeta("townybanners_banner")) {
                            StringDataField townBannerField = (StringDataField) town.getMetadata("townybanners_banner");
                            ItemStack townBanner = ItemUtils.stringToItem(townBannerField.getValue());
                            player.getInventory().addItem(townBanner);
                            return true;
                        }
                    }

                    if (args[1].equalsIgnoreCase("nation")) {
                        Nation nation = TownyAPI.getInstance().getNation(args[2]);
                        if (nation != null && nation.hasMeta("townybanners_banner")) {
                            StringDataField nationBannerField = (StringDataField) nation.getMetadata("townybanners_banner");
                            ItemStack nationBanner = ItemUtils.stringToItem(nationBannerField.getValue());
                            player.getInventory().addItem(nationBanner);
                            return true;
                        }
                    }
                }
            }
        }
        return true;
    }
}
