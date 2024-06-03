package me.ohandre.exoticforge.Events;

import me.ohandre.exoticforge.ExoticForge;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnvilInteractListener implements Listener {
    private final ExoticForge plugin;
    public AnvilInteractListener(ExoticForge plugin) {
        this.plugin = plugin;
    }
    private final Map<UUID, Integer> clickCounts = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            Player player = event.getPlayer();
            ItemStack itemInHand = event.getItem();
            UUID playerId = player.getUniqueId();

            if (block != null && block.getType() == Material.ANVIL) {
                if (itemInHand != null && itemInHand.getType() == Material.IRON_AXE
                        && itemInHand.getItemMeta() != null
                        && "Martello".equals(ChatColor.stripColor(itemInHand.getItemMeta().getDisplayName()))) {

                    clickCounts.put(playerId, clickCounts.getOrDefault(playerId, 0) + 1);

                    if (clickCounts.get(playerId) < 3) {
                        event.setCancelled(true);
                        plugin.showParticles(block.getLocation());
                        return;
                    }

                    clickCounts.put(playerId, 0);
                    event.setCancelled(true);

                    Location locationAbove = block.getLocation().add(0.5, 1, 0.5);
                    boolean itemFrameFound = false;
                    ItemFrame foundItemFrame = null;

                    for (Entity entity : block.getWorld().getNearbyEntities(locationAbove, 0.5, 0.5, 0.5)) {
                        if (entity instanceof ItemFrame) {
                            foundItemFrame = (ItemFrame) entity;
                            itemFrameFound = true;
                            break;
                        }
                    }

                    if (itemFrameFound) {

                        ItemStack itemInFrame = foundItemFrame.getItem();
                        if (itemInFrame.getType() == Material.GOLD_ORE) {
                            plugin.doneParticles(block.getLocation());
                            plugin.dropItem(block.getLocation(), new ItemStack(Material.RAW_GOLD));
                        } else if (itemInFrame.getType() == Material.IRON_ORE) {
                            plugin.doneParticles(block.getLocation());
                            plugin.dropItem(block.getLocation(), new ItemStack(Material.RAW_IRON));
                        }
                        foundItemFrame.remove();
                        return;
                    }
                } else if (itemInHand != null && itemInHand.getType() == Material.IRON_ORE) {
                    event.setCancelled(true);
                    placeItemFrame(block, itemInHand, Material.IRON_ORE, player);
                } else if (itemInHand != null && itemInHand.getType() == Material.GOLD_ORE) {
                    event.setCancelled(true);
                    placeItemFrame(block, itemInHand, Material.GOLD_ORE, player);
                }
            }
        }
    }

    private void placeItemFrame(Block block, ItemStack itemInHand, Material material, Player player) {
        Location locationAbove = block.getLocation().add(0.5, 1, 0.5);
        for (Entity entity : block.getWorld().getNearbyEntities(locationAbove, 0.5, 0.5, 0.5)) {
            if (entity instanceof ItemFrame) {
                return;
            }
        }

        ItemFrame itemFrame = block.getWorld().spawn(locationAbove, ItemFrame.class, frame -> {
            frame.setItem(new ItemStack(material));
            frame.setVisible(false);
            frame.setFixed(true);
        });

        if (player.getGameMode() != GameMode.CREATIVE) {
            itemInHand.setAmount(itemInHand.getAmount() - 1);
        }
    }
}
