package me.superbility.pigracing.listeners.racers;

import me.superbility.pigracing.Main;
import me.superbility.pigracing.race.PigRace;
import me.superbility.pigracing.utils.RacerUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FallResistance implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    private void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (plugin.race != null) {
            PigRace race = plugin.race;
            if (RacerUtils.playerInRace(player, race.getRacers())) {
                if (player.getVehicle() != null) {
                    LivingEntity pig = (LivingEntity) player.getVehicle();
                    if (pig.getVelocity().getY() < -0.3) {
                        pig.setVelocity(pig.getVelocity().setY(-0.3));
                    }
                    if (pig.getVelocity().getY() > 0.1) {
                        pig.setVelocity(pig.getVelocity().setY(0.1));
                    }
                }
            }
        }
    }
}