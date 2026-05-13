package dev.donutsmp.tpa.request;

import dev.donutsmp.tpa.TPADonutSMPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class RequestManager {
    private final TPADonutSMPPlugin plugin;
    private final Map<UUID, Request> incomingByTarget = new ConcurrentHashMap<>();
    private final Map<UUID, Request> outgoingByRequester = new ConcurrentHashMap<>();
    private final Set<UUID> toggledOff = new ConcurrentSkipListSet<>();

    public RequestManager(TPADonutSMPPlugin plugin) {
        this.plugin = plugin;
    }

    public synchronized boolean addRequest(Request r) {
        UUID requester = r.getRequester();
        UUID target = r.getTarget();

        if (outgoingByRequester.containsKey(requester)) return false;

        outgoingByRequester.put(requester, r);
        incomingByTarget.put(target, r);

        int expire = plugin.getConf().getRequestExpire();
        new BukkitRunnable() {
            @Override
            public void run() {
                Request existing = incomingByTarget.get(target);
                if (existing != null && existing.getRequester().equals(requester)) {
                    removeByRequester(requester);
                    Player p = Bukkit.getPlayer(requester);
                    if (p != null) plugin.getMessages().send(p, "messages.request-expired");
                }
            }
        }.runTaskLater(plugin, expire * 20L);

        return true;
    }

    public Request getByTarget(UUID target) { return incomingByTarget.get(target); }
    public Request getByRequester(UUID requester) { return outgoingByRequester.get(requester); }

    public synchronized void removeByRequester(UUID requester) {
        Request r = outgoingByRequester.remove(requester);
        if (r != null) incomingByTarget.remove(r.getTarget());
    }

    public synchronized void removeByTarget(UUID target) {
        Request r = incomingByTarget.remove(target);
        if (r != null) outgoingByRequester.remove(r.getRequester());
    }

    public synchronized void clearAll() {
        incomingByTarget.clear();
        outgoingByRequester.clear();
    }

    public boolean isToggled(UUID player) { return toggledOff.contains(player); }

    public boolean toggle(UUID player) {
        if (toggledOff.contains(player)) {
            toggledOff.remove(player);
            return false;
        } else {
            toggledOff.add(player);
            return true;
        }
    }
}
