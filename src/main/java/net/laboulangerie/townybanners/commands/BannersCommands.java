package net.laboulangerie.townybanners.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import net.laboulangerie.townybanners.BannerAdvancement;
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
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            TownyMessaging.sendErrorMsg(sender, "Tu peux faire ça seulement en jeu !");
            return true;
        }
        Player player = (Player) sender;


        if (cmd.getName().equalsIgnoreCase("tbanner") && player.hasPermission("towny.tbanner")) {
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
                        TownyMessaging.sendMsg(player, "Cette bannière a bien été enregistrée pour le village " + town.getName());

                    } else {
                        TownyMessaging.sendErrorMsg(player, "§cVous devez être dans un village pour pouvoir mettre la bannière de celui-ci.");
                    }
                } catch (NotRegisteredException e) {
                    e.printStackTrace();
                }

            } else {
                TownyMessaging.sendErrorMsg(player, "§cVeuillez avoir une bannière dans la main principale pour l'enregistrer.");
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("nbanner") && player.hasPermission("towny.nbanner")) {
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
                            TownyMessaging.sendMsg(player, "Cette bannière a bien été enregistrée pour la nation " + nation.getName());

                        } else {
                            TownyMessaging.sendErrorMsg(player, "Votre village doit être dans une nation pour pouvoir mettre la bannière de celle-ci.");
                        }
                    } else {
                        TownyMessaging.sendErrorMsg(player, "Vous devez être dans un village pour pouvoir mettre la bannière de sa nation.");
                    }
                } catch (NotRegisteredException e) {
                    e.printStackTrace();
                }

            } else {
                TownyMessaging.sendErrorMsg(player, "§cVeuillez avoir une bannière dans la main principale pour l'enregistrer.");
            }
            return true;
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
