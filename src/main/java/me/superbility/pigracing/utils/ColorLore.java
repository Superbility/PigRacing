package com.superdevelopment.pigracing.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ColorLore {
    public static List<String> getLore(List<String> list) {
        List<String> newList = new ArrayList<>();

        for(String s : list) {
            newList.add(ChatColor.translateAlternateColorCodes('&', s));

        }

        return newList;
    }
}
