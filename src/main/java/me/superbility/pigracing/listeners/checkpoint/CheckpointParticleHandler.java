package com.superdevelopment.pigracing.listeners.checkpoint;

import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.github.fierioziy.particlenativeapi.api.Particles_1_8;
import com.github.fierioziy.particlenativeapi.core.ParticleNativeCore;
import com.github.fierioziy.particlenativeapi.plugin.ParticleNativePlugin;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.superdevelopment.pigracing.Main;
import com.superdevelopment.pigracing.race.Racer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class CheckpointParticleHandler {
    private Main plugin = Main.getPlugin(Main.class);

    ParticleNativeAPI api = ParticleNativeCore.loadAPI(plugin);
    Particles_1_8 particles = api.getParticles_1_8();


}
