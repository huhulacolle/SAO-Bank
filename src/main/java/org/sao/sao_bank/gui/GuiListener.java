package org.sao.sao_bank.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getInventory() !=  null && event.getCurrentItem() != null && event.getView().getTitle().contains("Bank")) {
            int page = Integer.parseInt(event.getInventory().getItem(47).getItemMeta().getLocalizedName());
            if(event.getRawSlot() == 47 && event.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
                new Gui((Player) event.getWhoClicked(), page - 1);
            } else if(event.getRawSlot() == 51 && event.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
                new Gui((Player) event.getWhoClicked(), page + 1);
            } else if (event.getRawSlot() >= 45 && event.getRawSlot() <= 53) {
                event.setCancelled(true);
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChestRightClicked(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getClickedBlock().getType() == Material.ENDER_CHEST && playerInteractEvent.getAction().isRightClick()) {
            playerInteractEvent.getPlayer().sendMessage("You right clicked an ender chest");
            playerInteractEvent.setCancelled(true);
            new Gui(playerInteractEvent.getPlayer(), 1);
        }
    }
}
