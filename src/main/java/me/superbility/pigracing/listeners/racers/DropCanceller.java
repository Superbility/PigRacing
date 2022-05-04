package com.superdevelopment.pigracing.listeners.racers;

import com.superdevelopment.pigracing.Main;
import com.superdevelopment.pigracing.race.PigRace;
import com.superdevelopment.pigracing.utils.RacerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class DropCanceller implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    private void onPickup(PlayerDropItemEvent e) {
        if(plugin.race != null) {
            PigRace race = plugin.race;
            if (RacerUtils.playerInRace(e.getPlayer(), race.getRacers())) {
                e.setCancelled(true);
            }
        }
    }
}
