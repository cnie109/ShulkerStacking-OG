package me.barny1094875.shulkerstackingog;

import me.barny1094875.shulkerstackingog.Listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class ShulkerStacking_OG extends JavaPlugin
{

    private static ShulkerStacking_OG plugin;

    // used to prevent dupe glitches from inventory drag events
//    public static boolean isInventoryClosed = false;
    public static HashMap<UUID, Boolean> isInventoryClosed = new HashMap<>();

    @Override
    public void onEnable()
    {
        plugin = this;
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new ShulkerBoxStackingController(), this);
        getServer().getPluginManager().registerEvents(new ShulkerBoxPickupHandler(), this);
        getServer().getPluginManager().registerEvents(new ShulkerDragHandler(), this);
        getServer().getPluginManager().registerEvents(new ShulkerDragDupePrevention(), this);
        getServer().getPluginManager().registerEvents(new ShulkerBoxHopperHandler(), this);
    }

    public static ShulkerStacking_OG getPlugin()
    {
        return plugin;
    }
}
