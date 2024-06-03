package me.ohandre.exoticforge.Events;

import me.ohandre.exoticforge.ExoticForge;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlastFurnace;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BlastFurnaceInteractListener implements Listener {

    private final ExoticForge plugin;
    public BlastFurnaceInteractListener(ExoticForge plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItem();

        if (block != null && block.getType() == Material.BLAST_FURNACE) {
            BlastFurnace blastFurnace = (BlastFurnace) block.getState();
            ItemStack result = blastFurnace.getInventory().getResult();
            ItemStack replacedItem = new ItemStack(Material.LAVA_BUCKET);
            ItemMeta replacedItemMeta = replacedItem.getItemMeta();
            List<String> lore = new ArrayList<>();
            if (itemInHand != null && itemInHand.getType() == Material.BUCKET) {
                event.setCancelled(true);
                replacedItemMeta.setDisplayName("§6Minerale Fuso");
                replacedItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                replacedItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                if (blastFurnace.getInventory().contains(Material.IRON_INGOT, 2)) {
                    result.setAmount(result.getAmount() - 2);
                    plugin.doneParticles(block.getLocation());
                    lore.add(ChatColor.WHITE + "Ottieni questo oggetto tramite");
                    lore.add(ChatColor.WHITE + "la forgia e scopri le nuove leghe!");
                    lore.add("");
                    lore.add(ChatColor.AQUA + "Minerale: Ferro");
                    replacedItemMeta.setLore(lore);
                    replacedItem.setItemMeta(replacedItemMeta);
                    itemInHand.setAmount(itemInHand.getAmount() - 1);
                    player.getInventory().addItem(replacedItem);
                }
                if (blastFurnace.getInventory().contains(Material.GOLD_INGOT, 2)) {
                    result.setAmount(result.getAmount() - 2);
                    plugin.doneParticles(block.getLocation());
                    lore.add(ChatColor.WHITE + "Ottieni questo oggetto tramite");
                    lore.add(ChatColor.WHITE + "la forgia e scopri le nuove leghe!");
                    lore.add("");
                    lore.add(ChatColor.AQUA + "Minerale: Oro");
                    replacedItemMeta.setLore(lore);
                    replacedItem.setItemMeta(replacedItemMeta);
                    itemInHand.setAmount(itemInHand.getAmount() - 1);
                    player.getInventory().addItem(replacedItem);
                }
            }
        }
    }
}
