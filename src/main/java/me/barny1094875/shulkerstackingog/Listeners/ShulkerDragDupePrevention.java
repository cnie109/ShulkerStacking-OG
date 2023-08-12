package me.barny1094875.shulkerstackingog.Listeners;

import me.barny1094875.shulkerstackingog.ShulkerStacking_OG;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ShulkerDragDupePrevention implements Listener
{

    @EventHandler(priority = EventPriority.LOWEST)
    public void shulkerDragDupePrevent(InventoryCloseEvent event)
    {
        ShulkerStacking_OG.isInventoryClosed = true;
        Bukkit.getScheduler().runTaskLater(ShulkerStacking_OG.getPlugin(),
                () -> ShulkerStacking_OG.isInventoryClosed = false, 2);
    }
}
