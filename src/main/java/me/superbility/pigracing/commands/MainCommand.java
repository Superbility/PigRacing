package com.superdevelopment.pigracing.commands;

import com.sk89q.worldedit.bukkit.*;
import com.sk89q.worldedit.bukkit.selections.*;
import com.superdevelopment.pigracing.Main;
import com.superdevelopment.pigracing.listeners.checkpoint.CheckPointPassEventListener;
import com.superdevelopment.pigracing.race.PigRace;
import com.superdevelopment.pigracing.race.customentity.CustomPig;
import com.superdevelopment.pigracing.utils.LocationSerialiser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainCommand implements CommandExecutor {
    private Main plugin = Main.getPlugin(Main.class);
    private CheckPointPassEventListener checkPointPassEventListener = new CheckPointPassEventListener();

    private List<String> helpMessage = new ArrayList<String>(Arrays.asList(
            ChatColor.translateAlternateColorCodes('&', "&5&l-----------[&d&lPig-Racing&6&l]-----------"),
            ChatColor.translateAlternateColorCodes('&', "&7Made by &9Superbility"),
            "",
            ChatColor.translateAlternateColorCodes('&', "&5/pigrace help &d- Displays this message"),
            ChatColor.translateAlternateColorCodes('&', "&6/pigrace start &d- Starts a race"),
            ChatColor.translateAlternateColorCodes('&', "&6/vkits givekit [player] [type] &e- Gives a player ownership of a vkit"),
            ChatColor.translateAlternateColorCodes('&', "&6/vkits givekitperm [player] [type] &e- Gives a player ownership of a vkit permanently"),
            ChatColor.translateAlternateColorCodes('&', "&6/vkits reset [player] [type] &e- Resets a player's cooldown on a vkit"),
            ChatColor.translateAlternateColorCodes('&', "&6&l---------------------------------")
    ));
    private String noRaceMessage = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.NoCurrentRace"));

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("race")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("help")) {
                    for (String s : helpMessage) {
                        sender.sendMessage(s);
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("start")) {
                    if (sender.hasPermission("pigrace.start")) {
                        new PigRace();
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("join")) {
                    if (sender instanceof Player) {
                        PigRace race = plugin.race;
                        if (race != null) {
                            race.addPlayer((Player) sender);
                        } else {
                            sender.sendMessage(noRaceMessage);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You must be a player to use that command!");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("setWaitingLoc")) {
                    if (sender instanceof Player && ((Player) sender).getUniqueId().toString().equals("8ef66f53-70c5-44b7-8b56-161de639347b")) {
                        plugin.getConfig().set("Data.WaitingLocation", LocationSerialiser.getLiteStringFromLocation(((Player) sender).getLocation()));
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.GREEN + "Location set successfully!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "You cannot do this!");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("leave")) {
                    if (sender instanceof Player) {
                        PigRace race = plugin.race;
                        if (race != null) {
                            race.removePlayer((Player) sender, true);
                            if (((Player) sender).getVehicle() != null) {
                                if (((Player) sender).getVehicle().hasMetadata("pigrace")) {
                                    ((Player) sender).getVehicle().remove();
                                }
                            }
                        } else {
                            sender.sendMessage(noRaceMessage);
                        }
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("setfinishline")) {
                    if (sender instanceof Player && ((Player) sender).getUniqueId().toString().equals("8ef66f53-70c5-44b7-8b56-161de639347b")) {
                        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
                        Selection selection = worldEdit.getSelection((Player) sender);

                        plugin.getConfig().set("Data.FinishLine.Loc1", LocationSerialiser.getLiteStringFromLocation(selection.getMinimumPoint()));
                        plugin.getConfig().set("Data.FinishLine.Loc2", LocationSerialiser.getLiteStringFromLocation(selection.getMaximumPoint()));
                        plugin.saveConfig();

                        sender.sendMessage(ChatColor.GREEN + "Location set successfully!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "You cannot do this!");
                    }
                    return true;

                }
                if (args[0].equalsIgnoreCase("addcheckpoint")) {
                    if (sender instanceof Player && ((Player) sender).getUniqueId().toString().equals("8ef66f53-70c5-44b7-8b56-161de639347b")) {
                        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
                        Selection selection = worldEdit.getSelection((Player) sender);

                        int checkpoint;
                        if (plugin.getConfig().getConfigurationSection("Data.Checkpoints") != null) {
                            checkpoint = plugin.getConfig().getConfigurationSection("Data.Checkpoints").getKeys(false).size() + 1;
                        } else {
                            checkpoint = 1;
                        }

                        plugin.getConfig().set("Data.Checkpoints." + checkpoint + ".Loc1", LocationSerialiser.getLiteStringFromLocation(selection.getMinimumPoint()));
                        plugin.getConfig().set("Data.Checkpoints." + checkpoint + ".Loc2", LocationSerialiser.getLiteStringFromLocation(selection.getMaximumPoint()));
                        plugin.saveConfig();
                        checkPointPassEventListener.resetRegions();

                        sender.sendMessage(ChatColor.GREEN + "Checkpoint " + checkpoint + " added!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "You cannot do this!");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("resetcheckpoints")) {
                    if (sender instanceof Player && ((Player) sender).getUniqueId().toString().equals("8ef66f53-70c5-44b7-8b56-161de639347b")) {
                        plugin.getConfig().set("Data.Checkpoints", null);
                        plugin.saveConfig();
                        checkPointPassEventListener.resetRegions();

                        sender.sendMessage(ChatColor.GREEN + "Checkpoints removed!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "You cannot do this!");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("reloadcheckpoints")) {
                    if (sender instanceof Player && ((Player) sender).getUniqueId().toString().equals("8ef66f53-70c5-44b7-8b56-161de639347b")) {
                        checkPointPassEventListener.resetRegions();

                        sender.sendMessage(ChatColor.GREEN + "Checkpoints reloaded!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "You cannot do this!");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("setstartloc")) {
                    if (sender instanceof Player && ((Player) sender).getUniqueId().toString().equals("8ef66f53-70c5-44b7-8b56-161de639347b")) {
                        plugin.getConfig().set("Data.StartLocation", LocationSerialiser.getLiteStringFromLocation(((Player) sender).getLocation()));
                        plugin.saveConfig();

                        sender.sendMessage(ChatColor.GREEN + "Location set successfully!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "You cannot do this!");
                    }
                    return true;

                }
                if (args[0].equalsIgnoreCase("addpoweruploc")) {
                    if (sender instanceof Player && ((Player) sender).getUniqueId().toString().equals("8ef66f53-70c5-44b7-8b56-161de639347b")) {
                        List<String> powerupLocs = plugin.getConfig().getStringList("Data.Powerups");
                        powerupLocs.add(LocationSerialiser.getLiteStringFromLocation(((Player) sender).getLocation()));
                        plugin.getConfig().set("Data.Powerups", powerupLocs);
                        plugin.saveConfig();

                        sender.sendMessage(ChatColor.GREEN + "Location set successfully!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "You cannot do this!");
                    }
                    return true;

                }
                if (args[0].equalsIgnoreCase("spawnpig")) {
                    if (sender instanceof Player && ((Player) sender).getUniqueId().toString().equals("8ef66f53-70c5-44b7-8b56-161de639347b")) {
                        Pig pig = CustomPig.spawn(((Player) sender).getLocation());
                        pig.setSaddle(true);
                        pig.setMetadata("pigrace", new FixedMetadataValue(plugin, true));
                    }
                }
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dPig-Racing &7made by &9Superbility#6039&7. Use &6/pigrace help &7for help!"));
                return true;
            }
        }
        return false;
    }
}
