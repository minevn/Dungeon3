package mk.plugin.dungeon3.util;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStackUtils {
	
	public static boolean isNull(ItemStack item) {
		return item == null || item.getType() == Material.AIR;
	}
	
    public static Inventory fromBase64(String data, String title) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt(), title);
    
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            dataInput.close();
            inputStream.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
    
    public static Map<Integer, String> toBase64ItemStackm(Map<Integer, ItemStack> items) {
        try { 
            Map<Integer, String> map = new HashMap<Integer, String> ();
            
            for (int i : items.keySet()) {
            	map.put(i, toBase64ItemStack(items.get(i)));
            }
            return map;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }        
    }
    
    public static ItemStack toItemStack(String base64) {
    	try {
        	ByteArrayInputStream bais = new ByteArrayInputStream(Base64Coder.decodeLines(base64));
			BukkitObjectInputStream ois = new BukkitObjectInputStream(bais);
			
			ItemStack item = (ItemStack) ois.readObject();
			ois.close();
			bais.close();
			return item;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
	
    public static String toBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());
            
            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            
            // Serialize that array
            dataOutput.close();
            outputStream.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }        
    }
    
    public static String toBase64ItemStack(ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            dataOutput.writeObject(item);
            
            // Serialize that array
            dataOutput.close();
            outputStream.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }        
    }
    
    public static boolean hasTag(ItemStack item, String key) {
    	if (item == null) return false;
    	net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item.clone());
    	if (!nmsItemStack.hasTag()) return false;
    	NBTTagCompound tags = nmsItemStack.getTag();
    	if (tags.hasKey(key)) return true;
    	
    	return false; 	
    }
    
    public static String getTag(ItemStack item, String key) {
    	if (item == null) return null;
//    	if (!hasTag(item, key)) return null;
    	net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item.clone());
    	if (!nmsItemStack.hasTag()) return null;
    	NBTTagCompound tags = nmsItemStack.getTag();
    	if (!tags.hasKey(key)) return null;
    	return tags.getString(key);
    }
    
    public static ItemStack setTag(ItemStack item, String key, String value) {
    	net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item.clone());
    	NBTTagCompound tags = nmsItemStack.getTag();
    	tags.setString(key, value);
    	nmsItemStack.setTag(tags);
    	return CraftItemStack.asBukkitCopy(nmsItemStack);
    }
    
    public static ItemStack setTag(ItemStack item, Map<String, String> map) {
    	net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item.clone());
    	NBTTagCompound tags = nmsItemStack.getTag();
    	for (String key : map.keySet()) {
    		tags.setString(key, map.get(key));
    	}
    	nmsItemStack.setTag(tags);
    	return CraftItemStack.asBukkitCopy(nmsItemStack);
    }
    
    public static ItemStack removeTag(ItemStack item, String key) {
//    	if (!hasTag(item, key)) return item;
    	net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item.clone());
    	if (!nmsItemStack.hasTag()) return item;
    	NBTTagCompound tags = nmsItemStack.getTag();
    	if (!tags.hasKey(key)) return item;
    	tags.remove(key);
    	nmsItemStack.setTag(tags);
    	return CraftItemStack.asBukkitCopy(nmsItemStack);
    }
//    
//    public static Map<String, String> getTags(ItemStack itemStack) {
//    	Map<String, String> map = new HashMap<String, String> ();
//    	if (itemStack == null) return map;
//    	ItemStack item = itemStack.clone();
//    	net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);
//    	NBTTagCompound tags = nmsItemStack.getTag();
//    	for (String s : tags.getKeys()) {
//    		map.put(s, tags.getString(s));
//    	}
//    	
//    	return map;
//    }
    
    public static boolean hasLore(ItemStack item) {
    	if (item == null) return false;
    	if (!item.hasItemMeta()) return false;
    	return item.getItemMeta().hasLore();
    }
    
    public static List<String> getLore(ItemStack item) {
    	if (!hasLore(item)) return new ArrayList<String>();
    	return item.getItemMeta().getLore();
    }
    
    public static void setLore(ItemStack item, List<String> lore) {
    	ItemMeta meta = item.getItemMeta();
    	meta.setLore(lore);
    	item.setItemMeta(meta);
    }
    
    public static void addLoreLine(ItemStack item, String line) {
    	List<String> lore = getLore(item);
    	lore.add(line);
    	setLore(item, lore);
    }
    
    public static void addLore(ItemStack item, List<String> lore) {
    	for (int i = 0 ; i < lore.size() ; i++) {
    		addLoreLine(item, lore.get(i));
    	}
    }
    
    public static void setDisplayName(ItemStack item, String name) {
    	if (item == null || item.getType() == Material.AIR) return;
    	ItemMeta meta = item.getItemMeta();
    	meta.setDisplayName(name);
    	item.setItemMeta(meta);
    }
    
    public static String getName(ItemStack item) {
    	if (item.getItemMeta().hasDisplayName()) {
    		return item.getItemMeta().getDisplayName();
    	}
    	return "§f" + item.getType().name().toLowerCase().replace("_", " ");
    }
    
    public static void addFlag(ItemStack item, ItemFlag flag) {
    	ItemMeta meta = item.getItemMeta();
    	meta.addItemFlags(flag);
    	item.setItemMeta(meta);
    }
    
    public static void setUnbreakable(ItemStack item, boolean bool) {
    	ItemMeta meta = item.getItemMeta();
    	meta.setUnbreakable(bool);
    	item.setItemMeta(meta);
    }
    
    public static void addEnchant(ItemStack item, Enchantment enchant, int lv) {
    	ItemMeta meta = item.getItemMeta();
    	meta.addEnchant(enchant, lv, false);
    	item.setItemMeta(meta);
    }
    
    public static void addEnchantEffect(ItemStack item) {
		ItemStackUtils.addEnchant(item, Enchantment.DURABILITY, 1);
		ItemStackUtils.addFlag(item, ItemFlag.HIDE_ENCHANTS);
    }
    
    public static ItemStack subtractItem(ItemStack item, int amount) {
    	if (item == null) return null;
    	if (item.getAmount() <= amount) return null;
    	item.setAmount(item.getAmount() - amount);
    	return item;
    }
    
}
