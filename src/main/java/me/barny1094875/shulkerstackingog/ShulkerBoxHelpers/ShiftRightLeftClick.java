package me.barny1094875.shulkerstackingog.ShulkerBoxHelpers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class ShiftRightLeftClick
{

    public static void ShiftRightLeftClickShulkerBox(InventoryClickEvent event)
    {
        // check what kind of inventory was shift-clicked in
        // regardless, loop through the inventory(ies) and add the
        // shulker box stack to the
        InventoryView eventInventory = event.getView();
        InventoryType inventoryType = eventInventory.getType();
        Inventory containerInventory = eventInventory.getTopInventory();
        Inventory playerInventory = eventInventory.getBottomInventory();
        // store a temporary inventory so that we can handle
        // full shulker boxes
        Inventory tempPlayerInventory = Bukkit.createInventory(null, 36);
        tempPlayerInventory.setContents(playerInventory.getContents());
        // make the inventory size the max of 54 so that it will hold any size inventory
        Inventory tempContainerInventory = Bukkit.createInventory(null, 54);
        tempContainerInventory.setContents(containerInventory.getContents());
        Material shulkerBoxType = event.getCurrentItem().getType();
        int shulkerStackAmount = event.getCurrentItem().getAmount();


        // if it was the player's inventory
        if (inventoryType.equals(InventoryType.CRAFTING))
        {
            // check if the slot was a hotbar slot or not
            if (event.getSlotType().equals(InventoryType.SlotType.QUICKBAR))
            {
                // remove the hotbar of the tempInventory
                for (int i = 0; i < 9; i++)
                {
                    tempPlayerInventory.setItem(i, new ItemStack(Material.AIR));
                }

                // check the top part of the inventory for any shulker stacks that are not 64 yet
                while (tempPlayerInventory.first(shulkerBoxType) != -1)
                {

                    int firstShulkerSlot = tempPlayerInventory.first(shulkerBoxType);
                    ItemStack shulkerStack = tempPlayerInventory.getItem(firstShulkerSlot);
                    // if the shulker stack has 64 shulker boxes in it
                    // remove it from the temporary inventory and continue on
                    if (shulkerStack.getAmount() >= 64)
                    {
                        tempPlayerInventory.setItem(firstShulkerSlot, new ItemStack(Material.AIR));
                        continue;
                    }

                    // cancel default behavior since shulker boxes are being moved
                    event.setCancelled(true);

                    // stack up to 64 shulkers into the stack, and then continue on
                    if (shulkerStack.getAmount() + shulkerStackAmount <= 64)
                    {
                        playerInventory.getItem(firstShulkerSlot).setAmount(shulkerStack.getAmount() + shulkerStackAmount);
                        shulkerStackAmount = 0;
                        break;
                    }
                    else
                    {
                        // remove shulker boxes until the shulker stack has 64 boxes
                        shulkerStackAmount -= 64 - shulkerStack.getAmount();
                        playerInventory.getItem(firstShulkerSlot).setAmount(64);
                        tempPlayerInventory.setItem(firstShulkerSlot, new ItemStack(Material.AIR));
                    }
                }

                // change the shulker stack amount to what is left
                // or remove it if nothing is left
                if (shulkerStackAmount == 0)
                {
                    event.setCurrentItem(new ItemStack(Material.AIR));
                }
                else
                {
                    event.getCurrentItem().setAmount(shulkerStackAmount);
                    // look for an air gap to put the rest of the shulker boxes
                    for (int i = 9; i < playerInventory.getSize(); i++)
                    {
                        if (playerInventory.getItem(i) == null)
                        {
                            playerInventory.setItem(i, event.getCurrentItem());
                            event.setCurrentItem(new ItemStack(Material.AIR));
                        }
                    }
                }
            }
            // the slot was an inventory slot, so check the hotbar for boxes
            else
            {
                // remove everything from the upper inventory to prevent it from causing problems
                for (int i = 35; i > 8; i--)
                {
                    tempPlayerInventory.setItem(i, new ItemStack(Material.AIR));
                }

                while (tempPlayerInventory.first(shulkerBoxType) != -1)
                {
                    int firstShulkerSlot = tempPlayerInventory.first(shulkerBoxType);
                    ItemStack shulkerStack = tempPlayerInventory.getItem(firstShulkerSlot);
                    // if the shulker stack has 64 shulker boxes in it
                    // remove it from the temporary inventory and continue on
                    if (shulkerStack.getAmount() >= 64)
                    {
                        tempPlayerInventory.setItem(firstShulkerSlot, new ItemStack(Material.AIR));
                        continue;
                    }

                    // cancel default behavior since shulker boxes are being moved
                    event.setCancelled(true);

                    // stack up to 64 shulkers into the stack, and then continue on
                    if (shulkerStack.getAmount() + shulkerStackAmount <= 64)
                    {
                        playerInventory.getItem(firstShulkerSlot).setAmount(shulkerStack.getAmount() + shulkerStackAmount);
                        shulkerStackAmount = 0;
                        break;
                    }
                    else
                    {
                        // remove shulker boxes until the shulker stack has 64 boxes
                        shulkerStackAmount -= 64 - shulkerStack.getAmount();
                        playerInventory.getItem(firstShulkerSlot).setAmount(64);
                        tempPlayerInventory.setItem(firstShulkerSlot, new ItemStack(Material.AIR));
                    }
                }

                // change the shulker stack amount to what is left
                // or remove it if nothing is left
                if (shulkerStackAmount == 0)
                {
                    event.setCurrentItem(new ItemStack(Material.AIR));
                }
                else
                {
                    event.getCurrentItem().setAmount(shulkerStackAmount);
                    // look for an air gap to put the rest of the shulker boxes
                    for (int i = 0; i < 9; i++)
                    {
                        if (playerInventory.getItem(i) == null)
                        {
                            playerInventory.setItem(i, event.getCurrentItem());
                            event.setCurrentItem(new ItemStack(Material.AIR));
                        }
                    }
                }
            }
        }
        // if it was anything else
        else
        {
            // check if the click was in the bottom
            if (event.getRawSlot() > containerInventory.getSize())
            {
                while (tempContainerInventory.first(shulkerBoxType) != -1)
                {
                    int firstShulkerSlot = tempContainerInventory.first(shulkerBoxType);
                    ItemStack shulkerStack = tempContainerInventory.getItem(firstShulkerSlot);
                    // if the shulker stack has 64 shulker boxes in it
                    // remove it from the temporary inventory and continue on
                    if (shulkerStack.getAmount() >= 64)
                    {
                        tempContainerInventory.setItem(firstShulkerSlot, new ItemStack(Material.AIR));
                        continue;
                    }

                    // cancel default behavior since shulker boxes are being moved
                    event.setCancelled(true);

                    // stack up to 64 shulkers into the stack, and then continue on
                    if (shulkerStack.getAmount() + shulkerStackAmount <= 64)
                    {
                        containerInventory.getItem(firstShulkerSlot).setAmount(shulkerStack.getAmount() + shulkerStackAmount);
                        shulkerStackAmount = 0;
                        break;
                    }
                    else
                    {
                        // remove shulker boxes until the shulker stack has 64 boxes
                        shulkerStackAmount -= 64 - shulkerStack.getAmount();
                        containerInventory.getItem(firstShulkerSlot).setAmount(64);
                        tempContainerInventory.setItem(firstShulkerSlot, new ItemStack(Material.AIR));
                    }
                }

                // change the shulker stack amount to what is left
                // or remove it if nothing is left
                if (shulkerStackAmount == 0)
                {
                    event.setCurrentItem(new ItemStack(Material.AIR));
                }
                else
                {
                    event.getCurrentItem().setAmount(shulkerStackAmount);
                    // look for an air gap to put the rest of the shulker boxes
                    for (int i = 0; i < containerInventory.getSize(); i++)
                    {
                        if (containerInventory.getItem(i) == null)
                        {
                            containerInventory.setItem(i, event.getCurrentItem());
                            event.setCurrentItem(new ItemStack(Material.AIR));
                        }
                    }
                }
            }
            // the click was in the top inventory
            else
            {
                while (tempPlayerInventory.first(shulkerBoxType) != -1)
                {
                    int firstShulkerSlot = tempPlayerInventory.first(shulkerBoxType);
                    ItemStack shulkerStack = tempPlayerInventory.getItem(firstShulkerSlot);
                    // if the shulker stack has 64 shulker boxes in it
                    // remove it from the temporary inventory and continue on
                    if (shulkerStack.getAmount() >= 64)
                    {
                        tempPlayerInventory.setItem(firstShulkerSlot, new ItemStack(Material.AIR));
                        continue;
                    }

                    // cancel default behavior since shulker boxes are being moved
                    event.setCancelled(true);

                    // stack up to 64 shulkers into the stack, and then continue on
                    if (shulkerStack.getAmount() + shulkerStackAmount <= 64)
                    {
                        playerInventory.getItem(firstShulkerSlot).setAmount(shulkerStack.getAmount() + shulkerStackAmount);
                        shulkerStackAmount = 0;
                        break;
                    }
                    else
                    {
                        // remove shulker boxes until the shulker stack has 64 boxes
                        shulkerStackAmount -= 64 - shulkerStack.getAmount();
                        playerInventory.getItem(firstShulkerSlot).setAmount(64);
                        tempPlayerInventory.setItem(firstShulkerSlot, new ItemStack(Material.AIR));
                    }
                }

                // change the shulker stack amount to what is left
                // or remove it if nothing is left
                if (shulkerStackAmount == 0)
                {
                    event.setCurrentItem(new ItemStack(Material.AIR));
                }
                else
                {
                    event.getCurrentItem().setAmount(shulkerStackAmount);
                    // look for an air gap to put the rest of the shulker boxes
                    for (int i = 0; i < playerInventory.getSize(); i++)
                    {
                        if (playerInventory.getItem(i) == null)
                        {
                            playerInventory.setItem(i, event.getCurrentItem());
                            event.setCurrentItem(new ItemStack(Material.AIR));
                        }
                    }
                }
            }
        }
    }
}
