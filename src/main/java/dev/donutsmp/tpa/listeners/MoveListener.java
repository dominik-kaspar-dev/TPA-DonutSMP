package dev.donutsmp.tpa.listeners;

import dev.donutsmp.tpa.config.ConfigManager;
import dev.donutsmp.tpa.teleport.TeleportManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {
    private final TeleportManager teleports;
    private final ConfigManager config;

    public MoveListener(TeleportManager teleports, ConfigManager config) {
        this.teleports = teleports;
        this.config = config;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!config.cancelOnMove()) return;
        if (teleports.isPending(e.getPlayer().getUniqueId())) {
            teleports.cancelTeleport(e.getPlayer().getUniqueId(), "moved");
        }
    }
}
