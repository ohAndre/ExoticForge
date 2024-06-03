package me.ohandre.exoticforge.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Hammer implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack hammer = new ItemStack(Material.IRON_AXE);
            ItemMeta meta = hammer.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.GOLD + "Martello");
                hammer.setItemMeta(meta);
            }
            player.getInventory().addItem(hammer);
            player.sendMessage(ChatColor.GREEN + "Hai ricevuto un Martello!");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Questo comando può essere usato solo da un giocatore.");
        return false;
    }
}
