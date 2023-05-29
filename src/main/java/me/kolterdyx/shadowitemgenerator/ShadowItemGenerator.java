package me.kolterdyx.shadowitemgenerator;

import net.minecraft.world.IInventory;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.HashMap;


public class ShadowItemGenerator implements Listener {

    private final HashMap<HumanEntity, Inventory> inventories;
    private final ItemStack fillerItem;
    private final ItemStack generateButton;

    public ShadowItemGenerator() {
        inventories = new HashMap();
        fillerItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta meta = fillerItem.getItemMeta();
        meta.setDisplayName(" ");
        fillerItem.setItemMeta(meta);

        generateButton = new ItemStack(Material.HOPPER, 1);
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Create Shadow Item");
        generateButton.setItemMeta(meta);

        initItems();
    }

    private void initItems(Inventory inventory) {
        for (int i = 1; i < 6; i++) {
            inventory.setItem(i, fillerItem);
        }
        inventory.setItem(3, generateButton);
        inventory.setItem(0, null);
        inventory.setItem(8, null);
    }

    private void createInventory(HumanEntity player) {
        Inventory inv = Bukkit.createInventory(null, 9, "Shadow Item Generator");
        initItems(inv);
        inventories.put(player, inv);
    }

    private void openInventory(final HumanEntity player) {
        if (!inventories.containsKey(player)) {
            createInventory(player);
        }
        Inventory inventory = inventories.get(player);
        player.openInventory(inventory);
    }

    private void generateShadowItem(HumanEntity player) {
        Inventory inventory = inventories.get(player);
        ItemStack shadowItem = inventory.getItem(0);
        net.minecraft.world.item.ItemStack nmsitem = CraftItemStack.asNMSCopy(shadowItem);
        IInventory inv = ((CraftInventory) inventory).getInventory();
        inv.a(8, nmsitem);
        inv.a(7, nmsitem);
        inv.a(6, nmsitem);
        inv.a(0, nmsitem);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!inventories.containsValue(e.getInventory())) return;

        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null) return;
        if (clickedItem.equals(fillerItem)) {
            e.setCancelled(true);
            return;
        }
        if (clickedItem.equals(generateButton)) {
            e.setCancelled(true);
            generateShadowItem(e.getWhoClicked());
        }
    }


    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block.getType() == Material.DROPPER) {
                Nameable dropper = (Nameable) block.getState();
                if (dropper.getCustomName() != null && dropper.getCustomName().equalsIgnoreCase("shadow")) {
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
