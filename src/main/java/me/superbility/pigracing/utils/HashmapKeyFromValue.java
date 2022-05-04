package com.superdevelopment.pigracing.utils;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import java.util.HashMap;

public class HashmapKeyFromValue {
    public static CuboidSelection getSelection(HashMap<CuboidSelection, Integer> map, Integer key) {
        for(CuboidSelection selection : map.keySet()) {
            if(map.get(selection) == key) {
                return selection;
            }
        }
        return (CuboidSelection) map.keySet().toArray()[1];
    }
}
