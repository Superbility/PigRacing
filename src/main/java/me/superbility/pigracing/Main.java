package me.superbility.pigracing;

import me.superbility.pigracing.commands.MainCommand;
import me.superbility.pigracing.listeners.Disconnect;
import me.superbility.pigracing.listeners.LapCompleteListener;
import me.superbility.pigracing.listeners.OnWorldLoad;
import me.superbility.pigracing.listeners.checkpoint.CheckPointPassEventListener;
import me.superbility.pigracing.listeners.checkpoint.CheckpointParticleHandler;
import me.superbility.pigracing.listeners.pig.PigDamageCanceller;
import me.superbility.pigracing.listeners.pig.PigDismountCanceller;
import me.superbility.pigracing.listeners.pig.PigHitGroundListener;
import me.superbility.pigracing.listeners.racers.CommandCanceller;
import me.superbility.pigracing.listeners.racers.DropCanceller;
import me.superbility.pigracing.listeners.racers.FallResistance;
import me.superbility.pigracing.listeners.racers.PickupCanceller;
import me.superbility.pigracing.race.PigRace;
import me.superbility.pigracing.listeners.racers.PlayerDisconnectListener;
import me.superbility.pigracing.race.customentity.CustomPig;
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
