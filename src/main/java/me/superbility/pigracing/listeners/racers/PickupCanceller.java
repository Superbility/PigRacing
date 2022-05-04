package me.superbility.pigracing.listeners.racers;

import me.superbility.pigracing.Main;
import me.superbility.pigracing.race.PigRace;
import me.superbility.pigracing.utils.RacerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PickupCanceller implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    private void onPickup(PlayerPickupItemEvent e) {
        if(plugin.race != null) {
            PigRace race = plugin.race;
            if (RacerUtils.playerInRace(e.getPlayer(), race.getRacers())) {
                e.setCancelled(true);
            }
        }
    }
}
