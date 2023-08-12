package me.barny1094875.shulkerstackingog.Listeners;

import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class ShulkerBoxPickupHandler implements Listener
{

    @EventHandler
    public void shulkerBoxStacking(EntityPickupItemEvent event)
    {

        ItemStack item = event.getItem().getItemStack();
        if (!event.getEntity().getType().equals(EntityType.PLAYER))
        {
            return;
        }

        Player player = (Player) event.getEntity();
        Inventory playerInventory = player.getInventory();
        if (item.getType().toString().contains("SHULKER_BOX"))
        {
            BlockStateMeta shulker1Meta = (BlockStateMeta) item.getItemMeta();
            ShulkerBox shulker1 = (ShulkerBox) shulker1Meta.getBlockState();
            // check the inventory of the ground item shulker
            if (shulker1.getInventory().isEmpty())
            {
                // check for shulker box stacks to add to
                for (int i = 0; i < playerInventory.getSize(); i++)
                {
                    ItemStack inventoryItem = playerInventory.getItem(i);
                    if (inventoryItem == null)
                    {
                        continue;
                    }
                    if (inventoryItem.getType().equals(item.getType()))
                    {
                        BlockStateMeta shulker2Meta = (BlockStateMeta) inventoryItem.getItemMeta();
                        ShulkerBox shulker2 = (ShulkerBox) shulker2Meta.getBlockState();
                        // check the inventory of the inventory shulker
                        if (shulker2.getInventory().isEmpty())
                        {
                            if (inventoryItem.getAmount() + item.getAmount() <= 64)
                            {
                                inventoryItem.setAmount(inventoryItem.getAmount() + item.getAmount());
                                event.getItem().setHealth(-1);
                                event.setCancelled(true);
                                return;
                            }
                            if (inventoryItem.getAmount() <= 64)
                            {
                                item.setAmount((item.getAmount() + inventoryItem.getAmount()) - 64);
                                if (item.getAmount() <= 0)
                                {
                                    event.getItem().setHealth(-1);
                                    return;
                                }
                                event.getItem().setItemStack(item);
                                inventoryItem.setAmount(64);
                                event.setCancelled(true);
                            }
                        }
                    }
                }

                // look for air gaps to add the shulkers to
                for (int i = 0; i < playerInventory.getSize(); i++)
                {
                    ItemStack inventoryItem = playerInventory.getItem(i);
                    if (inventoryItem == null)
                    {
                        playerInventory.setItem(i, item);
                        event.getItem().setHealth(-1);
                        event.setCancelled(true);
                        return;
                    }
                }

            }
        }
    }

}
