package de.redstoneworld.redspectateteleport;

import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class RedSpectateTeleport extends JavaPlugin implements Listener {

    private BaseComponent[] denyMessage;

    @Override
    public void onEnable() {
        loadConfig();
        getCommand("redspectateteleport").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    public void loadConfig() {
        saveDefaultConfig();
        reloadConfig();
        denyMessage = MineDown.parse(getConfig().getString("lang.noPermissions"));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            if ("reload".equalsIgnoreCase(args[0]) && sender.hasPermission("rwm.spectateteleport.command.reload")) {
                loadConfig();
                sender.sendMessage(ChatColor.YELLOW + "Config reloaded!");
                return true;
            }
        }
        sender.sendMessage(ChatColor.AQUA + getName() + " " + ChatColor.YELLOW + getDescription().getVersion());
        return false;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSpectateTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE && !event.getPlayer().hasPermission("rwm.spectateteleport.use")) {
            event.setCancelled(true);
            event.getPlayer().spigot().sendMessage(denyMessage);
        }
    }
}
