package dev.donutsmp.tpa.listeners;

import dev.donutsmp.tpa.request.RequestManager;
import dev.donutsmp.tpa.teleport.TeleportManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class QuitListener implements Listener {
    private final RequestManager requests;
    private final TeleportManager teleports;

    public QuitListener(RequestManager requests, TeleportManager teleports) {
        this.requests = requests;
        this.teleports = teleports;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        UUID id = e.getPlayer().getUniqueId();
        requests.removeByRequester(id);
        requests.removeByTarget(id);
        teleports.cancelTeleport(id, "player quit");
    }
}
