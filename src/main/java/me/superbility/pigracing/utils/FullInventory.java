package com.superdevelopment.pigracing.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FullInventory {
    public static boolean hasFullInventory(Player player) {
        for(ItemStack it : player.getInventory().getContents())
        {
            if(it != null) return false;
        }
        return true;
    }
}
