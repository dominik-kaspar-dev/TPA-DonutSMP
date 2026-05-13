package dev.donutsmp.tpa.util;

import dev.donutsmp.tpa.TPADonutSMPPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class MessageService {
    private final TPADonutSMPPlugin plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public MessageService(TPADonutSMPPlugin plugin) {
        this.plugin = plugin;
    }

    public Component parse(String raw) {
        return mm.deserialize(raw == null ? "" : raw);
    }

    public void send(Player p, String key) {
        String raw = plugin.getConfig().getString(key, "");
        if (raw == null) return;
        // Try to use PlaceholderAPI if available (via reflection to avoid hard dependency)
        try {
            if (plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
                Class<?> placeholderAPI = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
                java.lang.reflect.Method setPlaceholders = placeholderAPI.getMethod("setPlaceholders", Player.class, String.class);
                raw = (String) setPlaceholders.invoke(null, p, raw);
            }
        } catch (Throwable ignored) {}
        p.sendMessage(parse(raw));
    }
}
