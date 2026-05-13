package dev.donutsmp.tpa.commands;

import dev.donutsmp.tpa.TPADonutSMPPlugin;
import dev.donutsmp.tpa.request.Request;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaCancelCommand implements CommandExecutor {
    private final TPADonutSMPPlugin plugin;

    public TpaCancelCommand(TPADonutSMPPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use this command."); return true; }
        Player p = (Player) sender;
        Request r = plugin.getRequests().getByRequester(p.getUniqueId());
        if (r == null) { p.sendMessage("You have no pending requests."); return true; }
        plugin.getRequests().removeByRequester(p.getUniqueId());
        Player target = Bukkit.getPlayer(r.getTarget());
        if (target != null) target.sendMessage("The teleport request was cancelled.");
        p.sendMessage("Cancelled your outgoing teleport request.");
        return true;
    }
}
