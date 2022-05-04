package com.superdevelopment.pigracing;

import com.superdevelopment.pigracing.commands.MainCommand;
import com.superdevelopment.pigracing.listeners.Disconnect;
import com.superdevelopment.pigracing.listeners.LapCompleteListener;
import com.superdevelopment.pigracing.listeners.OnWorldLoad;
import com.superdevelopment.pigracing.listeners.checkpoint.CheckPointPassEventListener;
import com.superdevelopment.pigracing.listeners.checkpoint.CheckpointParticleHandler;
import com.superdevelopment.pigracing.listeners.pig.PigDamageCanceller;
import com.superdevelopment.pigracing.listeners.pig.PigDismountCanceller;
import com.superdevelopment.pigracing.listeners.pig.PigHitGroundListener;
import com.superdevelopment.pigracing.listeners.racers.CommandCanceller;
import com.superdevelopment.pigracing.listeners.racers.DropCanceller;
import com.superdevelopment.pigracing.listeners.racers.FallResistance;
import com.superdevelopment.pigracing.listeners.racers.PickupCanceller;
import com.superdevelopment.pigracing.race.PigRace;
import com.superdevelopment.pigracing.listeners.racers.PlayerDisconnectListener;
import com.superdevelopment.pigracing.race.customentity.CustomPig;
import net.minecraft.server.v1_8_R3.EntityPig;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private CheckPointPassEventListener checkPointPassEventListener;
    private CheckpointParticleHandler checkpointParticleHandler;

    public PigRace race;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new Disconnect(), this);
        getServer().getPluginManager().registerEvents(new CheckPointPassEventListener(), this);
        getServer().getPluginManager().registerEvents(new LapCompleteListener(), this);
        getServer().getPluginManager().registerEvents(new PigDamageCanceller(), this);
        getServer().getPluginManager().registerEvents(new PigDismountCanceller(), this);
        getServer().getPluginManager().registerEvents(new PlayerDisconnectListener(), this);
        getServer().getPluginManager().registerEvents(new DropCanceller(), this);
        getServer().getPluginManager().registerEvents(new PickupCanceller(), this);
        getServer().getPluginManager().registerEvents(new CommandCanceller(), this);
        getServer().getPluginManager().registerEvents(new FallResistance(), this);
        getServer().getPluginManager().registerEvents(new OnWorldLoad(), this);
        getServer().getPluginManager().registerEvents(new PigHitGroundListener(), this);

        getCommand("race").setExecutor(new MainCommand());

        CustomPig.registerEntity("Pig", 90, EntityPig.class, CustomPig.class);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
