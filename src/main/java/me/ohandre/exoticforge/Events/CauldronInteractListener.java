package me.ohandre.exoticforge.Events;

import me.ohandre.exoticforge.ExoticForge;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class CauldronInteractListener implements Listener {
    private final ExoticForge plugin;
    private final Map<UUID, Boolean> goldBucketClicks = new HashMap<>();
    private final Map<UUID, Boolean> ironBucketClicks = new HashMap<>();

    public CauldronInteractListener(ExoticForge plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItem();

        if (block != null && block.getType() == Material.CAULDRON) {
            if (itemInHand != null && itemInHand.getType() == Material.LAVA_BUCKET) {
                event.setCancelled(true);

                if (itemInHand.hasItemMeta() && itemInHand.getItemMeta().hasLore()) {
                    if (itemInHand.getItemMeta().getLore().contains(ChatColor.AQUA + "Minerale: Oro")) {
                        handleBucketClick(player, block, goldBucketClicks, "oro", "secchio d'oro");
                    } else if (itemInHand.getItemMeta().getLore().contains(ChatColor.AQUA + "Minerale: Ferro")) {
                        handleBucketClick(player, block, ironBucketClicks, "ferro", "secchio di ferro");
                    }
                }
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (block.hasMetadata("ferro")) {
                    player.sendMessage("E' presente del ferro.");
                }
                if (block.hasMetadata("oro")) {
                    player.sendMessage("E' presente dell'oro.");
                }
            }
        }
    }

    private void handleBucketClick(Player player, Block block, Map<UUID, Boolean> bucketClicks, String metadataKey, String bucketName) {
        if (bucketClicks.getOrDefault(player.getUniqueId(), false)) {
            player.sendMessage("Hai già usato il " + bucketName + ".");
        } else {
            bucketClicks.put(player.getUniqueId(), true);
            player.sendMessage("Hai riempito con il " + bucketName + ".");

            if (goldBucketClicks.getOrDefault(player.getUniqueId(), false) && ironBucketClicks.getOrDefault(player.getUniqueId(), false)) {
                performAction(player, block);
            }
        }
    }

    private void performAction(Player player, Block block) {
        plugin.doneParticles(block.getLocation());

        ItemStack replacedItem = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta replacedItemMeta = replacedItem.getItemMeta();
        replacedItemMeta.setDisplayName("§6Minerale Fuso");
        replacedItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        replacedItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "Ottieni questo oggetto tramite");
        lore.add(ChatColor.WHITE + "la forgia e scopri le nuove leghe!");
        lore.add("");
        lore.add(ChatColor.AQUA + "Minerale: Oro Blu");
        replacedItemMeta.setLore(lore);
        replacedItem.setItemMeta(replacedItemMeta);
        player.getInventory().addItem(replacedItem);

        block.setMetadata("ferro", new FixedMetadataValue(plugin, 1));

        goldBucketClicks.put(player.getUniqueId(), false);
        ironBucketClicks.put(player.getUniqueId(), false);
    }
}
