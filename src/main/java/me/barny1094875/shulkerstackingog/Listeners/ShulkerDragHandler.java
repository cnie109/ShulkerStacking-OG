package me.barny1094875.shulkerstackingog.Listeners;

import me.barny1094875.shulkerstackingog.ShulkerStacking_OG;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ShulkerDragHandler implements Listener
{

    @EventHandler(priority = EventPriority.MONITOR)
    public void shulkerBoxDrag(InventoryDragEvent event)
    {
        if (!event.getOldCursor().getType().toString().contains("SHULKER_BOX"))
        {
            return;
        }

        // disable default behavior
        event.setCancelled(true);

        Bukkit.getScheduler().runTask(ShulkerStacking_OG.getPlugin(), () ->
        {
            // check if the inventory was closed in the last tick
            // this prevents a dupe glitch from occurring
            if (ShulkerStacking_OG.isInventoryClosed)
            {
                return;
            }
            ItemStack shulkerBoxes = event.getOldCursor();
            int numberOfSlotsDragged = event.getInventorySlots().size();
            ArrayList<Integer> slotsDragged = new ArrayList<>(event.getInventorySlots().stream().toList());
            Inventory dragInventory = event.getView().getBottomInventory();
            // right click drag
            if (event.getType().equals(DragType.SINGLE))
            {
                // if the player drags a stack of shulker boxes
                if (shulkerBoxes.getAmount() <= 1)
                {
                    return;
                }
                // set each slot to the appropriate amount of shulkers
                for (int i = 0; i < numberOfSlotsDragged; i++)
                {
                    if (dragInventory.getItem(slotsDragged.get(i)) == null ||
                            dragInventory.getItem(slotsDragged.get(i)).getType() == Material.AIR)
                    {
                        dragInventory.setItem(slotsDragged.get(i), new ItemStack(shulkerBoxes.getType(), 1));
                    }
                    else if (dragInventory.getItem(slotsDragged.get(i)) != null && dragInventory.getItem(slotsDragged.get(i)).getType().equals(shulkerBoxes.getType()))
                    {
                        dragInventory.getItem(slotsDragged.get(i)).setAmount(dragInventory.getItem(slotsDragged.get(i)).getAmount() + 1);
                    }
                }
                // remove the shulker boxes from the player's cursor
                event.getWhoClicked().setItemOnCursor(new ItemStack(shulkerBoxes.getType(), shulkerBoxes.getAmount() - numberOfSlotsDragged));
            }
            // left click drag
            else if (event.getType().equals(DragType.EVEN))
            {
                // if the player drags a stack of shulker boxes
                if (shulkerBoxes.getAmount() <= 1)
                {
                    return;
                }
                // set each slot to the appropriate amount of shulkers
                for (int i = 0; i < numberOfSlotsDragged; i++)
                {
                    if (dragInventory.getItem(slotsDragged.get(i)) == null || dragInventory.getItem(slotsDragged.get(i)).getType() == Material.AIR)
                    {
                        ItemStack newShulkerBoxes = new ItemStack(shulkerBoxes.getType(), shulkerBoxes.getAmount() / numberOfSlotsDragged);
                        dragInventory.setItem(slotsDragged.get(i), newShulkerBoxes);
                        if (newShulkerBoxes.getAmount() > 64)
                        {
                            shulkerBoxes.setAmount(shulkerBoxes.getAmount() - (64 - shulkerBoxes.getAmount()));
                            newShulkerBoxes.setAmount(64);
                            slotsDragged.remove(i);
                            break;
                        }
                        dragInventory.setItem(slotsDragged.get(i), newShulkerBoxes);
                    }
                    else if (dragInventory.getItem(slotsDragged.get(i)) != null && dragInventory.getItem(slotsDragged.get(i)).getType().equals(shulkerBoxes.getType()))
                    {
                        ItemStack newShulkerBoxes = new ItemStack(shulkerBoxes.getType(), shulkerBoxes.getAmount() / numberOfSlotsDragged);
                        dragInventory.getItem(slotsDragged.get(i)).setAmount(dragInventory.getItem(slotsDragged.get(i)).getAmount() + shulkerBoxes.getAmount() / numberOfSlotsDragged);
                        if (newShulkerBoxes.getAmount() > 64)
                        {
                            shulkerBoxes.setAmount(shulkerBoxes.getAmount() - (64 - shulkerBoxes.getAmount()));
                            newShulkerBoxes.setAmount(64);
                            slotsDragged.remove(i);
                            break;
                        }
                        dragInventory.setItem(slotsDragged.get(i), newShulkerBoxes);
                    }
                }
                event.getWhoClicked().setItemOnCursor(new ItemStack(shulkerBoxes.getType(), shulkerBoxes.getAmount() % numberOfSlotsDragged));
                ((Player) event.getWhoClicked()).updateInventory();
            }
        });
    }

}
