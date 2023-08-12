package me.barny1094875.shulkerstackingog.Listeners;

import me.barny1094875.shulkerstackingog.ShulkerBoxHelpers.DoubleClick;
import me.barny1094875.shulkerstackingog.ShulkerBoxHelpers.NormalLeftClick;
import me.barny1094875.shulkerstackingog.ShulkerBoxHelpers.NormalRightClick;
import me.barny1094875.shulkerstackingog.ShulkerBoxHelpers.ShiftRightLeftClick;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShulkerBoxStackingController implements Listener
{

    @EventHandler
    public void shulkerBoxStacking(InventoryClickEvent event)
    {

        if (event.getCurrentItem() == null)
        {
            return;
        }
        if (event.getCursor() == null)
        {
            return;
        }

        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursorItem = event.getCursor();
        Inventory inventory = event.getInventory();


        // if the player combines two shulker stacks
        if (event.getClick().equals(ClickType.LEFT))
        {
            if (currentItem.getType().toString().contains("SHULKER_BOX"))
            {
                if (cursorItem.getType().equals(currentItem.getType()))
                {
                    NormalLeftClick.NormalLeftClickShulkerBox(currentItem, cursorItem, event);
                    return;
                }
            }
        }

        // if the player sets down just one shulker box from a stack
        if (event.getClick().equals(ClickType.RIGHT))
        {
            if (currentItem.getType().toString().contains("SHULKER_BOX"))
            {
                if (cursorItem.getType().equals(currentItem.getType()))
                {
                    NormalRightClick.NormalRightClickShulkerBox(currentItem, cursorItem, event);
                }
            }
        }

        // if the player puts down a shulker box stack
        if (event.getClick().equals(ClickType.LEFT))
        {
            if (cursorItem.getType().toString().contains("SHULKER_BOX"))
            {
                // check if the shulker box stack only contains one shulker box
                if (cursorItem.getAmount() != 1)
                {
                    // remove the stack from their cursor
                    // and replace it with whatever was in the slot
                    event.getWhoClicked().setItemOnCursor(currentItem);
                    // place down the whole stack
                    event.setCurrentItem(cursorItem);
                    // stop default behaviors
                    event.setCancelled(true);
                    return;
                }
            }
        }

        // if the player double clicked in the inventory on a shulker box
        // gather all of the shulker boxes in the inventory to the player's cursor
        // including in their inventory

        if (event.getClick().equals(ClickType.DOUBLE_CLICK))
        {
            if (cursorItem.getType().toString().contains("SHULKER_BOX"))
            {
                DoubleClick.DoubleClickShulkerBox(cursorItem, event);
                return;
            }
        }


        // if the player shift clicks on a shukler box stack
        // we need to handle the stacking logic
        if (event.getClick().equals(ClickType.SHIFT_LEFT) ||
                event.getClick().equals(ClickType.SHIFT_RIGHT))
        {
            if (currentItem.getType().toString().contains("SHULKER_BOX"))
            {
                ShiftRightLeftClick.ShiftRightLeftClickShulkerBox(event);
            }
        }


        // if the player users CTRL+<drop> on the shulkers
        // drop the whole stack, instead of just one
        if (event.getClick().equals(ClickType.CONTROL_DROP))
        {
            if (currentItem.getType().toString().contains("SHULKER_BOX"))
            {
                event.getWhoClicked().getWorld().dropItem(event.getWhoClicked().getLocation(),
                        event.getCurrentItem(), (Item) ->
                        {
                            Item.setVelocity(event.getWhoClicked().getLocation().getDirection().multiply(0.35).setY(0.25));
                        });
                event.getCurrentItem().setAmount(0);
            }
        }

    }

}
