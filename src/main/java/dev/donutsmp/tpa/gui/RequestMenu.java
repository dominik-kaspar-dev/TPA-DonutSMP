package dev.donutsmp.tpa.gui;

import dev.donutsmp.tpa.TPADonutSMPPlugin;
import dev.donutsmp.tpa.request.Request;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class RequestMenu {
    public static final String TITLE = "TPA Request";

    public static void open(Request r, TPADonutSMPPlugin plugin) {
        Player requester = Bukkit.getPlayer(r.getRequester());
        Player target = Bukkit.getPlayer(r.getTarget());
        if (requester == null || target == null) return;

        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        // Center skull at slot 13
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) skull.getItemMeta();
        sm.setOwningPlayer(requester);
        sm.setDisplayName(ChatColor.GREEN + requester.getName());
        skull.setItemMeta(sm);
        inv.setItem(13, skull);

        // Accept (slot 11)
        ItemStack accept = new ItemStack(Material.GREEN_WOOL);
        ItemMeta am = accept.getItemMeta();
        am.setDisplayName(ChatColor.GREEN + "Accept");
        accept.setItemMeta(am);
        inv.setItem(11, accept);

        // Deny (slot 15)
        ItemStack deny = new ItemStack(Material.RED_WOOL);
        ItemMeta dm = deny.getItemMeta();
        dm.setDisplayName(ChatColor.RED + "Deny");
        deny.setItemMeta(dm);
        inv.setItem(15, deny);

        target.openInventory(inv);
    }
}
