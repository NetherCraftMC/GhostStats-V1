package de.ghost143.ghoststatsv1;

import de.ghost143.ghoststatsv1.commands.Stats;
import de.ghost143.ghoststatsv1.database.DatabaseManager;
import de.ghost143.ghoststatsv1.listeners.PlayerStatsListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class GhostStats_V1 extends JavaPlugin {


    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
        }

        loadDatabaseManager();

        getLogger().info("§aDas Stats System wurde Aktiviert..");

        getCommand("stats").setExecutor(new Stats(this));
        getServer().getPluginManager().registerEvents(new PlayerStatsListener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadDatabaseManager() {
        FileConfiguration config = getConfig();

        String dbHost = config.getString("database.host");
        int dbPort = config.getInt("database.port");
        String dbName = config.getString("database.name");
        String dbUser = config.getString("database.user");
        String dbPassword = config.getString("database.password");

        databaseManager = new DatabaseManager(dbHost, dbPort, dbName, dbUser, dbPassword);
    }
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
