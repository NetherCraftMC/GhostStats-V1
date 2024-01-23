package de.ghost143.ghoststatsv1.commands;

import de.ghost143.ghoststatsv1.GhostStats_V1;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Stats implements CommandExecutor {

    private GhostStats_V1 plugin;

    public Stats(GhostStats_V1 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                sender.sendMessage("§4Verwendung: §b/stats [Spieler]");
            } else if (args.length == 1) {
                displayStats(player, args[0]);
            } else {
                player.sendMessage("§4Verwendung: §b/stats [Spieler]");
            }

            return true;
        } else {
            sender.sendMessage("Dieser Befehl kann nur von Spielern ausgeführt werden.");
            return false;
        }
    }

    private void displayStats(Player sender, String targetPlayerName) {
        String targetPlayerUUID = findPlayerUUIDByName(targetPlayerName);

        if (targetPlayerUUID != null) {
            int kills = plugin.getDatabaseManager().getPlayerKills(targetPlayerUUID);
            int deaths = plugin.getDatabaseManager().getPlayerDeaths(targetPlayerUUID);

            sender.sendMessage("§a-§5*§a-§5 §b" + targetPlayerName + "§7's Stats§a-§5*§a-§5*§a-§5*");
            sender.sendMessage("§aKills§7: §b" + kills);
            sender.sendMessage("§4Tode§7: §b" + deaths);
            sender.sendMessage("§a-§5*§a-§5*§a-§5*§a-§5*§a-§5*§a-§5*§a-§5*§a-§5*");
        } else {
            sender.sendMessage("§4Spieler nicht gefunden oder Offline");
        }
    }

    private String findPlayerUUIDByName(String playerName) {
        Player targetPlayer = Bukkit.getPlayer(playerName);
        return (targetPlayer != null) ? targetPlayer.getUniqueId().toString() : null;
    }
}
