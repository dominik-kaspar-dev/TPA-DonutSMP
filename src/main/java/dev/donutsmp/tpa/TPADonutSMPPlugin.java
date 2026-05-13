package dev.donutsmp.tpa;

import dev.donutsmp.tpa.config.ConfigManager;
import dev.donutsmp.tpa.request.RequestManager;
import dev.donutsmp.tpa.teleport.TeleportManager;
import dev.donutsmp.tpa.util.MessageService;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;

public class TPADonutSMPPlugin extends JavaPlugin {
    private static TPADonutSMPPlugin instance;
    private ConfigManager configManager;
    private RequestManager requestManager;
    private TeleportManager teleportManager;
    private MessageService messageService;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        this.messageService = new MessageService(this);
        this.requestManager = new RequestManager(this);
        this.teleportManager = new TeleportManager(this, requestManager);

        // Commands
        getCommand("tpa").setExecutor(new dev.donutsmp.tpa.commands.TpaCommand(this));
        getCommand("tpahere").setExecutor(new dev.donutsmp.tpa.commands.TpahereCommand(this));
        getCommand("tpaccept").setExecutor(new dev.donutsmp.tpa.commands.TpaAcceptCommand(this));
        getCommand("tpdeny").setExecutor(new dev.donutsmp.tpa.commands.TpaDenyCommand(this));
        getCommand("tpacancel").setExecutor(new dev.donutsmp.tpa.commands.TpaCancelCommand(this));
        getCommand("tpatoggle").setExecutor(new dev.donutsmp.tpa.commands.TpaToggleCommand(this));
        getCommand("tpareload").setExecutor(new dev.donutsmp.tpa.commands.TpaReloadCommand(this));

        // Listeners
        getServer().getPluginManager().registerEvents(new dev.donutsmp.tpa.listeners.QuitListener(requestManager, teleportManager), this);
        getServer().getPluginManager().registerEvents(new dev.donutsmp.tpa.listeners.MoveListener(teleportManager, configManager), this);
        getServer().getPluginManager().registerEvents(new dev.donutsmp.tpa.listeners.DamageListener(teleportManager, configManager), this);
        getServer().getPluginManager().registerEvents(new dev.donutsmp.tpa.gui.GuiListener(this), this);

        // bStats
        try {
            new Metrics(this, 0000); // TODO: replace with real bStats id
        } catch (Throwable ignored) {}
    }

    @Override
    public void onDisable() {
        requestManager.clearAll();
        teleportManager.cancelAll();
    }

    public static TPADonutSMPPlugin getInstance() {
        return instance;
    }

    public ConfigManager getConf() { return configManager; }
    public RequestManager getRequests() { return requestManager; }
    public TeleportManager getTeleports() { return teleportManager; }
    public MessageService getMessages() { return messageService; }
}
