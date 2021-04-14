package net.laboulangerie.townybanners.utils;

import org.bukkit.inventory.ItemStack;

import java.util.Base64;

public class ItemUtils {

    public static String itemToString(ItemStack item) {
        byte[] itemBytes = item.serializeAsBytes();
        return Base64.getEncoder().encodeToString(itemBytes);
    }

    public static ItemStack stringToItem(String string) {
        byte[] decoded = Base64.getDecoder().decode(string);
        return ItemStack.deserializeBytes(decoded);
    }


}
