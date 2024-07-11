package org.sao.sao_bank.listener;

import org.sao.sao_bank.gui.Gui;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.sao.sao_bank.SAO_Bank;

import java.io.IOException;
import java.sql.SQLException;

public class EnderChestAnimation implements Listener {

    private final SAO_Bank main;

    public EnderChestAnimation(SAO_Bank main) {
        this.main = main;
    }

    @EventHandler
    private void openAnimation(PlayerInteractEvent e) throws SQLException, IOException {

        if (e.getClickedBlock().getType() == Material.ENDER_CHEST && e.getAction().isRightClick()) {

            e.setCancelled(true);

            new Gui(e.getPlayer(), 1);

            Location enderchestLocation = e.getPlayer().getTargetBlock(null, 5).getLocation();
            e.getPlayer().setMetadata("openedEnderChestLocation", new FixedMetadataValue(main, enderchestLocation));

            PacketContainer openPacket = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);
            openPacket.getBlockPositionModifier().write(0, new BlockPosition(enderchestLocation.getBlockX(), enderchestLocation.getBlockY(), enderchestLocation.getBlockZ()));
            openPacket.getIntegers().write(0, 1);
            openPacket.getIntegers().write(1, 1);
            ProtocolLibrary.getProtocolManager().sendServerPacket(e.getPlayer(), openPacket);

            e.getPlayer().playSound(enderchestLocation, Sound.BLOCK_ENDER_CHEST_OPEN, 0.3f, 1.0f);

        };
    }

    @EventHandler
    private void closeAnimation(InventoryCloseEvent e) {
        if (e.getView().getTitle().contains("Bank")) {
            Location enderchestLocation = (Location) e.getPlayer().getMetadata("openedEnderChestLocation").get(0).value();
            e.getPlayer().removeMetadata("openedEnderChestLocation", main);

            // Send packet to visually close the enderchest
            PacketContainer closePacket = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);
            closePacket.getBlockPositionModifier().write(0, new BlockPosition(enderchestLocation.getBlockX(), enderchestLocation.getBlockY(), enderchestLocation.getBlockZ()));
            closePacket.getIntegers().write(0, 1);  // Type of block action (1 for chest)
            closePacket.getIntegers().write(1, 0);  // Action ID (0 for close)
            ProtocolLibrary.getProtocolManager().sendServerPacket((Player) e.getPlayer(), closePacket);

            ((Player) e.getPlayer()).playSound(enderchestLocation, Sound.BLOCK_ENDER_CHEST_CLOSE, 0.3f, 1.0f);
        }
    }
}
