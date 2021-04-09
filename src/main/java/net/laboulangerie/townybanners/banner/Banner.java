package net.laboulangerie.townybanners.banner;

import java.util.UUID;

public abstract class Banner {

    private UUID governmentUniqueId;
    private String governmentName;
    private BannerType bannerType;

    private String base64SerializedBanner;
    private String jsonAdvancement;

}
