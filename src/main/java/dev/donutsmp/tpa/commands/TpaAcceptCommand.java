package dev.donutsmp.tpa.commands;

import dev.donutsmp.tpa.TPADonutSMPPlugin;
import dev.donutsmp.tpa.request.Request;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaAcceptCommand implements CommandExecutor {
    private final TPADonutSMPPlugin plugin;

    public TpaAcceptCommand(TPADonutSMPPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use this command."); return true; }
        Player p = (Player) sender;
        Request r = plugin.getRequests().getByTarget(p.getUniqueId());
        if (r == null) { p.sendMessage("No incoming requests."); return true; }

        if (plugin.getConf().guiEnabled()) {
            dev.donutsmp.tpa.gui.RequestMenu.open(r, plugin);
            return true;
        }

        // remove request and start teleport
        plugin.getRequests().removeByTarget(p.getUniqueId());
        plugin.getTeleports().startTeleport(r);
        return true;
    }
}
