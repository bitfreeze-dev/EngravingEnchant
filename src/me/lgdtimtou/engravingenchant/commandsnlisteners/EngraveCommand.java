package me.lgdtimtou.engravingenchant.commandsnlisteners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.lgdtimtou.engravingenchant.Main;
import me.lgdtimtou.engravingenchant.Utilities;
import me.lgdtimtou.engravingenchant.registering.EngravingEnchant;
import net.md_5.bungee.api.ChatColor;

public class EngraveCommand implements CommandExecutor{

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player target = null;
		
		if (!(sender instanceof Player))
			return false;
		Player player = (Player) sender;
		
		
		
		if (args.length == 0 || args.length > 2) {
			player.sendMessage(Utilities.getConfigString("UsageMessage"));
			return false;
		}
		
		if (args.length == 1) {
			target = (Player) sender;
		}	
		if (args.length == 2) {
			if (Bukkit.getServer().getPlayerExact(args[1]) == null) {
				player.sendMessage(Utilities.getConfigString("PlayerOfflineMessage"));
				return false;
			}
			target = Bukkit.getServer().getPlayerExact(args[1]);
					
				
				
		}
		if (args[0].toLowerCase().contentEquals("add")) {
			if (!player.hasPermission("engrave.command.add")) {
				player.sendMessage(Utilities.getConfigString("NoPermissionMessage"));
				return false;
			}
			if (target.getInventory().getItemInMainHand().getType() == Material.AIR) {
				player.sendMessage(Utilities.getConfigString("NoItemMessage"));
				return false;
			}
			ItemStack olditem = target.getInventory().getItemInMainHand();
			ItemMeta oldmeta = olditem.getItemMeta();
			Material itemmaterial = target.getInventory().getItemInMainHand().getType();
			if (oldmeta.hasLore()) {
				for (String str : oldmeta.getLore()) {
					if (str.contains(Main.enchantName)) {
						player.sendMessage(Utilities.getConfigString("AlreadyEngravedMessage"));
						return false;
					}
				}
						
			}
			ItemStack item = null;
			if (itemmaterial == Material.BOOK) {
				item = new ItemStack(Material.ENCHANTED_BOOK);
				item.addEnchantments(olditem.getEnchantments());
			} 
			else if (itemmaterial == Material.ENCHANTED_BOOK) {
				player.sendMessage(Utilities.getConfigString("EngravedAddEnchantedBookMessage"));
				return false;
			}
			else {
				item = new ItemStack(itemmaterial);
				item.addEnchantments(olditem.getEnchantments());
			}
			target.getInventory().removeItem(olditem);
			item.addUnsafeEnchantment(EngravingEnchant.ENGRAVING, 1);
				
				
			ItemMeta newmeta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
	
				
				
			lore.add(ChatColor.GRAY + Main.enchantName);
			if (oldmeta.hasLore())
				for (String l : oldmeta.getLore())
					lore.add(l);
			newmeta.setLore(lore);
			newmeta.setDisplayName(oldmeta.getDisplayName());
			item.setItemMeta(newmeta);
				
	
				
			target.getInventory().addItem(item);
			return true;
		}
		if (args[0].toLowerCase().contentEquals("list")) {
			if (!player.hasPermission("engrave.command.list")) {
				player.sendMessage(Utilities.getConfigString("NoPermissionMessage"));
				return false;
			}
			if (target.getInventory().getItemInMainHand().getType() == Material.AIR) {
				player.sendMessage(Utilities.getConfigString("NoItemMessage"));
				return false;
			}
			ItemStack item = target.getInventory().getItemInMainHand();
			ItemMeta meta = item.getItemMeta();

			if (!Utilities.checkEngraved(item) || !meta.hasLore()) {
				player.sendMessage(Utilities.getConfigString("NotEngravedMessage"));
				return false;
			}
			List<String> lore = meta.getLore();
			String strlore = Utilities.stringBuilder(lore);
			int index = strlore.indexOf(Main.killedPrefix);
			if (index == -1) {
				player.sendMessage(Utilities.getConfigString("NoPlayersMessage"));
				return false;
			}
			String strloresub = strlore.substring(index, strlore.length() - 2);
	
			player.sendMessage(Utilities.getConfigString("EngravedListColor") + strloresub);
			System.out.println(index);	
			System.out.println(strloresub);
			
			return true;
		}
		if (!player.hasPermission("engrave.command.itemtype")) {
			player.sendMessage(Utilities.getConfigString("NoPermissionMessage"));
			return false;
		}
		String material = args[0];
		ItemStack item = null;
		try {
			item = new ItemStack(Material.getMaterial(material.toUpperCase()));
		} catch (Exception e) {
			player.sendMessage(Utilities.getConfigString("InvalidArgumentsMessage"));
			return false;
		}
		item.addUnsafeEnchantment(EngravingEnchant.ENGRAVING, 1);
			
			
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();

			
			
		lore.add(ChatColor.GRAY + Main.enchantName);
		if (meta.hasLore())
			for (String l : meta.getLore())
					lore.add(l);
		meta.setLore(lore);
		item.setItemMeta(meta);
			

			
		target.getInventory().addItem(item);
		return true;
		
		
		
	}

}
