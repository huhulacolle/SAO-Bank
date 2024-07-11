package org.sao.sao_bank.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;
import org.sao.sao_bank.SAO_Bank;
import org.sao.sao_bank.database.Database;
import org.sao.sao_bank.gui.Gui;

import java.io.IOException;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;

public class InventoryManagement implements Listener {

    Database database = SAO_Bank.database;

    @EventHandler
        private void onInventoryClick(InventoryClickEvent e) throws SQLException, IOException {
        if (e.getCurrentItem() != null && e.getView().getTitle().contains("Bank")) {
            int page = Integer.parseInt(e.getInventory().getItem(47).getItemMeta().getLocalizedName());
            if(e.getRawSlot() == 47 && e.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
                new Gui((Player) e.getWhoClicked(), page - 1);
            } else if(e.getRawSlot() == 51 && e.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
                new Gui((Player) e.getWhoClicked(), page + 1);
            } else if (e.getRawSlot() >= 45 && e.getRawSlot() <= 53) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void getInventory(InventoryCloseEvent e) throws SQLException {
        if (e.getView().getTitle().contains("Bank")) {
            String content = InventorySerialize.seriliaze(Arrays.copyOf(e.getInventory().getContents(),
                    e.getInventory().getContents().length-9));

            int page = Integer.parseInt(e.getInventory().getItem(47).getItemMeta().getLocalizedName());

            String query = "INSERT INTO bank_inventory (uuid, num_pages, inventory_content) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE inventory_content = ?";
            PreparedStatement statement = database.getConnection().prepareStatement(query);
            statement.setString(1, e.getPlayer().getUniqueId().toString()); // uuid
            statement.setInt(2, page);  // num_pages
            statement.setString(3, content.isEmpty() ? null : content);
            statement.setString(4, content.isEmpty() ? null : content); // inventory_content
            statement.executeUpdate();
        }
    }
}