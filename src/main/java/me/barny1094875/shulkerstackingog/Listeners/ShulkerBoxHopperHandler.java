package me.barny1094875.shulkerstackingog.Listeners;

import me.barny1094875.shulkerstackingog.ShulkerStacking_OG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.Arrays;

public class ShulkerBoxHopperHandler implements Listener
{
    @EventHandler
    public void shulkerBoxHopperHandler(InventoryMoveItemEvent event)
    {
        ItemStack eventItem = event.getItem();
        Inventory eventDestination = event.getDestination();
        Inventory eventSource = event.getSource();
        // if the item is a shulker box
        if(eventItem.getType().toString().contains("SHULKER_BOX"))
        {
            // check that the shulker box is empty
            BlockStateMeta shulkerMeta = (BlockStateMeta) eventItem.getItemMeta();
            ShulkerBox shulker = (ShulkerBox) shulkerMeta.getBlockState();
            if(!shulker.getInventory().isEmpty())
            {
                return;
            }
            // cancel the event so that I can guarantee where the item will be
            event.setCancelled(true);
            // item is always in eventSource now

            Material shulkerType = eventItem.getType();
            // loop through the destination to see if there is a shulker box stack to add to
            for(int i = 0; i < eventDestination.getSize(); i++)
            {
                ItemStack destinationItem = eventDestination.getItem(i);
                if(destinationItem == null)
                {
                    continue;
                }

                // if the item is the right kind of shulker box
                if(destinationItem.getType().equals(shulkerType)){
                    // check that the shulker boxes are emtpy
                    BlockStateMeta destinationShulkerMeta = (BlockStateMeta) destinationItem.getItemMeta();
                    ShulkerBox destinationShulker = (ShulkerBox) destinationShulkerMeta.getBlockState();
                    if(!destinationShulker.getInventory().isEmpty())
                    {
                        continue;
                    }
                    // check that the item stack is not full
                    if(destinationItem.getAmount() > 63)
                    {
                        continue;
                    }

                    destinationItem.setAmount(destinationItem.getAmount() + 1);
                    Bukkit.getScheduler().runTaskLater(ShulkerStacking_OG.getPlugin(), () -> {
                        eventItem.setAmount(eventItem.getAmount() - 1);
                    }, 1);
                    return;
                }
            }

            // if no shulker box was found, try to put it in the first empty slot
            if(eventDestination.addItem(eventItem).isEmpty()){
                Bukkit.getScheduler().runTaskLater(ShulkerStacking_OG.getPlugin(), () -> {
                    eventItem.setAmount(eventItem.getAmount() - 1);
                }, 1);
            }
        }
    }
}
