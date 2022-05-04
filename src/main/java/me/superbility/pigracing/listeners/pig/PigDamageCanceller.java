package me.superbility.pigracing.listeners.pig;

import me.superbility.pigracing.Main;
import me.superbility.pigracing.utils.RacerUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PigDamageCanceller implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    private void onDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) e.getEntity();
            if(entity.hasMetadata("pigrace")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof LivingEntity && e.getDamager() instanceof Player) {
            LivingEntity entity = (LivingEntity) e.getEntity();
            Player damager = (Player) e.getDamager();
            if(entity.hasMetadata("pigrace")) {
                if (plugin.race != null) {
                    if (RacerUtils.playerInRace(damager, plugin.race.getRacers())) {
                        e.setDamage(0);
                        entity.setVelocity(damager.getLocation().getDirection().setY(0).normalize().multiply(1));
                    }
                }
            }
        }
    }
}
