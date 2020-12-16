package net.laboulangerie.townybanners.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import net.laboulangerie.townybanners.BannerAdvancement;
import net.laboulangerie.townybanners.TownyBanners;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Base64;

public class BannersCommands implements CommandExecutor {
    private TownyBanners townyBanners;

    public BannersCommands(TownyBanners townyBanners) {
        this.townyBanners = townyBanners;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            TownyMessaging.sendErrorMsg(sender, townyBanners.getConfig().getString("messages.command.consoleTryCommand"));
            return true;
        }
        Player player = (Player) sender;


        if (cmd.getName().equalsIgnoreCase("tbanner") && player.hasPermission("townybanners.town")) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (Tag.BANNERS.isTagged(itemInHand.getType())) {
                ItemMeta bannerM = itemInHand.getItemMeta();
                ItemStack banner = new ItemStack(itemInHand.getType(), 1); // get only 1 banner
                banner.setItemMeta(bannerM);

                String serializedBanner = itemToString(banner);

                StringDataField bannerField = new StringDataField("banner", serializedBanner);
                try {
                    Resident resident = TownyAPI.getInstance().getDataSource().getResident(player.getName());
                    if (resident.hasTown()) {
                        Town town = resident.getTown();
                        town.addMetaData(bannerField);

                        if (Bukkit.getAdvancement(NamespacedKey.minecraft("towny_banners_town_" + town.getName().toLowerCase())) != null) {
                            Bukkit.getUnsafe().removeAdvancement(NamespacedKey.minecraft("towny_banners_town_" + town.getName().toLowerCase()));
                            Bukkit.getServer().reloadData();
                        }

                        Bukkit.getUnsafe().loadAdvancement(NamespacedKey.minecraft("towny_banners_town_" + town.getName().toLowerCase()), new BannerAdvancement().getJsonAdvancement(town.getName(), banner, "yellow"));
                        TownyMessaging.sendMsg(player, townyBanners.getConfig().getString("messages.command.townBannerSaved").replace("+townName", town.getName()));

                    } else {
                        TownyMessaging.sendErrorMsg(player, townyBanners.getConfig().getString("messages.command.playerDoesNotBelongToATown"));
                    }
                } catch (NotRegisteredException e) {
                    e.printStackTrace();
                }

            } else {
                TownyMessaging.sendErrorMsg(player, townyBanners.getConfig().getString("messages.command.playerHasNoBannerInHand"));
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("nbanner") && player.hasPermission("townybanners.nation")) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (Tag.BANNERS.isTagged(itemInHand.getType())) {
                ItemMeta bannerM = itemInHand.getItemMeta();
                ItemStack banner = new ItemStack(itemInHand.getType(), 1);
                banner.setItemMeta(bannerM);

                String serializedBanner = itemToString(banner);

                StringDataField bannerField = new StringDataField("banner", serializedBanner);
                try {
                    Resident resident = TownyAPI.getInstance().getDataSource().getResident(player.getName());
                    if (resident.hasTown()) {
                        Town town = resident.getTown();
                        if (town.hasNation()) {
                            Nation nation = town.getNation();
                            nation.addMetaData(bannerField);

                            if (Bukkit.getAdvancement(NamespacedKey.minecraft("towny_banners_nation_" + nation.getName().toLowerCase())) != null) {
                                Bukkit.getUnsafe().removeAdvancement(NamespacedKey.minecraft("towny_banners_nation_" + nation.getName().toLowerCase()));
                                Bukkit.getServer().reloadData();
                            }

                            Bukkit.getUnsafe().loadAdvancement(NamespacedKey.minecraft("towny_banners_nation_" + nation.getName().toLowerCase()), new BannerAdvancement().getJsonAdvancement(nation.getName(), banner, "gold"));
                            TownyMessaging.sendMsg(player, townyBanners.getConfig().getString("messages.command.nationBannerSaved").replace("+nationName", nation.getName()));

                        } else {
                            TownyMessaging.sendErrorMsg(player, townyBanners.getConfig().getString("messages.command.townDoesNotBelongToANation"));
                        }
                    } else {
                        TownyMessaging.sendErrorMsg(player, townyBanners.getConfig().getString("messages.command.playerDoesNotBelongToATown"));
                    }
                } catch (NotRegisteredException e) {
                    e.printStackTrace();
                }

            } else {
                TownyMessaging.sendErrorMsg(player, townyBanners.getConfig().getString("messages.command.playerHasNoBannerInHand"));
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("townybanners") && player.hasPermission("townybanners.reload")) {

            if (args[0].equalsIgnoreCase("reload")) {
                TownyMessaging.sendMsg(player, "Towny Banners config is reloading...");

                Bukkit.getPluginManager().disablePlugin(townyBanners);
                Bukkit.getPluginManager().getPlugin("TownyBanners").reloadConfig();
                Bukkit.getPluginManager().enablePlugin(townyBanners);

                TownyMessaging.sendMsg(player, "Towny Banners config has been reloaded!");
                return true;
            }
            return false;
        }
        return true;
    }

    public String itemToString(ItemStack item) {
        byte[] itemBytes = item.serializeAsBytes();
        return Base64.getEncoder().encodeToString(itemBytes);
    }

    public ItemStack stringToItem(String string) {
        byte[] decoded = Base64.getDecoder().decode(string);
        return ItemStack.deserializeBytes(decoded);
    }

}
