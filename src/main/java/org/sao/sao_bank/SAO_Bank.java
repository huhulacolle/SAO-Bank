package org.sao.sao_bank;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.sao.sao_bank.listener.EnderChestAnimation;

import java.util.logging.Level;

public final class SAO_Bank extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().log(Level.INFO, "ça marche :D");
        Bukkit.getPluginManager().registerEvents(new EnderChestAnimation(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
