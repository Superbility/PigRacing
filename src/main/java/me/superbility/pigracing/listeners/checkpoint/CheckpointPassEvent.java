package me.superbility.pigracing.listeners.checkpoint;

import me.superbility.pigracing.race.PigRace;
import me.superbility.pigracing.race.Racer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CheckpointPassEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Racer racer;
    private final PigRace race;
    private boolean isCancelled;

    public CheckpointPassEvent(Racer racer, PigRace race) {
        this.racer = racer;
        this.race = race;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    public Racer getRacer() {return racer;}
    public PigRace getRace() {return race;}
}
