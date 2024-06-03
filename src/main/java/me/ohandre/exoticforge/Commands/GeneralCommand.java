package me.ohandre.exoticforge.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GeneralCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Utilizzo: /" + label + " <bucket|give> [tipo]");
            return false;
        }

        if (args[0].equalsIgnoreCase("bucket")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Questo comando può essere usato solo da un giocatore.");
                return false;
            }

            if (args.length != 2) {
                sender.sendMessage(ChatColor.RED + "Utilizzo: /" + label + " bucket <tipo>");
                return false;
            }

            Player player = (Player) sender;
            String mineralType = args[1].toLowerCase();
            ItemStack bucket = null;

            if (mineralType.equals("ferro")) {
                bucket = createBucket("Ferro");
            } else if (mineralType.equals("oro")) {
                bucket = createBucket("Oro");
            } else {
                sender.sendMessage(ChatColor.RED + "Tipo di minerale non valido. Usare 'ferro' o 'oro'.");
                return false;
            }

            if (bucket != null) {
                player.getInventory().addItem(bucket);
                player.sendMessage(ChatColor.GREEN + "Hai ricevuto un secchio di " + mineralType + " fuso!");
            }

            return true;
        } else if (args[0].equalsIgnoreCase("give")) {
            if (args.length != 2) {
                sender.sendMessage(ChatColor.RED + "Utilizzo: /" + label + " give <Hammer>");
                return false;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Questo comando può essere usato solo da un giocatore.");
                return false;
            }

            Player player = (Player) sender;
            ItemStack itemToGive = null;

            if (args[1].equalsIgnoreCase("hammer")) {
                itemToGive = createHammer();
            } else {
                sender.sendMessage(ChatColor.RED + "Elemento non valido. Usa 'Hammer'.");
                return false;
            }

            if (itemToGive != null) {
                player.getInventory().addItem(itemToGive);
                player.sendMessage(ChatColor.GREEN + "Hai ricevuto un Martello!");
            }

            return true;
        }

        sender.sendMessage(ChatColor.RED + "Comando sconosciuto. Utilizzo: /" + label + " <bucket|give> [tipo]");
        return false;
    }

    private ItemStack createBucket(String metalType) {
        ItemStack bucket = new ItemStack(Material.LAVA_BUCKET);
        List<String> lore = new ArrayList<>();
        ItemMeta meta = bucket.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "§6Minerale Fuso");
            lore.add(ChatColor.WHITE + "Ottieni questo oggetto tramite");
            lore.add(ChatColor.WHITE + "la forgia e scopri le nuove leghe!");
            lore.add("");
            lore.add(ChatColor.AQUA + "Minerale: " + metalType);
            meta.setLore(lore);
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            bucket.setItemMeta(meta);
        }

        return bucket;
    }

    private ItemStack createHammer() {
        ItemStack hammer = new ItemStack(Material.IRON_AXE);
        ItemMeta meta = hammer.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Martello");
            hammer.setItemMeta(meta);
        }

        return hammer;
    }
}
