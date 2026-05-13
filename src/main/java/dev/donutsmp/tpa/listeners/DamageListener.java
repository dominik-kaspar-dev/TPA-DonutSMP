package dev.donutsmp.tpa.listeners;

import dev.donutsmp.tpa.config.ConfigManager;
import dev.donutsmp.tpa.teleport.TeleportManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {
    private final TeleportManager teleports;
    private final ConfigManager config;

    public DamageListener(TeleportManager teleports, ConfigManager config) {
        this.teleports = teleports;
        this.config = config;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!config.cancelOnDamage()) return;
        if (!(e.getEntity() instanceof org.bukkit.entity.Player)) return;
        org.bukkit.entity.Player p = (org.bukkit.entity.Player) e.getEntity();
        if (teleports.isPending(p.getUniqueId())) teleports.cancelTeleport(p.getUniqueId(), "damage");
    }
}
