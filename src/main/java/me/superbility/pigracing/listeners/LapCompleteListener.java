package me.superbility.pigracing.listeners;

import me.superbility.pigracing.Main;
import me.superbility.pigracing.listeners.checkpoint.CheckpointPassEvent;
import me.superbility.pigracing.race.Racer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LapCompleteListener implements Listener {
    private Main plugin = Main.getPlugin(Main.class);

    private String lapComplete = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.LapComplete"));
    private String completeBroadcast = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.CompleteBroadcast"));

    @EventHandler
    private void onCheckpointPassed(CheckpointPassEvent e) {
        if(e.getRacer().getCheckpoint() == e.getRace().getMaxChekpoints()) {
            Racer racer = e.getRacer();

            racer.setCheckpoint(0);
            racer.addLap();
            racer.getPlayer().sendMessage(lapComplete.replace("{lap}", String.valueOf(racer.getLap())));

            if(racer.getLap() == 3) {
                e.getRace().setRacerCompleted(racer);
                racer.setInGame(false);
                racer.getPlayer().getVehicle().remove();
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.sendMessage(completeBroadcast.replace("{player}", racer.getPlayer().getName()).replace("{position}", String.valueOf(e.getRace().getRacerPosition(racer))));
                }
            }
        }
    }
}