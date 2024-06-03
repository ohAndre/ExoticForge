package me.ohandre.exoticforge.Events;

import me.ohandre.exoticforge.ExoticForge;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

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
        if (event.getHand() != EquipmentSlot.HAND)
            return; // Verifica se l'interazione è stata fatta con la mano principale

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItem();

        if (block != null && block.getType() == Material.CAULDRON) {
            if (itemInHand != null && itemInHand.getType() == Material.LAVA_BUCKET) {
                event.setCancelled(true);

                if (itemInHand.hasItemMeta() && itemInHand.getItemMeta().hasLore()) {
                    if (itemInHand.getItemMeta().getLore().contains(ChatColor.AQUA + "Minerale: Oro")) {
                        handleBucketClick(player, block, goldBucketClicks, "oro", "secchio d'oro", "gold");
                    } else if (itemInHand.getItemMeta().getLore().contains(ChatColor.AQUA + "Minerale: Ferro")) {
                        handleBucketClick(player, block, ironBucketClicks, "ferro", "secchio di ferro", "iron");
                    }
                }
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (block.hasMetadata("gold") && block.hasMetadata("iron")) {
                    player.sendMessage("Sono presenti sia del ferro che dell'oro.");
                } else if (block.hasMetadata("iron")) {
                    player.sendActionBar("§bE' presente del ferro");
                } else if (block.hasMetadata("gold")) {
                    player.sendActionBar("§bE' presente dell'oro");
                } else {
                    player.sendActionBar("§cNon è presente niente");
                }
            }
        }
    }

    private void handleBucketClick(Player player, Block block, Map<UUID, Boolean> bucketClicks, String metadataKey, String bucketName, String metalType) {
        if (bucketClicks.getOrDefault(player.getUniqueId(), false)) {
            player.sendMessage("Hai già usato il " + bucketName + ".");
        } else {
            bucketClicks.put(player.getUniqueId(), true);
            player.sendActionBar("§fHai inserito: §b" + bucketName);

            block.setMetadata(metalType, new FixedMetadataValue(plugin, true));

            if (goldBucketClicks.getOrDefault(player.getUniqueId(), false) && ironBucketClicks.getOrDefault(player.getUniqueId(), false)) {
                performAction(player, block);
            }
        }
    }


// All'interno della classe CauldronInteractListener

    private void performAction(Player player, Block block) {
        player.sendMessage("§cFondendo... aspetta 10 secondi");

        // Aggiungi un ritardo di 10 secondi prima dell'esecuzione di performAction
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.doneParticles(block.getLocation());
                player.sendMessage("§aHai ottenuto: §9Oro Blu");
                ItemStack replacedItem = new ItemStack(Material.LAVA_BUCKET);
                ItemMeta replacedItemMeta = replacedItem.getItemMeta();
                replacedItemMeta.setDisplayName("§6Minerale Fuso");
                replacedItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                replacedItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.WHITE + "Ottieni questo oggetto tramite");
                lore.add(ChatColor.WHITE + "la forgia e scopri le nuove leghe!");
                lore.add("");
                lore.add(ChatColor.BLUE + "Lega: Oro Blu");
                replacedItemMeta.setLore(lore);
                replacedItem.setItemMeta(replacedItemMeta);
                player.getInventory().addItem(replacedItem);

                block.removeMetadata("gold", plugin);
                block.removeMetadata("iron", plugin);

                goldBucketClicks.put(player.getUniqueId(), false);
                ironBucketClicks.put(player.getUniqueId(), false);
            }
        }.runTaskLater(plugin, 200);
    }
}


