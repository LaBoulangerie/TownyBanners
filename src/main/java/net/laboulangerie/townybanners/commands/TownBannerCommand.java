package net.laboulangerie.townybanners.commands;

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
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
import org.jetbrains.annotations.NotNull;

public class TownBannerCommand implements CommandExecutor {

    private TownyBanners townyBanners;
    private TownyBannersConfig config;

    public TownBannerCommand(TownyBanners townyBanners) {
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
                    NamespacedKey townKey = Keys.TOWN.getKey(town.getName().toLowerCase());
                    if (Bukkit.getAdvancement(townKey) != null) {
                        Bukkit.getUnsafe().removeAdvancement(townKey);
                        Bukkit.getServer().reloadData();
                    }

                    Bukkit.getUnsafe().loadAdvancement(townKey,
                            this.townyBanners.getBannerAdvancement().getJsonAdvancement(town.getName(), banner, this.config.getTownColor()));
                    TownyMessaging.sendMsg(player, this.config.getTownBannerSaved(town.getName()));

                } else {
                    TownyMessaging.sendErrorMsg(player, this.config.getPlayerDoesNotBelongToATown());
                }
            } catch (NotRegisteredException e) {
                e.printStackTrace();
            }

        } else {
            TownyMessaging.sendErrorMsg(player, this.config.getPlayerHasNoBannerInHand());
        }
        return true;
    }
}
