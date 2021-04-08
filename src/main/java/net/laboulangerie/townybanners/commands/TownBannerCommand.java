package net.laboulangerie.townybanners.commands;

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import net.laboulangerie.townybanners.advancement.BannerAdvancement;
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

public class TownBannerCommand implements CommandExecutor {

    private TownyBanners townyBanners;

    public TownBannerCommand(TownyBanners townyBanners) {
        this.townyBanners = townyBanners;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            TownyMessaging.sendErrorMsg(sender, townyBanners.getConfig().getString("messages.command.consoleTryCommand"));
            return false;
        }
        Player player = (Player) sender;

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (Tag.BANNERS.isTagged(itemInHand.getType())) {
            ItemMeta bannerM = itemInHand.getItemMeta();
            ItemStack banner = new ItemStack(itemInHand.getType(), 1); // get only 1 banner
            banner.setItemMeta(bannerM);

            String serializedBanner = ItemUtils.itemToString(banner);

            StringDataField bannerField = new StringDataField("banner", serializedBanner);
            try {
                Resident resident = this.townyBanners.getTownyDataSource().getResident(player.getName());
                if (resident.hasTown()) {
                    Town town = resident.getTown();
                    town.addMetaData(bannerField);

                    if (Bukkit.getAdvancement(NamespacedKey.minecraft("towny_banners_town_" + town.getName().toLowerCase())) != null) {
                        Bukkit.getUnsafe().removeAdvancement(NamespacedKey.minecraft("towny_banners_town_" + town.getName().toLowerCase()));
                        Bukkit.getServer().reloadData();
                    }

                    Bukkit.getUnsafe().loadAdvancement(NamespacedKey.minecraft("towny_banners_town_" + town.getName().toLowerCase()), this.townyBanners.getBannerAdvancement().getJsonAdvancement(town.getName(), banner, "yellow"));
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
}
