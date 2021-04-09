package net.laboulangerie.townybanners.banner;

import com.google.gson.JsonObject;
import com.palmergames.bukkit.towny.object.Government;
import net.laboulangerie.townybanners.utils.Reflector;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class BannerBuilder {

    private static Class<?> NBT_TAG_COMPOUND_CLASS;
    private static Class<?> CRAFT_ITEMSTACK_CLASS;

    private static Constructor<?> NBT_TAG_COMPOUND_CONSTRUCTOR;
    private static Method NBT_TO_STRING;
    private static Method AS_NMS_COPY;
    private static Method NBT_GET_TAG;

    static {
        try {
            Optional<Class<?>> nbtTagCompoundOptional = Reflector.nmsClass("NBTTagCompound");
            Optional<Class<?>> craftItemStackOptional = Reflector.obcClass("inventory.CraftItemStack");
            nbtTagCompoundOptional.ifPresent(aClass -> NBT_TAG_COMPOUND_CLASS = aClass);
            craftItemStackOptional.ifPresent(aClass -> CRAFT_ITEMSTACK_CLASS = aClass);

            NBT_TAG_COMPOUND_CONSTRUCTOR = NBT_TAG_COMPOUND_CLASS.getDeclaredConstructor();
            AS_NMS_COPY = CRAFT_ITEMSTACK_CLASS.getMethod("asNMSCopy", ItemStack.class);
            NBT_GET_TAG = CRAFT_ITEMSTACK_CLASS.getMethod("getTag");
            NBT_TO_STRING = NBT_TAG_COMPOUND_CLASS.getMethod("toString");
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }


    public static Banner buildBanner(BannerType  type, Government government, ItemStack banner) {
        Banner b = null;
        switch (type) {
            case NATION_BANNER:
                b = new NationBanner();
                break;
            case TOWN_BANNER:
                b = new TownBanner();
                break;
        }
        return b;
    }


    private static String buildJsonAdvancement(String enteringMsg, ItemStack banner, String color) {
        JsonObject advancementJson = new JsonObject();

        JsonObject bannerJson = new JsonObject();
        bannerJson.addProperty("item", "minecraft:" + banner.getType().toString().toLowerCase());
        try {
            Object nmsCopy = AS_NMS_COPY.invoke(banner);
            Object tag = NBT_GET_TAG.invoke(nmsCopy);
            String toString = (String) NBT_TO_STRING.invoke(tag);

            if (tag != null) {
                bannerJson.addProperty("nbt", toString);
            } else {
                tag = NBT_TAG_COMPOUND_CONSTRUCTOR.newInstance();
                toString = (String) NBT_TO_STRING.invoke(tag);
                bannerJson.addProperty("nbt", toString);
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
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


        return advancementJson.getAsString();
    }
}
