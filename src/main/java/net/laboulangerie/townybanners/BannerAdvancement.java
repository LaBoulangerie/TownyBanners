package net.laboulangerie.townybanners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class BannerAdvancement {

    public String getJsonAdvancement(String name, ItemStack banner, String color) {
        JsonObject advancementJson = new JsonObject();

        JsonObject bannerJson = new JsonObject();
        bannerJson.addProperty("item", "minecraft:" + banner.getType().toString().toLowerCase());
        if (CraftItemStack.asNMSCopy(banner).getTag() != null) {
            bannerJson.addProperty("nbt", CraftItemStack.asNMSCopy(banner).getTag().toString());
        } else {
            bannerJson.addProperty("nbt", new NBTTagCompound().toString());
        }

        JsonObject titleJson = new JsonObject();
        titleJson.addProperty("text", "Entering " + name);
        titleJson.addProperty("color", color);

        JsonObject displayJson = new JsonObject();
        displayJson.add("icon", bannerJson);
        displayJson.add("title", titleJson);
        displayJson.add("description", titleJson);
        displayJson.addProperty("background", "minecraft:textures/gui/advancements/backgrounds/adventure.png");
        displayJson.addProperty("frame", "goal");
        displayJson.addProperty("announce_to_chat", "false");
        displayJson.addProperty("hidden", true);

        JsonObject criteriaJson = new JsonObject();
        JsonObject triggerJson = new JsonObject();

        triggerJson.addProperty("trigger", "minecraft:impossible");
        criteriaJson.add("impossible", triggerJson);

        advancementJson.add("criteria", criteriaJson);
        advancementJson.add("display", displayJson);

        Gson advancementGson = new GsonBuilder().setPrettyPrinting().create();
        return advancementGson.toJson(advancementJson);
    }

}
