package com.superdevelopment.pigracing.listeners.pig;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.superdevelopment.pigracing.Main;
import com.superdevelopment.pigracing.listeners.checkpoint.CheckPointPassEventListener;
import com.superdevelopment.pigracing.race.PigRace;
import com.superdevelopment.pigracing.race.RaceState;
import com.superdevelopment.pigracing.race.Racer;
import com.superdevelopment.pigracing.utils.RacerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.Map;

public class PigHitGroundListener implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    private void onHitGround(PlayerMoveEvent e) {
        PigRace race = plugin.race;
        if(race != null) {
            if (race.getGameState() == RaceState.INPROGRESS) {

                Player player = e.getPlayer();
                List<Racer> racers = race.getRacers();

                if (RacerUtils.playerInRace(player, racers)) {
                    Racer racer = RacerUtils.getRacerFromPlayer(player, racers);
                    int racerCheckpoint = racer.getCheckpoint();

                    Pig pig = (Pig) player.getVehicle();

                    if(pig.isOnGround()) { //Pig on ground
                        Material mat = pig.getLocation().clone().getBlock().getType();
                        if(mat != Material.CARPET) {
                            CuboidSelection checkpoint = getKey(CheckPointPassEventListener.checkpointRegions, racerCheckpoint);
                            Location minPoint = checkpoint.getMinimumPoint();
                            Location maxPoint = checkpoint.getMaximumPoint();

                            Location location = new Location(checkpoint.getWorld(),
                                    (maxPoint.getX() + minPoint.getX()) / 2,
                                    (maxPoint.getY() + (minPoint.getY() / 2)) + 5,
                                    (maxPoint.getZ() + minPoint.getZ()) / 2);

                            pig.eject();
                            pig.teleport(location);
                            player.teleport(location);
                            pig.setPassenger(player);
                            player.playSound(location, Sound.VILLAGER_DEATH, 1, 1);
                        }
                    }
                }
            }
        }
    }
    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
