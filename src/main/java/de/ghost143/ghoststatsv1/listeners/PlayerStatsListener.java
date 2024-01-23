package de.ghost143.ghoststatsv1.listeners;

import de.ghost143.ghoststatsv1.GhostStats_V1;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerStatsListener implements Listener {

    private GhostStats_V1 plugin;

    public PlayerStatsListener(GhostStats_V1 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerUUID = event.getPlayer().getUniqueId().toString();


        if (!plugin.getDatabaseManager().playerExists(playerUUID)) {

            plugin.getDatabaseManager().createPlayer(playerUUID);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        EntityDamageByEntityEvent damageEvent = findPlayerAttacker(event);

        if (damageEvent != null) {
            Entity attacker = damageEvent.getDamager();

            if (attacker instanceof Player) {
                String uuidAttacker = ((Player) attacker).getUniqueId().toString();
                String uuidVictim = player.getUniqueId().toString();

                plugin.getDatabaseManager().incrementKills(uuidAttacker);
                plugin.getDatabaseManager().incrementDeaths(uuidVictim);
            }
        }
    }

    private EntityDamageByEntityEvent findPlayerAttacker(PlayerDeathEvent event) {
        EntityDamageByEntityEvent damageEvent = null;

        if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            damageEvent = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
        }

        return damageEvent;
    }
}
