package dev.donutsmp.tpa.commands;

import dev.donutsmp.tpa.TPADonutSMPPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaToggleCommand implements CommandExecutor {
    private final TPADonutSMPPlugin plugin;

    public TpaToggleCommand(TPADonutSMPPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use this command."); return true; }
        Player p = (Player) sender;
        boolean newState = plugin.getRequests().toggle(p.getUniqueId());
        if (newState) plugin.getMessages().send(p, "messages.toggle-disabled"); else plugin.getMessages().send(p, "messages.toggle-enabled");
        return true;
    }
}
