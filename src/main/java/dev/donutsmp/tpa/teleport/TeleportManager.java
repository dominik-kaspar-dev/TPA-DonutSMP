package dev.donutsmp.tpa.teleport;

import dev.donutsmp.tpa.TPADonutSMPPlugin;
import dev.donutsmp.tpa.request.Request;
import dev.donutsmp.tpa.request.RequestManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportManager {
    private final TPADonutSMPPlugin plugin;
    private final RequestManager requests;
    private final Map<UUID, BukkitTask> pending = new HashMap<>();
    private final Map<UUID, Location> origin = new HashMap<>();
    private final Map<UUID, Long> lastUsed = new HashMap<>();

    public TeleportManager(TPADonutSMPPlugin plugin, RequestManager requests) {
        this.plugin = plugin;
        this.requests = requests;
    }

    public synchronized boolean isPending(UUID p) { return pending.containsKey(p); }

    public synchronized void startTeleport(Request r) {
        Player requester = Bukkit.getPlayer(r.getRequester());
        Player target = Bukkit.getPlayer(r.getTarget());
        if (requester == null || target == null) return;

        Player teleporter = r.isHere() ? target : requester;
        Location destination = r.isHere() ? requester.getLocation() : target.getLocation();

        // cooldown check
        int cooldown = plugin.getConf().getCooldown();
        if (!teleporter.hasPermission("tpa.bypass.cooldown")) {
            long last = lastUsed.getOrDefault(teleporter.getUniqueId(), 0L);
            long since = (System.currentTimeMillis() - last) / 1000L;
            if (since < cooldown) {
                teleporter.sendMessage(Component.text("You must wait " + (cooldown - since) + "s before teleporting."));
                return;
            }
        }

        // safeplace check
        if (plugin.getConf().safeTeleportEnabled()) {
            if (!isSafe(destination)) {
                Location safe = findNearestSafe(destination, plugin.getConf().safeRadius(), plugin.getConf().safeVertical());
                if (safe == null) {
                    plugin.getMessages().send(teleporter, "messages.no-safe-spot");
                    return;
                }
                destination = safe;
            }
        }

        int warmup = plugin.getConf().getWarmup();
        if (warmup > 0 && !teleporter.hasPermission("tpa.bypass.warmup")) {
            origin.put(teleporter.getUniqueId(), teleporter.getLocation());
            plugin.getMessages().send(teleporter, "messages.teleport-start");
            
            // Create final copies for use in inner class
            final Player finalTeleporter = teleporter;
            final Location finalDestination = destination;
            
            BukkitTask task = new BukkitRunnable() {
                int left = warmup;
                @Override
                public void run() {
                    if (!finalTeleporter.isOnline()) { cancelTeleport(finalTeleporter.getUniqueId(), "player offline"); return; }
                    if (!finalTeleporter.getLocation().equals(origin.get(finalTeleporter.getUniqueId()))) {
                        cancelTeleport(finalTeleporter.getUniqueId(), "moved");
                        return;
                    }
                    if (left-- <= 0) {
                        doTeleport(finalTeleporter, finalDestination);
                        pending.remove(finalTeleporter.getUniqueId());
                        origin.remove(finalTeleporter.getUniqueId());
                        this.cancel();
                        return;
                    }
                }
            }.runTaskTimer(plugin, 20L, 20L);

            pending.put(teleporter.getUniqueId(), task);
        } else {
            doTeleport(teleporter, destination);
        }

        lastUsed.put(teleporter.getUniqueId(), System.currentTimeMillis());
    }

    private void doTeleport(Player who, Location dest) {
        Bukkit.getScheduler().runTask(plugin, () -> who.teleport(dest));
        who.sendMessage(Component.text("Teleported."));
    }

    public synchronized void cancelTeleport(UUID player, String reason) {
        BukkitTask t = pending.remove(player);
        if (t != null) t.cancel();
        origin.remove(player);
        Player p = Bukkit.getPlayer(player);
        if (p != null) p.sendMessage(Component.text("Teleport cancelled."));
    }

    public synchronized void cancelAll() {
        for (BukkitTask t : pending.values()) t.cancel();
        pending.clear();
        origin.clear();
    }

    private boolean isSafe(Location loc) {
        if (loc.getY() <= 0) return false;
        Block feet = loc.getBlock();
        Block head = loc.clone().add(0,1,0).getBlock();
        Block below = loc.clone().add(0,-1,0).getBlock();

        if (!isPassable(feet) || !isPassable(head)) return false;
        if (isDangerous(below) || isDangerous(feet) || isDangerous(head)) return false;
        return true;
    }

    private boolean isPassable(Block b) {
        Material m = b.getType();
        if (m == Material.AIR || m == Material.CAVE_AIR || m == Material.VOID_AIR) return true;
        if (m == Material.WATER) return true;
        return !m.isSolid();
    }

    private boolean isDangerous(Block b) {
        Material m = b.getType();
        String name = m.name();
        if (name.contains("LAVA")) return true;
        if (m == Material.FIRE || m == Material.CAMPFIRE || m == Material.SOUL_CAMPFIRE) return true;
        if (m == Material.MAGMA_BLOCK || m == Material.CACTUS) return true;
        return false;
    }

    private Location findNearestSafe(Location originLoc, int radius, int vertical) {
        for (int r = 0; r <= radius; r++) {
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    for (int dy = -vertical; dy <= vertical; dy++) {
                        Location l = originLoc.clone().add(dx, dy, dz);
                        if (isSafe(l)) return l;
                    }
                }
            }
        }
        return null;
    }

    public TPADonutSMPPlugin getPlugin() { return plugin; }
}
