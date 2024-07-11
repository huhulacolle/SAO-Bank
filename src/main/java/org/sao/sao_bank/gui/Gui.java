package org.sao.sao_bank.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.sao.sao_bank.SAO_Bank;
import org.sao.sao_bank.database.Database;
import org.sao.sao_bank.inventory.InventorySerialize;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Gui {
    Inventory guiBank;
    Database database = SAO_Bank.database;
    public Gui(Player player, int page) throws SQLException, IOException {
        Inventory gui = Bukkit.createInventory(null, 9*6, "Bank Page - "+ page);

        List<ItemStack> allItems = new ArrayList<>();
        for (int i = 0; i < 135; i++) {
            allItems.add(player.getEnderChest().getItem(i));
        }

        ItemStack left;
        ItemMeta leftMeta;
        if (PageUtil.isPageValid(allItems, page - 1, 52)) {
            left = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            leftMeta = left.getItemMeta();
            leftMeta.setDisplayName(ChatColor.GREEN + "Go page left");
        } else {
            left = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            leftMeta = left.getItemMeta();
            leftMeta.setDisplayName(ChatColor.RED + "No more pages left");
        }
        leftMeta.setLocalizedName(page + "");
        left.setItemMeta(leftMeta);
        gui.setItem(9*5+2, left);

        ItemStack right;
        ItemMeta rightMeta;
        if (PageUtil.isPageValid(allItems, page + 1, 52)) {
            right = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            rightMeta = left.getItemMeta();
            rightMeta.setDisplayName(ChatColor.GREEN + "Go page right");
        } else {
            right = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            rightMeta = left.getItemMeta();
            rightMeta.setDisplayName(ChatColor.RED + "No more pages left");
        }
        right.setItemMeta(rightMeta);
        gui.setItem(9*5+6, right);

        for(ItemStack is : PageUtil.getPageItems(allItems, page, 52)) {
            gui.setItem(gui.firstEmpty(), is);
        }


        ItemStack unusableSlot;
        ItemMeta unusableSlotMeta;
        unusableSlot = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        unusableSlotMeta = left.getItemMeta();
        unusableSlotMeta.setDisplayName(ChatColor.GRAY + "Unusable Slot");
        unusableSlot.setItemMeta(unusableSlotMeta);
        gui.setItem(9*5, unusableSlot);
        gui.setItem(9*5+1, unusableSlot);
        gui.setItem(9*5+3, unusableSlot);
        gui.setItem(9*5+5, unusableSlot);
        gui.setItem(9*5+7, unusableSlot);
        gui.setItem(9*5+8, unusableSlot);

        ItemStack pageNumber;
        ItemMeta pageNumberMeta;
        pageNumber = new ItemStack(Material.PAPER);
        pageNumberMeta = pageNumber.getItemMeta();
        pageNumberMeta.setDisplayName("Page: " + page);
        pageNumber.setItemMeta(pageNumberMeta);
        gui.setItem(9*5+4, pageNumber);

        String query = "SELECT * FROM bank_inventory WHERE uuid = ? AND num_pages = ?";
        PreparedStatement statement = database.getConnection().prepareStatement(query);
        statement.setString(1, player.getUniqueId().toString()); // uuid
        statement.setInt(2, page);  //
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            ItemStack[] item = InventorySerialize.deseriliaze(resultSet.getString("inventory_content"));
            for (int i = 0; i < item.length; i++) {
                gui.setItem(i, item[i]);
            }
        }

        player.openInventory(gui);
    }
}