package me.superbility.pigracing.listeners.racers;

import me.superbility.pigracing.Main;
import me.superbility.pigracing.utils.RacerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnectListener implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        if(plugin.race != null) {
            if(RacerUtils.playerInRace(e.getPlayer(), plugin.race.getRacers())) {
                if(e.getPlayer().getVehicle() != null) {
                    if(e.getPlayer().getVehicle().hasMetadata("pigrace")) {
                        e.getPlayer().getVehicle().remove();
                    }
                }
            }
        }
    }
}
