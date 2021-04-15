package net.laboulangerie.townybanners.banner;

import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.inventory.ItemStack;

public class TownBanner extends Banner {

    protected TownBanner(Town town, ItemStack banner, String enteringMessage, String color, String jsonAdvancement) {
        super(town, banner, enteringMessage, color, jsonAdvancement);
    }
}
