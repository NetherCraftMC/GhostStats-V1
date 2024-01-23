package de.ghost143.ghoststatsv1.database;

import org.bukkit.Bukkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private Connection connection;

    public DatabaseManager(String host, int port, String database, String user, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
            connection = DriverManager.getConnection(url, user, password);
            createTable();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("§cVerbindung zur Datenbank konnte nicht hergestellt werden!");
        }
    }

    private void createTable() {
        try {
            String statsTableSql = "CREATE TABLE IF NOT EXISTS player_stats (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "uuid VARCHAR(36) NOT NULL," +
                    "kills INT DEFAULT 0," +
                    "deaths INT DEFAULT 0)";
            PreparedStatement statsStatement = connection.prepareStatement(statsTableSql);
            statsStatement.executeUpdate();
            statsStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayerIfNotExists(String uuid) {
        if (!playerExists(uuid)) {
            createPlayer(uuid);
        }
    }

    public void createPlayer(String uuid) {
        try {
            String sql = "INSERT INTO player_stats (uuid, kills, deaths) VALUES (?, 0, 0)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, uuid);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean playerExists(String uuid) {
        try {
            String sql = "SELECT uuid FROM player_stats WHERE uuid = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, uuid);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void incrementKills(String uuid) {
        try {
            String sql = "UPDATE player_stats SET kills = kills + 1 WHERE uuid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementDeaths(String uuid) {
        try {
            String sql = "UPDATE player_stats SET deaths = deaths + 1 WHERE uuid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPlayerKills(String uuid) {
        return getStat(uuid, "kills");
    }

    public int getPlayerDeaths(String uuid) {
        return getStat(uuid, "deaths");
    }

    private int getStat(String uuid, String stat) {
        try {
            String sql = "SELECT " + stat + " FROM player_stats WHERE uuid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(stat);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}