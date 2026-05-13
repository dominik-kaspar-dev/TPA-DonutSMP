package dev.donutsmp.tpa.commands;

import dev.donutsmp.tpa.TPADonutSMPPlugin;
import dev.donutsmp.tpa.request.Request;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaDenyCommand implements CommandExecutor {
    private final TPADonutSMPPlugin plugin;

    public TpaDenyCommand(TPADonutSMPPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use this command."); return true; }
        Player p = (Player) sender;
        Request r = plugin.getRequests().getByTarget(p.getUniqueId());
        if (r == null) { p.sendMessage("No incoming requests."); return true; }

        plugin.getRequests().removeByTarget(p.getUniqueId());
        Player requester = Bukkit.getPlayer(r.getRequester());
        if (requester != null) requester.sendMessage("Your teleport request was denied.");
        p.sendMessage("You denied the teleport request.");
        return true;
    }
}
