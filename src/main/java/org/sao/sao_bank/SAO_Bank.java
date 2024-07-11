package org.sao.sao_bank;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.sao.sao_bank.database.Database;
import org.sao.sao_bank.inventory.InventoryManagement;
import org.sao.sao_bank.listener.EnderChestAnimation;

import java.sql.SQLException;
import java.util.logging.Level;

public final class SAO_Bank extends JavaPlugin {

    public static Database database = new Database("db", 3306, "sao", "docker", "123");

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().log(Level.INFO, "ça marche :D");
        Bukkit.getPluginManager().registerEvents(new EnderChestAnimation(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryManagement(), this);

        try {
            database.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
