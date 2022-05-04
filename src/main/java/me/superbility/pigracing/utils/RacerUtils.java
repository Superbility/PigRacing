package com.superdevelopment.pigracing.utils;

import com.superdevelopment.pigracing.race.Racer;
import org.bukkit.entity.Player;

import java.util.List;

public class RacerUtils {
    public static boolean playerInRace(Player player, List<Racer> racers){
        for(Racer r : racers) {
            if(player.getUniqueId().equals(r.getPlayer().getUniqueId())) {
                return true;
            }
        }
        return false;
    }
    public static Racer getRacerFromPlayer(Player player, List<Racer> racers) {
        for(Racer r : racers) {
            if(r.getPlayer() == player) {
                return r;
            }
        }
        return null;
    }
}
