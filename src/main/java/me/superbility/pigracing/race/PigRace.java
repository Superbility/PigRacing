package me.superbility.pigracing.race;

import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.github.fierioziy.particlenativeapi.api.Particles_1_8;
import com.github.fierioziy.particlenativeapi.core.ParticleNativeCore;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import me.superbility.pigracing.Main;
import me.superbility.pigracing.listeners.checkpoint.CheckPointPassEventListener;
import me.superbility.pigracing.race.customentity.CustomPig;
import com.superdevelopment.pigracing.utils.*;
import io.github.theluca98.textapi.Title;
import me.superbility.pigracing.utils.Actionbar;
import me.superbility.pigracing.utils.ArrayIndexFromValue;
import me.superbility.pigracing.utils.LocationSerialiser;
import me.superbility.pigracing.utils.RacerUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PigRace {
    private Main plugin = Main.getPlugin(Main.class);
    private CheckPointPassEventListener checkPointPassEventListener = new CheckPointPassEventListener();

    private ParticleNativeAPI api = ParticleNativeCore.loadAPI(plugin);
    private Particles_1_8 particles = api.getParticles_1_8();

    private List<Racer> racers;
    private List<LivingEntity> pigs;
    private List<Racer> completedRacers = new ArrayList<>();
    private int timeRemaining;
    private Location waitLocation;
    private Location startLocation;
    private RaceState raceState = RaceState.WAITING;
    private int maxCheckpoints = CheckPointPassEventListener.checkpointRegions.size();

    private List<String> joinMessage = new ArrayList<>();
    private List<String> joinedMessage = new ArrayList<>();
    private String inventoryNotEmptyMessage;
    private String leaveMessage;
    private String joinedBroadcast;
    private String alreadyInRace;
    private String notInRace;
    private String waitingActionbar;
    private String inProgressActionbar;
    private String completedActionbar;
    private String goneTooFar;


    public PigRace() {
        racers = new ArrayList<>();
        timeRemaining = 300;
        pigs = new ArrayList<>();

        for(String s : plugin.getConfig().getStringList("Messages.JoinRace")) {
            joinMessage.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        for(String s : plugin.getConfig().getStringList("Messages.Joined")) {
            joinedMessage.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        inventoryNotEmptyMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.InventoryNotEmpty"));
        leaveMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.Leave"));
        joinedBroadcast = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.JoinedBroadcast"));
        alreadyInRace = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.AlreadyInRace"));
        notInRace = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.NotInRace"));
        waitingActionbar = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Actionbar.Waiting"));
        inProgressActionbar = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Actionbar.InProgress"));
        completedActionbar = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Actionbar.RaceCompleted"));
        goneTooFar = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.GoneTooFar"));

        waitLocation = LocationSerialiser.getLiteLocationFromString(plugin.getConfig().getString("Data.WaitingLocation"));
        startLocation = LocationSerialiser.getLiteLocationFromString(plugin.getConfig().getString("Data.StartLocation"));

        plugin.race = this;
        startGathering();
        startActionbars();
        startLocationListener();
    }

    public RaceState getGameState() {
        return raceState;
    }
    public List<Racer> getRacers() {
        return racers;
    }
    public int getMaxChekpoints() {
        return maxCheckpoints;
    }

    private void startGathering() {
        new BukkitRunnable() {
            int counter = 10; //TODO CHANGE 60 / ADD CONFIG
            @Override
            public void run() {
                if(counter == 0) {
                    startRace();
                    cancel();
                }
                if(counter == 60 || counter == 30 || counter == 10 || counter == 5 || counter == 4 || counter == 3 || counter == 2 || counter == 1) {
                    for (String s : joinMessage) {
                        TextComponent component = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', s.replace("{time}", String.valueOf(counter)))));
                        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pigrace join"));
                        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.LIGHT_PURPLE + "/pigrace join")));
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.spigot().sendMessage(component);
                        }
                    }
                }
                counter--;
            }
        }.runTaskTimer(plugin, 20, 20);
    }
    private void startRace() {
        for(Racer r : racers) {
            Player player = r.getPlayer();
            player.teleport(startLocation);

            Pig pig = CustomPig.spawn(player.getLocation());
            pig.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 100));
            pig.setSaddle(true);
            pig.setMetadata("pigrace", new FixedMetadataValue(plugin, true));
            //pig.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1, false));

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 100));

            r.setPig(pig);
            pigs.add(pig);

            r.setInGame(true);
            raceState = RaceState.INPROGRESS;

            startParticles();
        }

        new BukkitRunnable() {
            int counter = 5;
            @Override
            public void run() {
                if(counter == 5 || counter == 4) {
                    for(Racer r : racers) {
                        Player player = r.getPlayer();
                        player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 20, 1);
                        Title title = new Title(ChatColor.GREEN + String.valueOf(counter), "", 5, 10, 5);
                        title.send(player);
                    }
                }
                if(counter == 3 || counter == 2) {
                    for(Racer r : racers) {
                        Player player = r.getPlayer();
                        player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 20, 1);
                        Title title = new Title(ChatColor.GOLD + String.valueOf(counter), "", 5, 10, 5);
                        title.send(player);
                    }
                }
                if(counter == 1) {
                    for(Racer r : racers) {
                        Player player = r.getPlayer();
                        player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 20, 1);
                        Title title = new Title(ChatColor.RED + "1", "", 5, 10, 5);
                        title.send(player);
                    }
                }
                if(counter == 0) {
                    Title title = new Title(ChatColor.RED + "GOOOOOOOOOOO", "", 5, 10, 5);
                    for(Racer r : racers) {
                        title.send(r.getPlayer());
                        r.getPig().setPassenger(r.getPlayer());
                    }
                    cancel();
                }
                counter--;
            }
        }.runTaskTimer(plugin, 20, 20);
    }
    private void startParticles() {
        HashMap<CuboidSelection, Integer> checkpointRegions = CheckPointPassEventListener.checkpointRegions;
        new BukkitRunnable() {
            float radius = 3f;
            float angle = 0f;
            @Override
            public void run() {
                for (CuboidSelection selection : checkpointRegions.keySet()) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getWorld() == selection.getWorld()) {
                            if (p.getLocation().distance(selection.getMinimumPoint()) <= 40) {
                                Location minPoint = selection.getMinimumPoint();
                                Location maxPoint = selection.getMaximumPoint();

                                Location location = new Location(selection.getWorld(),
                                        (maxPoint.getX() + minPoint.getX()) / 2,
                                        (maxPoint.getY() + minPoint.getY()) / 2,
                                        (maxPoint.getZ() + minPoint.getZ()) / 2);

                                double xz = (radius * Math.sin(angle));
                                double y = (radius * Math.cos(angle));
                                double xz90 = (radius * Math.sin(angle + 90));
                                double y90 = (radius * Math.cos(angle + 90));
                                double xz180 = (radius * Math.sin(angle + 180));
                                double y180 = (radius * Math.cos(angle + 180));
                                double xz270 = (radius * Math.sin(angle + 270));
                                double y270 = (radius * Math.cos(angle + 270));
                                angle += 0.03;

                                Location oppLocation = location.clone();
                                Location location90 = location.clone();
                                Location oppLocation90 = location.clone();
                                Location location180 = location.clone();
                                Location oppLocation180 = location.clone();
                                Location location270 = location.clone();
                                Location oppLocation270 = location.clone();

                                if(getDirection(selection) == 'x') {
                                    location.add(xz, y, 0);
                                    location90.add(xz90, y90, 0);
                                    location180.add(xz180, y180, 0);
                                    location270.add(xz270, y270, 0);
                                    oppLocation.subtract(xz, y, 0);
                                    oppLocation90.subtract(xz90, y90, 0);
                                    oppLocation180.subtract(xz180, y180, 0);
                                    oppLocation270.subtract(xz270, y, 0);
                                } else {
                                    location.add(0, y, xz);
                                    location90.add(0, y90, xz90);
                                    location180.add(0, y180, xz180);
                                    location270.add(0, y270, xz270);
                                    oppLocation.subtract(0, y, xz);
                                    oppLocation90.subtract(0, y90, xz90);
                                    oppLocation180.subtract(0, y180, xz180);
                                    oppLocation270.subtract(0, y270, xz270);
                                }

                                particles.sendPacket(p, particles.REDSTONE().packetColored(true, location, Color.FUCHSIA));
                                particles.sendPacket(p, particles.REDSTONE().packetColored(true, oppLocation, Color.WHITE));

                                particles.sendPacket(p, particles.REDSTONE().packetColored(true, location90, Color.FUCHSIA));
                                particles.sendPacket(p, particles.REDSTONE().packetColored(true, oppLocation90, Color.WHITE));
                                
                                particles.sendPacket(p, particles.REDSTONE().packetColored(true, location180, Color.FUCHSIA));
                                particles.sendPacket(p, particles.REDSTONE().packetColored(true, oppLocation180, Color.WHITE));

                                particles.sendPacket(p, particles.REDSTONE().packetColored(true, location270, Color.FUCHSIA));
                                particles.sendPacket(p, particles.REDSTONE().packetColored(true, oppLocation270, Color.WHITE));
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 1, 1);
    }
    private char getDirection(CuboidSelection selection) {
        if(selection.getWidth() > selection.getLength()) {
            return 'x';
        } else {
            return 'z';
        }
    }
    private void startActionbars() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Racer r : racers) {
                    if(raceState == RaceState.WAITING) {
                        Actionbar.sendActionText(r.getPlayer(), waitingActionbar.replace("{players}", String.valueOf(racers.size())));
                    }
                    if(raceState == RaceState.INPROGRESS) {
                        if(r.getLap() == 3) {
                            Actionbar.sendActionText(r.getPlayer(), completedActionbar.replace("{position}", String.valueOf(ArrayIndexFromValue.getIndex(completedRacers, r))));
                        } else {
                            Actionbar.sendActionText(r.getPlayer(), inProgressActionbar.replace("{laps}", String.valueOf(r.getLap())));
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }
    private void startLocationListener() {
        new BukkitRunnable() {

            @Override
            public void run() {
                for(Racer r : racers) {
                    Player player = r.getPlayer();
                    if(player.getLocation().distance(startLocation) >= 150) {
                        if (player.getVehicle() != null) {
                            Entity pig = player.getVehicle();
                            pig.eject();
                            pig.teleport(startLocation);
                            player.teleport(startLocation);
                            pig.setPassenger(player);
                            player.sendMessage(goneTooFar);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 10, 10);
    }
    private void startPowerupSpawns() {
        new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskTimer(plugin, 2000, 2000);

    }

    public void addPlayer(Player player) {
        if (RacerUtils.playerInRace(player, racers)) {
            player.sendMessage(alreadyInRace);
            return;
        }
        racers.add(new Racer(player));
        player.teleport(waitLocation);
        for (String s : joinedMessage) {
            player.sendMessage(s);
        }
        Bukkit.broadcastMessage(joinedBroadcast.replace("{player}", player.getName()).replace("{amount}", String.valueOf(racers.size())));
    }
    public void removePlayer(Player player, boolean messages) {
        if(!RacerUtils.playerInRace(player, racers)) {
            if(messages) player.sendMessage(notInRace);
            return;
        }
        for(Racer r : racers) {
            if(r.getPlayer() == player) {
                racers.remove(r);
                if(messages) player.sendMessage(leaveMessage);
                return;
            }
        }
    }

    public void setRacerCompleted(Racer r) {
        completedRacers.add(r);
    }
    public int getRacerPosition(Racer r) {
        return ArrayIndexFromValue.getIndex(completedRacers, r);
    }
}
