package me.superbility.pigracing.listeners.checkpoint;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import me.superbility.pigracing.Main;
import me.superbility.pigracing.race.PigRace;
import me.superbility.pigracing.race.RaceState;
import me.superbility.pigracing.race.Racer;
import me.superbility.pigracing.utils.LocationSerialiser;
import me.superbility.pigracing.utils.RacerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.List;

public class CheckPointPassEventListener implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    public static HashMap<CuboidSelection, Integer> checkpointRegions = new HashMap<CuboidSelection, Integer>();

    public void resetRegions() {
        checkpointRegions.clear();
        if(plugin.getConfig().contains("Data.Checkpoints")) {
            for (String s : plugin.getConfig().getConfigurationSection("Data.Checkpoints").getKeys(false)) {
                Location loc1 = LocationSerialiser.getLiteLocationFromString(plugin.getConfig().getString("Data.Checkpoints." + s + ".Loc1"));
                Location loc2 = LocationSerialiser.getLiteLocationFromString(plugin.getConfig().getString("Data.Checkpoints." + s + ".Loc2"));

                CuboidSelection region = new CuboidSelection(loc1.getWorld(), loc1, loc2);
                checkpointRegions.put(region, Integer.valueOf(s));
            }
        }
    }

    @EventHandler
    private void onMove(PlayerMoveEvent e) {
        PigRace race = plugin.race;
        if(race != null) {
            if(race.getGameState() == RaceState.INPROGRESS) {

                Player player = e.getPlayer();
                List<Racer> racers = race.getRacers();

                if(RacerUtils.playerInRace(player, racers)) {
                    Racer racer = RacerUtils.getRacerFromPlayer(player, racers);
                    int racerCheckpoint = racer.getCheckpoint();

                    if (racer.getLap() < 3) {
                        for (CuboidSelection selection : checkpointRegions.keySet()) {
                            if (player.getLocation().toVector().isInAABB(selection.getMinimumPoint().toVector(), selection.getMaximumPoint().toVector())) { //TODO PROBLEM HERE - TRY MAKING THE SELECTION 3 BLOCKS WIDE
                                int currentCheckpoint = checkpointRegions.get(selection);
                                if (racerCheckpoint + 1 == currentCheckpoint) { //Checks if right checkpoint
                                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 1);
                                    racer.nextCheckpoint(checkpointRegions);

                                    CheckpointPassEvent checkpointPassEvent = new CheckpointPassEvent(racer, race);
                                    Bukkit.getPluginManager().callEvent(checkpointPassEvent);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
 }
