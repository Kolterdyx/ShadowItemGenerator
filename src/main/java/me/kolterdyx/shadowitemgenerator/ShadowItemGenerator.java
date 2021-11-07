package me.kolterdyx.shadowitemgenerator;

import net.minecraft.world.IInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class ShadowItemGenerator implements Listener {

    private final Inventory inventory;
    private final ItemStack fillerItem;
    private final ItemStack generateButton;

    public ShadowItemGenerator(){
        inventory = Bukkit.createInventory(null, 9, "Shadow Item Generator");
        fillerItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta meta = fillerItem.getItemMeta();
        meta.setDisplayName(" ");
        fillerItem.setItemMeta(meta);

        generateButton = new ItemStack(Material.HOPPER, 1);
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Create Shadow Item");
        generateButton.setItemMeta(meta);

        initItems();
    }

    private void initItems(){
        for(int i=1;i<6;i++){
            inventory.setItem(i, fillerItem);
        }
        inventory.setItem(3, generateButton);
        inventory.setItem(0, null);
        inventory.setItem(8, null);
    }

    private void openInventory(final HumanEntity player){
        player.openInventory(inventory);
    }

    private void generateShadowItem() {
        ItemStack shadowItem = inventory.getItem(0);
        net.minecraft.world.item.ItemStack nmsitem = CraftItemStack.asNMSCopy(shadowItem);
        IInventory inv = ((CraftInventory) inventory).getInventory();
        inv.setItem(8, nmsitem);
        inv.setItem(7, nmsitem);
        inv.setItem(6, nmsitem);
        inv.setItem(0, nmsitem);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != inventory) return;


        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null) return;
        if (clickedItem.equals(fillerItem)){
            e.setCancelled(true);
            return;
        }
        if (clickedItem.equals(generateButton)){
            e.setCancelled(true);
            generateShadowItem();
        }

    }


    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block.getType() == Material.DROPPER) {
                Nameable dropper = (Nameable) block.getState();
                if (dropper.getCustomName() != null && dropper.getCustomName().equalsIgnoreCase("shadow")){
                    Inventory dropper_inv = ((Container) dropper).getInventory();
                    for (ItemStack item :
                            dropper_inv.getContents()) {
                        if (item == null) continue;
                        if (item.getItemMeta().getDisplayName().equalsIgnoreCase("shadow") && item.getType() == Material.HOPPER) {
                            event.setCancelled(true);
                            openInventory(event.getPlayer());
                        }
                    }

                }
            }
        }


    }
}
