package net.laboulangerie.townybanners.advancement;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AdvancementRevoker extends BukkitRunnable {

    private String key;
    private Player player;

    public AdvancementRevoker(Player player, String key) {
        this.player = player;
        this.key = key;
    }

    @Override
    public void run() {
        Advancement advancement = Bukkit.getAdvancement(NamespacedKey.minecraft(this.key));
        AdvancementProgress progress = this.player.getAdvancementProgress(advancement);
        if (progress.isDone()) {
            for (String criteria : progress.getAwardedCriteria()) {
                progress.revokeCriteria(criteria);
            }
        }
    }
}
