package net.laboulangerie.townybanners.banner;

import com.palmergames.bukkit.towny.object.Government;
import com.palmergames.bukkit.towny.object.Town;
import net.laboulangerie.townybanners.advancement.Keys;
import net.laboulangerie.townybanners.utils.ItemUtils;
import org.bukkit.inventory.ItemStack;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.UUID;

public abstract class Banner implements Serializable {

    private UUID governmentUniqueId;
    private String governmentName;
    private BannerType bannerType;

    private String base64SerializedBanner;
    private Keys key;
    private String jsonAdvancement;

    private String enteringMessage;
    private String color;

    protected Banner(Government government, ItemStack banner, String enteringMessage, String color, String jsonAdvancement) {
        this.governmentUniqueId = government.getUUID();
        this.governmentName = government.getName();
        this.bannerType = (government instanceof Town) ? BannerType.TOWN_BANNER : BannerType.NATION_BANNER;
        this.base64SerializedBanner = ItemUtils.itemToString(banner);
        this.jsonAdvancement = jsonAdvancement;
        this.key = (government instanceof Town) ? Keys.TOWN : Keys.NATION;
        this.enteringMessage = enteringMessage;
        this.color = color;
    }

    public UUID getGovernmentUniqueId() {
        return governmentUniqueId;
    }

    public String getGovernmentName() {
        return governmentName;
    }

    public BannerType getBannerType() {
        return bannerType;
    }

    public Keys getKey() {
        return key;
    }

    public String getBase64SerializedBanner() {
        return base64SerializedBanner;
    }

    public String getJsonAdvancement() {
        return jsonAdvancement;
    }

    public String getEnteringMessage() {
        return color + enteringMessage;
    }

    public String getColor() {
        return color;
    }



    public BufferedImage toBufferedImage() {
        BufferedImage image = null;
        return image;
    }

}
