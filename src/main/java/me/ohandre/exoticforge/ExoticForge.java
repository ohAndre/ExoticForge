package me.ohandre.exoticforge;

import me.ohandre.exoticforge.Commands.Hammer;
import me.ohandre.exoticforge.Events.AnvilInteractListener;
import me.ohandre.exoticforge.Events.BlastFurnaceInteractListener;
import me.ohandre.exoticforge.Events.CauldronInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ExoticForge extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new AnvilInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new CauldronInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlastFurnaceInteractListener(this), this);
        this.getCommand("martello").setExecutor(new Hammer());
    }

    // Metodo per mostrare le particelle
    public void showParticles(Location location) {
        new BukkitRunnable() {
            @Override
            public void run() {
                location.getWorld().spawnParticle(Particle.CRIT, location.add(0.5, 1, 0.5), 30, 0.5, 0.5, 0.5, 0.05);
            }
        }.runTask(this);
    }

    // Metodo per mostrare le particelle di completamento
    public void doneParticles(Location location) {
        new BukkitRunnable() {
            @Override
            public void run() {
                location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location.add(0.5, 1, 0.5), 30, 0.5, 0.5, 0.5, 0.05);
            }
        }.runTask(this);
    }

    // Metodo per far cadere l'oggetto
    public void dropItem(Location location, ItemStack item) {
        location.getWorld().dropItemNaturally(location.add(0.5, 1, 0.5), item);
    }
}
