package net.laboulangerie.townybanners.advancement;

import com.google.gson.JsonObject;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import net.laboulangerie.townybanners.TownyBanners;
import net.laboulangerie.townybanners.utils.ItemUtils;
import net.laboulangerie.townybanners.utils.TownyBannersConfig;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class BannerAdvancement {

    private TownyBanners townyBanners;
    private TownyBannersConfig config;

    public BannerAdvancement(TownyBanners townyBanners) {
        this.townyBanners = townyBanners;
        this.config = townyBanners.getTownyBannerConfig();
    }

    public String getJsonAdvancement(String enteringMsg, ItemStack banner, String color) {
        JsonObject advancementJson = new JsonObject();

        JsonObject bannerJson = new JsonObject();
        bannerJson.addProperty("item", "minecraft:" + banner.getType().toString().toLowerCase());
        if (CraftItemStack.asNMSCopy(banner).getTag() != null) {
            bannerJson.addProperty("nbt", CraftItemStack.asNMSCopy(banner).getTag().toString());
        } else {
            bannerJson.addProperty("nbt", new NBTTagCompound().toString());
        }

        JsonObject titleJson = new JsonObject();
        titleJson.addProperty("text", enteringMsg);
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


        return this.townyBanners.getGson().toJson(advancementJson);
    }

    public void loadTownAdvancement(Keys type, ItemStack banner, String name) {
        String lowerCase = name.toLowerCase();
        try {
            Bukkit.getUnsafe().removeAdvancement(type.getKey(lowerCase));
            Bukkit.getServer().reloadData();
            Bukkit.getUnsafe().loadAdvancement(type.getKey(lowerCase),
                    this.getJsonAdvancement(this.config.enteringTown(name),
                            banner, this.config.getNationColor()));
            this.townyBanners.getServer().getConsoleSender().sendMessage(TownyBanners.BANNER_TAG + ChatColor.GREEN + "Advancement " + type.getKey(name) + " saved");
        } catch (IllegalArgumentException e) {
            this.townyBanners.getServer().getConsoleSender().sendMessage(TownyBanners.BANNER_TAG + ChatColor.DARK_RED + "Error while saving, Advancement " + type.getKey(name) + " seems to already exist");
        }
    }

    public void loadNationAdvancement(Keys type, ItemStack banner, String name) {
        String lowerCase = name.toLowerCase();
        try {
            Bukkit.getUnsafe().removeAdvancement(type.getKey(lowerCase));
            Bukkit.getServer().reloadData();
            Bukkit.getUnsafe().loadAdvancement(type.getKey(lowerCase),
                    this.getJsonAdvancement(this.config.enteringNation(name),
                            banner, this.config.getNationColor()));
            this.townyBanners.getServer().getConsoleSender().sendMessage(TownyBanners.BANNER_TAG + ChatColor.GREEN + "Advancement " + type.getKey(name) + " saved");
        } catch (IllegalArgumentException e) {
            this.townyBanners.getServer().getConsoleSender().sendMessage(TownyBanners.BANNER_TAG + ChatColor.DARK_RED + "Error while saving, Advancement " + type.getKey(name) + " seems to already exist");
        }
    }


    public void registerAdvancements() {
        Collection<Town> towns = this.townyBanners.getTownyDataSource().getTowns();
        if (this.config.isPoppingOut()) {

            for (Town town : towns) {

                if (town.hasMeta("townybanners_banner")) {
                    StringDataField townBannerField = (StringDataField) town.getMetadata("townybanners_banner");
                    ItemStack townBanner = ItemUtils.stringToItem(townBannerField.getValue());

                    String townName = town.getName();
                    this.loadTownAdvancement(Keys.TOWN, townBanner, townName);
                }

                if (town.hasNation()) {
                    try {
                        Nation nation = town.getNation();
                        if (nation.hasMeta("townybanners_banner")) {

                            StringDataField nationBannerField = (StringDataField) nation.getMetadata("townybanners_banner");
                            ItemStack nationBanner = ItemUtils.stringToItem(nationBannerField.getValue());

                            String nationName = nation.getName();
                            this.loadNationAdvancement(Keys.NATION, nationBanner, nationName);
                        }
                    } catch (NotRegisteredException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
