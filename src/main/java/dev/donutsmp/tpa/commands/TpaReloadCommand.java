package dev.donutsmp.tpa.commands;

import dev.donutsmp.tpa.TPADonutSMPPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TpaReloadCommand implements CommandExecutor {
    private final TPADonutSMPPlugin plugin;

    public TpaReloadCommand(TPADonutSMPPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tpa.admin")) { sender.sendMessage("No permission."); return true; }
        plugin.getConf().reload();
        sender.sendMessage("TPA config reloaded.");
        return true;
    }
}
