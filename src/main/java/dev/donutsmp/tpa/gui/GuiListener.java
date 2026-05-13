package dev.donutsmp.tpa.gui;

import dev.donutsmp.tpa.TPADonutSMPPlugin;
import dev.donutsmp.tpa.request.Request;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {
    private final TPADonutSMPPlugin plugin;

    public GuiListener(TPADonutSMPPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle() == null) return;
        if (!e.getView().getTitle().equals(RequestMenu.TITLE)) return;
        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null) return;
        if (!(e.getWhoClicked() instanceof org.bukkit.entity.Player)) return;
        org.bukkit.entity.Player p = (org.bukkit.entity.Player) e.getWhoClicked();

        Request r = plugin.getRequests().getByTarget(p.getUniqueId());
        if (r == null) { p.closeInventory(); return; }

        int slot = e.getRawSlot();
        if (slot == 11) { // Accept
            plugin.getRequests().removeByTarget(p.getUniqueId());
            plugin.getTeleports().startTeleport(r);
            p.closeInventory();
        } else if (slot == 15) { // Deny
            plugin.getRequests().removeByTarget(p.getUniqueId());
            org.bukkit.entity.Player requester = Bukkit.getPlayer(r.getRequester());
            if (requester != null) requester.sendMessage("Your teleport request was denied.");
            p.sendMessage("You denied the teleport request.");
            p.closeInventory();
        }
    }
}
