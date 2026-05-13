package dev.donutsmp.tpa.request;

import org.bukkit.entity.Player;

import java.util.UUID;

public class Request {
    private final UUID requester;
    private final UUID target;
    private final boolean here; // true if request asks target to teleport to requester (tpahere)
    private final long createdAt;

    public Request(Player requester, Player target, boolean here) {
        this.requester = requester.getUniqueId();
        this.target = target.getUniqueId();
        this.here = here;
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getRequester() { return requester; }
    public UUID getTarget() { return target; }
    public boolean isHere() { return here; }
    public long getCreatedAt() { return createdAt; }
}
