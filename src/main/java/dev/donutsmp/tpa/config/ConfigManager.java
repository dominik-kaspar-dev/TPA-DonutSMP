package dev.donutsmp.tpa.config;

import dev.donutsmp.tpa.TPADonutSMPPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final TPADonutSMPPlugin plugin;
    private final FileConfiguration cfg;

    public ConfigManager(TPADonutSMPPlugin plugin) {
        this.plugin = plugin;
        this.cfg = plugin.getConfig();
    }

    public int getWarmup() { return cfg.getInt("settings.warmup", 5); }
    public int getCooldown() { return cfg.getInt("settings.cooldown", 10); }
    public int getRequestExpire() { return cfg.getInt("settings.request-expire", 60); }
    public boolean cancelOnMove() { return cfg.getBoolean("settings.cancel-on-move", true); }
    public boolean cancelOnDamage() { return cfg.getBoolean("settings.cancel-on-damage", true); }
    public boolean safeTeleportEnabled() { return cfg.getBoolean("settings.safe-teleport-enabled", true); }
    public int safeRadius() { return cfg.getInt("settings.safe-search-radius", 5); }
    public int safeVertical() { return cfg.getInt("settings.safe-search-vertical", 3); }
    public boolean guiEnabled() { return cfg.getBoolean("gui.enabled", true); }

    public void reload() {
        plugin.reloadConfig();
    }
}
