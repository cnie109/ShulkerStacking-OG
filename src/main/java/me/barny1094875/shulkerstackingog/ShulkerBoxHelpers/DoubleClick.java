package me.barny1094875.shulkerstackingog.ShulkerBoxHelpers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class DoubleClick
{

    public static void DoubleClickShulkerBox(ItemStack cursorItem, InventoryClickEvent event)
    {
        // check that the held shulker box is empty
        Inventory heldShulkerInventory = ((ShulkerBox) ((BlockStateMeta) cursorItem.getItemMeta()).getBlockState()).getInventory();
        if (heldShulkerInventory.isEmpty())
        {
            // loop through the inventory, and the player's inventory
            // and count up the shulker boxes
            // ignore boxes in result slots
            // delete the shulker boxes, up to 64, and set the cursor stack
            // to the number of boxes, up to 64
            InventoryView eventView = event.getView();
            Inventory playerInventory = eventView.getBottomInventory();
            Inventory containerInventory = eventView.getTopInventory();
            Material shulkerType = cursorItem.getType();
            int containerSize = containerInventory.getSize();

            // store a temporary inventory so that we can handle
            // full shulker boxes
            Inventory tempPlayerInventory = Bukkit.createInventory(null, 36);
            tempPlayerInventory.setContents(playerInventory.getContents());
            // make the inventory size the max of 54 so that it will hold any size inventory
            Inventory tempContainerInventory = Bukkit.createInventory(null, 54);
            tempContainerInventory.setContents(containerInventory.getContents());

            // use these two inventories to loop through the inventories
            // we can delete full shulkers from these temporary inventories
            // without deleting them from the main inventory
            // and then just copy over the empty shulker deletes to the main
            // inventories

            // loop through the container inventory until there are no
            // more shulker boxes, counting them up, and removing them
            // up to 64 boxes
            int shulkerBoxCount = cursorItem.getAmount();
            while (tempContainerInventory.first(shulkerType) != -1)
            {
                int firstShulker = tempContainerInventory.first(shulkerType);
                // grab the inventory of the shulker box in that slot
                Inventory shulkerInventory = ((ShulkerBox) ((BlockStateMeta) tempContainerInventory.getItem(firstShulker).getItemMeta()).getBlockState()).getInventory();
                // if that inventory is not empty, delete it from the temporary inventory, and continue on
                // otherwise, do the rest of the logic
                if (!shulkerInventory.isEmpty())
                {
                    tempContainerInventory.setItem(firstShulker, new ItemStack(Material.AIR));
                    continue;
                }

                // if the shulker box is in a RESULT slot, then break from
                // the loop, since that is always the last slot
                InventoryType.SlotType slotType = eventView.getSlotType(firstShulker);
                if (slotType.equals(InventoryType.SlotType.RESULT))
                {
                    break;
                }

                // if the shulker box count would have gone above 64
                ItemStack shulkerStackItem = containerInventory.getItem(firstShulker);
                int shulkerStackAmount = shulkerStackItem.getAmount();
                if (shulkerBoxCount + shulkerStackAmount > 64)
                {
                    // get how many shulker boxes we need to get to 64
                    int shulkersRequired = 64 - shulkerBoxCount;
                    // remove that many boxes from the stack
                    shulkerStackItem.setAmount(shulkerStackAmount - shulkersRequired);
                    shulkerBoxCount += shulkersRequired;
                    // break from the loop, since we have a stack of shulkers collected
                    break;
                }
                else
                {
                    // add the shulkerStackAmount to shulkerBoxCount
                    shulkerBoxCount += shulkerStackAmount;
                    // remove the shulkers from that slot
                    containerInventory.setItem(firstShulker, new ItemStack(Material.AIR));
                    tempContainerInventory.setItem(firstShulker, new ItemStack(Material.AIR));
                }
            }

            // loop through the player's inventory
            while (tempPlayerInventory.first(shulkerType) != -1)
            {
                int firstShulker = tempPlayerInventory.first(shulkerType);

                // grab the inventory of the shulker box in that slot
                Inventory shulkerInventory = ((ShulkerBox) ((BlockStateMeta) tempPlayerInventory.getItem(firstShulker).getItemMeta()).getBlockState()).getInventory();
                // if that inventory is not empty, delete it from the temporary inventory, and continue on
                // otherwise, do the rest of the logic
                if (!shulkerInventory.isEmpty())
                {
                    // remove this non-empty shulker from the temporary inventory
                    tempPlayerInventory.setItem(firstShulker, new ItemStack(Material.AIR));
                    continue;
                }

                // if the shulker box is in a RESULT slot, then break from
                // the loop, since that is always the last slot
                // add the container's size to the here to offset it to the player's inventory
                InventoryType.SlotType slotType = eventView.getSlotType(firstShulker + containerSize);
                if (slotType.equals(InventoryType.SlotType.RESULT))
                {
                    break;
                }
                // if the shulker box count would have gone above 64
                ItemStack shulkerStackItem = playerInventory.getItem(firstShulker);
                int shulkerStackAmount = shulkerStackItem.getAmount();
                if (shulkerBoxCount + shulkerStackAmount > 64)
                {
                    // get how many shulker boxes we need to get to 64
                    int shulkersRequired = 64 - shulkerBoxCount;
                    // remove that many boxes from the stack
                    shulkerStackItem.setAmount(shulkerStackAmount - shulkersRequired);
                    shulkerBoxCount += shulkersRequired;
                    // break from the loop, since we have a stack of shulkers collected
                    break;
                }
                else
                {
                    // add the shulkerStackAmount to shulkerBoxCount
                    shulkerBoxCount += shulkerStackAmount;
                    // remove the shulkers from that slot
                    playerInventory.setItem(firstShulker, new ItemStack(Material.AIR));
                    tempPlayerInventory.setItem(firstShulker, new ItemStack(Material.AIR));
                }
            }

            // now that we have the number of shulkers to grab, put them into the cursor
            // if the shulkerBoxCount was 1, then don't do anything
            if (shulkerBoxCount != 1)
            {
                ItemStack newShulkers = new ItemStack(shulkerType);
                newShulkers.setAmount(shulkerBoxCount);
                event.getWhoClicked().setItemOnCursor(newShulkers);
                // cancel default behavior
                event.setCancelled(true);
            }
        }
    }

}
