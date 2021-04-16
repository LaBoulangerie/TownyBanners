package net.laboulangerie.townybanners.commands;

import com.palmergames.bukkit.towny.TownyMessaging;
import net.laboulangerie.townybanners.TownyBanners;
import net.laboulangerie.townybanners.utils.TownyBannersConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class TownyBannersCommand implements CommandExecutor {

    private TownyBanners townyBanners;
    private TownyBannersConfig config;

    public TownyBannersCommand(TownyBanners townyBanners) {
        this.townyBanners = townyBanners;
        this.config = townyBanners.getTownyBannerConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            TownyMessaging.sendErrorMsg(sender, this.config.getConsoleTryCommand());
            return false;
        }

        Player player = (Player) sender;
        if (player.hasPermission("townybanners.reload")) {
            if (args.length > 1 && args[0].equalsIgnoreCase("reload")) {
                TownyMessaging.sendMsg(player, "Towny Banners config is reloading...");

                /*Bukkit.getPluginManager().disablePlugin(townyBanners);
                Bukkit.getPluginManager().getPlugin("TownyBanners").reloadConfig();
                Bukkit.getPluginManager().enablePlugin(townyBanners);*/

                //Hot reload (this can not work as well as intended
                PluginManager manager = Bukkit.getPluginManager();
                manager.disablePlugin(this.townyBanners);
                this.townyBanners.reloadTownyBannerConfig();
                manager.enablePlugin(this.townyBanners);
                TownyMessaging.sendMsg(player, "TownyBanners config has been reloaded!");
                return true;
            }
            return false;
        }
        return true;
    }
}
