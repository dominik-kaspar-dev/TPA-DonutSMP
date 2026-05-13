package dev.donutsmp.tpa.commands;

import dev.donutsmp.tpa.TPADonutSMPPlugin;
import dev.donutsmp.tpa.request.Request;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

public class TpaCommand implements CommandExecutor {
    private final TPADonutSMPPlugin plugin;

    public TpaCommand(TPADonutSMPPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use this command."); return true; }
        Player p = (Player) sender;
        if (!p.hasPermission("tpa.use")) { p.sendMessage("No permission."); return true; }
        if (args.length != 1) { p.sendMessage("Usage: /tpa <player>"); return true; }
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) { p.sendMessage("Player not found."); return true; }
        if (target.getUniqueId().equals(p.getUniqueId())) { p.sendMessage("Cannot request yourself."); return true; }

        if (plugin.getRequests().isToggled(target.getUniqueId())) {
            plugin.getMessages().send(p, "messages.toggle-disabled");
            return true;
        }

        Request r = new Request(p, target, false);
        boolean ok = plugin.getRequests().addRequest(r);
        if (!ok) { p.sendMessage("You already have a pending request."); return true; }

        String sentRaw = plugin.getConfig().getString("messages.request-sent", "Sent a teleport request to <player>.");
        sentRaw = sentRaw.replace("<player>", target.getName());
        p.sendMessage(plugin.getMessages().parse(sentRaw));

        if (plugin.getConf().guiEnabled()) {
            dev.donutsmp.tpa.gui.RequestMenu.open(r, plugin);
        } else {
            Component accept = Component.text("[ACCEPT]", NamedTextColor.GREEN).clickEvent(ClickEvent.runCommand("/tpaccept"));
            Component deny = Component.text("[DENY]", NamedTextColor.RED).clickEvent(ClickEvent.runCommand("/tpdeny"));
            target.sendMessage(plugin.getMessages().parse("<gold>" + p.getName() + " sent you a teleport request.</gold>"));
            target.sendMessage(accept.append(Component.text(" ")).append(deny));
        }

        return true;
    }
}
