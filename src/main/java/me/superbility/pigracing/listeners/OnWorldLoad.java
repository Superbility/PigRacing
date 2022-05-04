package com.superdevelopment.pigracing.listeners;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.superdevelopment.pigracing.Main;
import com.superdevelopment.pigracing.listeners.checkpoint.CheckPointPassEventListener;
import com.superdevelopment.pigracing.utils.LocationSerialiser;
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
