package me.superbility.pigracing.listeners;

import me.superbility.pigracing.Main;
import me.superbility.pigracing.listeners.checkpoint.CheckPointPassEventListener;
import me.superbility.pigracing.utils.LocationSerialiser;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class OnWorldLoad implements Listener {
    private Main plugin = Main.getPlugin(Main.class);
    private CheckPointPassEventListener checkPointPassEventListener = new CheckPointPassEventListener();

    @EventHandler
    private void onLoad(WorldLoadEvent e) {
        boolean load = false;

        for (String s : plugin.getConfig().getConfigurationSection("Data.Checkpoints").getKeys(false)) {
            Location loc1 = LocationSerialiser.getLiteLocationFromString(plugin.getConfig().getString("Data.Checkpoints." + s + ".Loc1"));

            if(loc1.getWorld().equals(loc1.getWorld())) load = true;
        }

        if(load) checkPointPassEventListener.resetRegions();
    }
}
