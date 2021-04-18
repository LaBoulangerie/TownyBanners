package net.laboulangerie.townybanners.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TownyBannersTab implements TabCompleter {

    private static List<String> getTownsNames() {
        List<String> townsNames = new ArrayList<>();
        for (Town town : TownyUniverse.getInstance().getTowns()) {
            townsNames.add(town.getName());
        }
        return townsNames;
    }

    private static List<String> getNationNames() {
        List<String> nationNames = new ArrayList<>();
        for (Nation nation : TownyUniverse.getInstance().getNations()) {
            nationNames.add(nation.getName());
        }
        return nationNames;
    }

    List<String> subCommands = new ArrayList<String>();
    List<String> arguments = new ArrayList<String>();


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (subCommands.isEmpty()) {
            subCommands.add("reload");
            subCommands.add("set");
            subCommands.add("unset");
            subCommands.add("give");
        }

        if (arguments.isEmpty()) {
            arguments.add("town");
            arguments.add("nation");
        }

        List<String> results = new ArrayList<String>();
        if (args.length == 1) {
            for (String arg : subCommands) {
                if (arg.toLowerCase().startsWith(args[0].toLowerCase())) {
                    results.add(arg);
                }
            }
        }

        if (args.length == 2 && !args[0].equals("reload")) {
            for (String arg : arguments) {
                if (arg.toLowerCase().startsWith(args[1].toLowerCase())) {
                    results.add(arg);
                }
            }
        }

        if (args.length == 3 && args[1].toLowerCase().equals("town")) {
            for (String town : getTownsNames()) {
                if (town.toLowerCase().startsWith(args[2].toLowerCase())) {
                    results.add(town);
                }
            }
        }

        if (args.length == 3 && args[1].toLowerCase().equals("nation")) {
            for (String nation : getNationNames()) {
                if (nation.toLowerCase().startsWith(args[2].toLowerCase())) {
                    results.add(nation);
                }
            }
        }

        return results;
    }
}
