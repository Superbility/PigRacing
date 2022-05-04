package me.superbility.pigracing.listeners;

import me.superbility.pigracing.Main;
import me.superbility.pigracing.race.PigRace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Disconnect implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    public class InWaiting implements Listener {
        @EventHandler
        private void onDisconnect(PlayerQuitEvent e) {
            PigRace race = plugin.race;
            if(race != null) {
                race.removePlayer(e.getPlayer(), false);
            }
        }
    }
    public class InRace {

    }
}
