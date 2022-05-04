package me.superbility.pigracing.listeners.pig;

import me.superbility.pigracing.Main;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class PigDismountCanceller implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    String dismountMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.DismountMessage"));

    @EventHandler
    private void onDamage(VehicleExitEvent e) {
        if(e.getExited() instanceof Player) {
            LivingEntity entity = (LivingEntity) e.getExited().getVehicle();
            if(entity.hasMetadata("pigrace")) {
                e.getExited().sendMessage(dismountMessage);
                e.setCancelled(true);
            }
        }
    }
}
