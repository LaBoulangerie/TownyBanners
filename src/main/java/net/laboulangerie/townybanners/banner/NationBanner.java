package net.laboulangerie.townybanners.banner;

import com.palmergames.bukkit.towny.object.Nation;
import org.bukkit.inventory.ItemStack;

public class NationBanner extends Banner {

    protected NationBanner(Nation nation, ItemStack banner, String enteringMessage, String color, String jsonAdvancement) {
        super(nation, banner, enteringMessage, color, jsonAdvancement);
    }
}
