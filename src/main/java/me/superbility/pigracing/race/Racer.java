package me.superbility.pigracing.race;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import me.superbility.pigracing.listeners.checkpoint.CheckpointParticleHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Racer {
    private CheckpointParticleHandler checkpointParticleHandler = new CheckpointParticleHandler();

    private int lap;
    private double time;
    private LivingEntity pig;
    private Player player;
    private boolean inGame;
    private int checkpoint = 0;

    public Racer(Player player) {
        lap = 0;
        time = 0;
        pig = null;
        this.player = player;
        this.inGame = false;
    }

    public int getLap() {
        return lap;
    }
    public void addLap() {
        this.lap++;
    }

    public double getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }

    public LivingEntity getPig() {
        return pig;
    }
    public void setPig(LivingEntity pig) {
        this.pig = pig;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean inGame() {
        return inGame;
    }
    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public int getCheckpoint() {
        return checkpoint;
    }
    public void setCheckpoint(int checkpoint) {
        this.checkpoint = checkpoint;
    }
    public void nextCheckpoint(HashMap<CuboidSelection, Integer> selections) {
        this.checkpoint++;
    }
}
