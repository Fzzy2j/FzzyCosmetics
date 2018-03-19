package me.fzzy.fzzycosmetics.util;

import me.fzzy.fzzycosmetics.FzzyCosmetics;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Methods {

    public static ItemStack setSkin(ItemStack item, String nick) {
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(nick);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(ItemStack i, String name, String lore) {
        ArrayList<String> a = new ArrayList<>();
        a.add(lore);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(a);
        i.setItemMeta(meta);
        return i;
    }

    public static ItemStack createItem(ItemStack i, String name) {
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(name);
        i.setItemMeta(meta);
        return i;
    }

    public static ItemStack createItem(ItemStack i, String name, ArrayList<String> lore) {
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }

    public static ItemStack createColorArmor(ItemStack i, Color c) {
        LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
        meta.setColor(c);
        i.setItemMeta(meta);
        return i;
    }

    public void firework(Color color, Location loc) {
        FireworkEffect effect = FireworkEffect.builder().trail(true).withColor(color).build();
        final Firework firework = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta fwm = firework.getFireworkMeta();
        fwm.addEffect(effect);
        firework.setFireworkMeta(fwm);
        new BukkitRunnable() {
            @Override
            public void run() {
                firework.detonate();
            }
        }.runTaskLater(FzzyCosmetics.plugin, 1L);
    }

    public static String getPrettyString(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

}
