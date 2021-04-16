package net.laboulangerie.townybanners.listeners;

import com.palmergames.bukkit.towny.event.PlayerEnterTownEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

import net.laboulangerie.townybanners.TownyBanners;
import net.laboulangerie.townybanners.advancement.AdvancementRevoker;
import net.laboulangerie.townybanners.advancement.Keys;
import net.laboulangerie.townybanners.banner.Banner;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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

            if (town.hasMeta("townybanners_banner")) {
                grantAdvancement(player, Keys.TOWN, town.getName().toLowerCase());
                revokeAdvancement(player, Keys.TOWN, town.getName().toLowerCase());
            }

            if (town.hasNation()) {
                if (town.getNation().hasMeta("townybanners_banner")) {
                    Nation nation = town.getNation();
                    grantAdvancement(player, Keys.NATION, nation.getName().toLowerCase());
                    revokeAdvancement(player, Keys.NATION, nation.getName().toLowerCase());
                }
            }
        }
    }

    public void grantAdvancement(Player player, Banner banner) {
        this.grantAdvancement(player, banner.getKey(), banner.getGovernmentName());
    }

    public void revokeAdvancement(Player player, Banner banner) {
        this.revokeAdvancement(player, banner.getKey(), banner.getGovernmentName());
    }

    public void grantAdvancement(Player player, Keys key, String name) {

        Advancement advancement = Bukkit.getAdvancement(key.getKey(name));

        AdvancementProgress progress = player.getAdvancementProgress(advancement);
        for (String criteria : progress.getRemainingCriteria()) {
            progress.awardCriteria(criteria);
        }

    }

    public void revokeAdvancement(Player player, Keys key, String name) {
        AdvancementRevoker revoker = new AdvancementRevoker(player, key.getKey(name));
        revoker.runTaskLater(this.townyBanners, 120);
    }
}
