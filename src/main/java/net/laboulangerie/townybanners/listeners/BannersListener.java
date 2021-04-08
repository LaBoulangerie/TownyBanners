package net.laboulangerie.townybanners.listeners;

import com.palmergames.bukkit.towny.event.PlayerEnterTownEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

import net.laboulangerie.townybanners.TownyBanners;
import net.laboulangerie.townybanners.advancement.AdvancementRevoker;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class BannersListener implements Listener {
    private TownyBanners townyBanners;

    public BannersListener(TownyBanners townyBanners) {
        this.townyBanners = townyBanners;
    }

    @EventHandler
    public void onTownEntering(PlayerEnterTownEvent event) throws NotRegisteredException {
        if (townyBanners.getConfig().getBoolean("advancementPopUp")) {

            Player player = event.getPlayer();
            Town town = event.getEnteredtown();

            if (town.hasMeta("banner")) {
                grantAdvancement(player, "towny_banners_town_" + town.getName().toLowerCase());
                revokeAdvancement(player, "towny_banners_town_" + town.getName().toLowerCase());
            }

            if (town.hasNation()) {
                if (town.getNation().hasMeta("banner")) {
                    Nation nation = town.getNation();
                    grantAdvancement(player, "towny_banners_nation_" + nation.getName().toLowerCase());
                    revokeAdvancement(player, "towny_banners_nation_" + nation.getName().toLowerCase());
                }
            }
        }
    }

    public void grantAdvancement(Player player, String namespacedKey) {

        Advancement advancement = Bukkit.getAdvancement(NamespacedKey.minecraft(namespacedKey));
        AdvancementProgress progress;

        progress = player.getAdvancementProgress(advancement);

        if (!progress.isDone()) {
            for (String criteria : progress.getRemainingCriteria()) {
                progress.awardCriteria(criteria);
            }
        }

    }

    public void revokeAdvancement(Player player, String namespacedKey) {
        AdvancementRevoker revoker = new AdvancementRevoker(player, namespacedKey);
        revoker.runTaskLater(this.townyBanners, 120);
    }
}
