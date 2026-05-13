package dev.donutsmp.tpa.commands;

import dev.donutsmp.tpa.TPADonutSMPPlugin;
import dev.donutsmp.tpa.request.Request;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpahereCommand implements CommandExecutor {
    private final TPADonutSMPPlugin plugin;

    public TpahereCommand(TPADonutSMPPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("tpa.use")) {
            p.sendMessage("You don't have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            p.sendMessage("Usage: /tpahere <player>");
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            p.sendMessage("Player not found or offline.");
            return true;
        }

        if (target.getUniqueId().equals(p.getUniqueId())) {
            p.sendMessage("You cannot send a request to yourself.");
            return true;
        }

        Request r = new Request(p, target, true);
        boolean ok = plugin.getRequests().addRequest(r);
        if (!ok) {
            p.sendMessage("You already have a pending TPA request.");
            return true;
        }

        String sentRaw = plugin.getConfig().getString("messages.request-sent", "Sent request to <player>.");
        sentRaw = sentRaw.replace("<player>", target.getName()).replace("<time>", String.valueOf(plugin.getConf().getRequestExpire()));
        p.sendMessage(plugin.getMessages().parse(sentRaw));

        if (plugin.getConf().guiEnabled()) {
            dev.donutsmp.tpa.gui.RequestMenu.open(r, plugin);
        } else {
            String recvRaw = plugin.getConfig().getString("messages.request-received", "<player> sent you a teleport request.");
            recvRaw = recvRaw.replace("<player>", p.getName()).replace("<time>", String.valueOf(plugin.getConf().getRequestExpire()));
            target.sendMessage(plugin.getMessages().parse(recvRaw));
        }

        return true;
    }
}
