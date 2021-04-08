package net.laboulangerie.townybanners.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import net.laboulangerie.townybanners.TownyBanners;
import net.laboulangerie.townybanners.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class NationBannersCommand implements CommandExecutor {

    private TownyBanners townyBanners;

    public NationBannersCommand(TownyBanners townyBanners) {
        this.townyBanners = townyBanners;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            TownyMessaging.sendErrorMsg(sender, townyBanners.getConfig().getString("messages.command.consoleTryCommand"));
            return false;
        }

        Player player = (Player) sender;
        if (player.hasPermission("townybanners.nation")) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (Tag.BANNERS.isTagged(itemInHand.getType())) {
                ItemMeta bannerM = itemInHand.getItemMeta();
                ItemStack banner = new ItemStack(itemInHand.getType(), 1);
                banner.setItemMeta(bannerM);

                String serializedBanner = ItemUtils.itemToString(banner);
                StringDataField bannerField = new StringDataField("banner", serializedBanner);
                try {
                    Resident resident = this.townyBanners.getTownyDataSource().getResident(player.getName());
                    if (resident.hasTown()) {
                        Town town = resident.getTown();
                        if (town.hasNation()) {
                            Nation nation = town.getNation();
                            nation.addMetaData(bannerField);

                            if (Bukkit.getAdvancement(NamespacedKey.minecraft("towny_banners_nation_" + nation.getName().toLowerCase())) != null) {
                                Bukkit.getUnsafe().removeAdvancement(NamespacedKey.minecraft("towny_banners_nation_" + nation.getName().toLowerCase()));
                                Bukkit.getServer().reloadData();
                            }

                            Bukkit.getUnsafe().loadAdvancement(NamespacedKey.minecraft("towny_banners_nation_" + nation.getName().toLowerCase()), this.townyBanners.getBannerAdvancement().getJsonAdvancement(nation.getName(), banner, "gold"));
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
        }
        return true;
    }
}
